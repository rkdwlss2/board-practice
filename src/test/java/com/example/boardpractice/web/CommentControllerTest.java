package com.example.boardpractice.web;

import com.example.boardpractice.web.dto.comment.CommentDeleteRequestDto;
import com.example.boardpractice.web.dto.comment.CommentCreateRequestDto;
import com.example.boardpractice.web.dto.comment.CommentUpdateResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CommentControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void commentList_get_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 1L;



        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(get("/boards/posts/{boardId}/comment",boardId)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        // 직렬화 수행
        List<CommentUpdateResponseDto> commentResponseDtoList = om.readValue(
                responseBody,
                new TypeReference<List<CommentUpdateResponseDto>>() {
                }
        );

        System.out.println("responseBody = "+responseBody);
        // Then 결과 검증 - 상태코드 확인
        assertThat(commentResponseDtoList).hasSize(2);
        assertThat(commentResponseDtoList.get(0).getCommentId()).isEqualTo(1L);
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void board_create_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 2024L;
        CommentCreateRequestDto commentCreateRequestDto =
                CommentCreateRequestDto.builder()
                        .content("This is a comment")
                        .build();

        String requestBody = om.writeValueAsString(commentCreateRequestDto);
        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/boards/posts/{boardId}/comment",boardId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        resultActions.andExpect(status().isOk());
    }
    @Test
    public void board_update_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 2024L;
        CommentCreateRequestDto commentCreateRequestDto =
                CommentCreateRequestDto.builder()
                        .content("This is a comment")
                        .build();
        String requestBody = om.writeValueAsString(commentCreateRequestDto);
        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(put("/boards/posts/{boardId}/comment",boardId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void board_delete_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 2024L;

        CommentDeleteRequestDto commentDeleteRequestDto = new CommentDeleteRequestDto();
        String requestBody = om.writeValueAsString(commentDeleteRequestDto);
        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(put("/boards/posts/{boardId}/comment",boardId)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isOk());
    }



}
