package com.example.boardpractice.web.api;

import com.example.boardpractice.service.AiFitCheckService;
import com.example.boardpractice.web.dto.ai.FitCheckRequestDto;
import com.example.boardpractice.web.dto.ai.FitCheckResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiFitCheckController {

    private final AiFitCheckService aiFitCheckService;

    @PostMapping("/ai/fit-check/posts/{postId}")
    public ResponseEntity<FitCheckResponseDto> checkFit(
            @PathVariable Long postId,
            @RequestBody @Valid FitCheckRequestDto requestDto
    ) {
        return ResponseEntity.ok(aiFitCheckService.checkFit(postId, requestDto));
    }
}
