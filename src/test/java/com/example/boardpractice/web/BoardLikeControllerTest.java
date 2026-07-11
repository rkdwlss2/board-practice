package com.example.boardpractice.web;

import com.example.boardpractice.web.dto.Board.BoardCreateResponseDto;
import com.example.boardpractice.web.dto.Board.BoardRequestDto;
import com.example.boardpractice.web.dto.user.UserLoginRequestDto;
import com.example.boardpractice.web.dto.user.UserSignupRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardLikeControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private UserLoginRequestDto signup() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail("like" + suffix + "@gmail.com");
        userSignupRequestDto.setPassword("Asdf!12345");
        userSignupRequestDto.setNickname("l" + suffix);

        mvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(userSignupRequestDto)))
                .andExpect(status().isCreated());

        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail(userSignupRequestDto.getEmail());
        userLoginRequestDto.setPassword(userSignupRequestDto.getPassword());
        return userLoginRequestDto;
    }

    private MockHttpSession login(UserLoginRequestDto userLoginRequestDto) throws Exception {
        MvcResult result = mvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(userLoginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(true);
    }

    private Long createBoard(MockHttpSession session) throws Exception {
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("좋아요 테스트 게시글");
        boardRequestDto.setContent("좋아요 테스트 게시글 내용");

        MvcResult result = mvc.perform(post("/boards/posts")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(boardRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        BoardCreateResponseDto responseDto = om.readValue(
                result.getResponse().getContentAsString(),
                BoardCreateResponseDto.class
        );
        return responseDto.getBoardId();
    }

    @Test
    public void likes_plus_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        Long boardId = createBoard(session);

        ResultActions resultActions = mvc.perform(post("/boards/likes/{boardId}", boardId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void likes_minus_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        Long boardId = createBoard(session);

        mvc.perform(post("/boards/likes/{boardId}", boardId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ResultActions resultActions = mvc.perform(delete("/boards/likes/{boardId}", boardId)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }
}
