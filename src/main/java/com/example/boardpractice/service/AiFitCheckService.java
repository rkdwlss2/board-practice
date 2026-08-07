package com.example.boardpractice.service;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.web.dto.ai.FitCheckRequestDto;
import com.example.boardpractice.web.dto.ai.FitCheckResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AiFitCheckService {

    private static final String GROQ_CHAT_COMPLETIONS_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    private final BoardService boardService;
    private final ObjectMapper objectMapper;

    @Value("${groq.api-key:}")
    private String groqApiKey;

    @Value("${groq.model:qwen/qwen3.6-27b}")
    private String groqModel;

    public FitCheckResponseDto checkFit(Long postId, FitCheckRequestDto requestDto) {
        if (groqApiKey == null || groqApiKey.isBlank()) {
            throw new IllegalStateException("GROQ_API_KEY가 설정되어 있지 않습니다.");
        }

        Boards board = boardService.findBoardById(postId);
        String outputText = requestGroq(board, requestDto);
        return parseFitCheckResponse(outputText, board.getBoardImageUrl());
    }

    private String requestGroq(Boards board, FitCheckRequestDto requestDto) {
        List<Map<String, Object>> content = new ArrayList<>();
        content.add(Map.of(
                "type", "text",
                "text", buildPrompt(board, requestDto)
        ));

        if (board.getBoardImageUrl() != null && !board.getBoardImageUrl().isBlank()) {
            content.add(Map.of(
                    "type", "image_url",
                    "image_url", Map.of("url", board.getBoardImageUrl())
            ));
        }

        Map<String, Object> body = buildGroqRequestBody(content, true);

        try {
            String responseBody = sendGroqRequest(body);
            return extractOutputText(responseBody);
        } catch (RestClientResponseException e) {
            if (isJsonValidationFailure(e)) {
                try {
                    String responseBody = sendGroqRequest(buildGroqRequestBody(content, false));
                    return extractOutputText(responseBody);
                } catch (RestClientResponseException retryException) {
                    throw buildGroqException(retryException);
                }
            }
            throw buildGroqException(e);
        } catch (RestClientException e) {
            throw new IllegalStateException("Groq 핏 체크 요청에 실패했습니다.", e);
        }
    }

    private Map<String, Object> buildGroqRequestBody(List<Map<String, Object>> content, boolean jsonMode) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("model", groqModel);
        body.put("messages", List.of(
                Map.of(
                        "role", "system",
                        "content", "You are a JSON API. Return only one valid JSON object. Do not include markdown, prose, code fences, reasoning text, or <think> tags."
                ),
                Map.of(
                        "role", "user",
                        "content", content
                )
        ));
        body.put("temperature", 0.2);
        body.put("max_completion_tokens", 1200);
        if (isQwenReasoningModel()) {
            body.put("reasoning_effort", "none");
            body.put("reasoning_format", "hidden");
        }
        if (jsonMode) {
            body.put("response_format", Map.of("type", "json_object"));
        }
        return body;
    }

    private boolean isQwenReasoningModel() {
        return groqModel != null && groqModel.startsWith("qwen/");
    }

    private String sendGroqRequest(Map<String, Object> body) {
        return RestClient.builder()
                .baseUrl(GROQ_CHAT_COMPLETIONS_URL)
                .defaultHeader("Authorization", "Bearer " + groqApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build()
                .post()
                .body(body)
                .retrieve()
                .body(String.class);
    }

    private boolean isJsonValidationFailure(RestClientResponseException e) {
        if (e.getStatusCode().value() != 400) {
            return false;
        }
        String responseBody = e.getResponseBodyAsString();
        return responseBody.contains("Failed to validate JSON")
                || responseBody.contains("failed_generation");
    }

    private ResponseStatusException buildGroqException(RestClientResponseException e) {
        String providerMessage = extractProviderErrorMessage(e.getResponseBodyAsString());
        String suffix = providerMessage.isBlank() ? "" : " (" + providerMessage + ")";
        int statusCode = e.getStatusCode().value();

        if (statusCode == 401 || statusCode == 403) {
            return new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Groq API 키가 올바르지 않거나 권한이 없습니다." + suffix
            );
        }

        if (statusCode == 429) {
            return new ResponseStatusException(
                    HttpStatus.TOO_MANY_REQUESTS,
                    "Groq 사용량 한도에 도달했습니다. 잠시 후 다시 시도하거나 Groq 콘솔의 API 한도를 확인해 주세요." + suffix
            );
        }

        if (statusCode == 400) {
            return new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Groq 요청 형식이 올바르지 않습니다. 모델명과 게시글 이미지 URL이 외부에서 접근 가능한 HTTPS URL인지 확인해 주세요." + suffix
            );
        }

        return new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "Groq 응답 처리에 실패했습니다." + suffix
        );
    }

    private String extractProviderErrorMessage(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            return "";
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode message = root.path("error").path("message");
            return message.isTextual() ? message.asText() : "";
        } catch (JacksonException e) {
            return "";
        }
    }

    private String buildPrompt(Boards board, FitCheckRequestDto requestDto) {
        return """
                /no_think

                너는 아크테릭스 의류/신발 사이즈를 판단하는 핏 어시스턴트다.
                게시글 작성자의 착샷, 게시글 내용, 사용자의 체형 정보를 비교해서 사용자가 해당 제품을 입었을 때 맞을지 판단한다.

                반드시 아래 JSON 형식만 반환한다. 마크다운 코드블록은 쓰지 않는다.
                <think> 같은 추론 과정이나 설명 문장은 절대 출력하지 않는다.
                {
                  "result": "LIKELY_FITS | SIZE_UP | SIZE_DOWN | UNCERTAIN",
                  "label": "사용자에게 보여줄 짧은 결론",
                  "sizeAdvice": "한 문장 조언",
                  "reasons": ["이유 1", "이유 2", "이유 3"]
                }

                판단 기준:
                - 사진만으로 신체 치수를 단정하지 않는다.
                - 어깨선, 소매/기장, 품 여유, 레이어링 가능성만 조심스럽게 추정한다.
                - 작성자 스펙이 본문에 있으면 사용자 스펙과 비교한다.
                - 정보가 부족하면 UNCERTAIN을 선택한다.
                - 의료/체형 평가처럼 들리는 표현은 피하고, 의류 사이즈 조언만 한다.

                게시글 제목: %s
                게시글 내용:
                %s

                사용자 정보:
                키: %dcm
                몸무게: %dkg
                평소 상의 사이즈: %s
                평소 하의 사이즈: %s
                평소 신발 사이즈: %s
                원하는 핏: %s
                """.formatted(
                nullToEmpty(board.getTitle()),
                nullToEmpty(board.getContent()),
                requestDto.getHeight(),
                requestDto.getWeight(),
                nullToEmpty(requestDto.getUsualTopSize()),
                nullToEmpty(requestDto.getUsualBottomSize()),
                nullToEmpty(requestDto.getUsualShoeSize()),
                nullToEmpty(requestDto.getPreferredFit())
        );
    }

    private String extractOutputText(String responseBody) {
        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("AI 응답이 비어 있습니다.");
        }

        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode content = root.path("choices").path(0).path("message").path("content");
            if (content.isTextual() && !content.asText().isBlank()) {
                return content.asText();
            }
        } catch (JacksonException e) {
            throw new IllegalStateException("AI 응답을 해석하지 못했습니다.", e);
        }

        throw new IllegalStateException("AI 응답에서 결과 텍스트를 찾지 못했습니다.");
    }

    private FitCheckResponseDto parseFitCheckResponse(String outputText, String boardImageUrl) {
        try {
            JsonNode root = objectMapper.readTree(extractJsonObject(outputText));
            List<String> reasons = new ArrayList<>();
            JsonNode reasonsNode = root.path("reasons");
            if (reasonsNode.isArray()) {
                for (JsonNode reason : reasonsNode) {
                    if (reason.isTextual()) {
                        reasons.add(reason.asText());
                    }
                }
            }

            return FitCheckResponseDto.builder()
                    .result(root.path("result").asText("UNCERTAIN"))
                    .label(root.path("label").asText("판단이 어렵습니다."))
                    .sizeAdvice(root.path("sizeAdvice").asText("사진과 본문 정보가 부족해 정확한 사이즈 판단이 어렵습니다."))
                    .boardImageUrl(boardImageUrl)
                    .reasons(reasons.isEmpty() ? List.of("착샷, 작성자 스펙, 제품명이 충분하지 않습니다.") : reasons)
                    .build();
        } catch (JacksonException e) {
            return FitCheckResponseDto.builder()
                    .result("UNCERTAIN")
                    .label("판단이 어렵습니다.")
                    .sizeAdvice("AI 응답 형식이 올바르지 않아 정확한 사이즈 판단이 어렵습니다.")
                    .boardImageUrl(boardImageUrl)
                    .reasons(List.of("AI 응답이 정해진 JSON 형식이 아니었습니다."))
                    .build();
        }
    }

    private String extractJsonObject(String text) {
        String cleanedText = stripCodeFence(removeThinkingBlock(text));
        String jsonObject = findFitCheckJsonObject(cleanedText);
        if (jsonObject != null) {
            return jsonObject;
        }
        return stripCodeFence(text);
    }

    private String removeThinkingBlock(String text) {
        return text.replaceAll("(?s)<think>.*?</think>", "").trim();
    }

    private String findFitCheckJsonObject(String text) {
        for (int startIndex = text.lastIndexOf('{'); startIndex >= 0; startIndex = text.lastIndexOf('{', startIndex - 1)) {
            int endIndex = findJsonObjectEnd(text, startIndex);
            if (endIndex < 0) {
                continue;
            }

            String candidate = text.substring(startIndex, endIndex + 1);
            if (isFitCheckJson(candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private int findJsonObjectEnd(String text, int startIndex) {
        int depth = 0;
        boolean inString = false;
        boolean escaping = false;

        for (int i = startIndex; i < text.length(); i++) {
            char current = text.charAt(i);
            if (escaping) {
                escaping = false;
                continue;
            }
            if (current == '\\') {
                escaping = true;
                continue;
            }
            if (current == '"') {
                inString = !inString;
                continue;
            }
            if (inString) {
                continue;
            }
            if (current == '{') {
                depth++;
            } else if (current == '}') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isFitCheckJson(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            String result = root.path("result").asText();
            return List.of("LIKELY_FITS", "SIZE_UP", "SIZE_DOWN", "UNCERTAIN").contains(result)
                    && root.path("label").isTextual()
                    && root.path("sizeAdvice").isTextual()
                    && root.path("reasons").isArray();
        } catch (JacksonException e) {
            return false;
        }
    }

    private String stripCodeFence(String text) {
        return text
                .replaceFirst("^```json\\s*", "")
                .replaceFirst("^```\\s*", "")
                .replaceFirst("\\s*```$", "")
                .trim();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
