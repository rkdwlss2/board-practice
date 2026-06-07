package com.example.boardpractice.web;

import com.example.boardpractice.web.dto.Board.PostDetailResponseDto;
import com.example.boardpractice.web.dto.Board.PostResponseDto;
import com.example.boardpractice.web.dto.user.UserSignupRequestDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void boardList_get_success_test() throws Exception {
        // Given 데이터 세팅
        int page = 1;
        int size = 10;


        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(get("/boards/posts")
                .queryParam("page", Integer.toString(page))
                .queryParam("size",Integer.toString(size))
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        // 직렬화 수행
        List<PostResponseDto> postResponseDtoList = om.readValue(
                responseBody,
                new TypeReference<List<PostResponseDto>>() {
                }
        );

        System.out.println("responseBody = "+responseBody);
        // Then 결과 검증 - 상태코드 확인
        assertThat(postResponseDtoList).hasSize(2);
        assertThat(postResponseDtoList.get(0).getBoardId()).isEqualTo(2024L);
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void boardDetail_get_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 2024L;

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(get("/boards/posts/{boardId}",boardId)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        // 직렬화 수행
        PostDetailResponseDto postDetailResponseDto = om.readValue(
                responseBody,
                PostDetailResponseDto.class
        );

        System.out.println("responseBody = "+postDetailResponseDto.toString());
        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        assertThat(postDetailResponseDto.getBoardId()).isEqualTo(2025L);
        resultActions.andExpect(status().isOk());
    }
}
