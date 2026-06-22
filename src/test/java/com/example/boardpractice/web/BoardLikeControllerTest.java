package com.example.boardpractice.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardLikeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void likes_plus_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 2024L;

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/boards/likes/{board_id}",boardId)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void likes_minus_success_test() throws Exception {
        // Given 데이터 세팅
        Long boardId = 2024L;

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(delete("/boards/likes/{board_id}",boardId)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        resultActions.andExpect(status().isOk());
    }
}
