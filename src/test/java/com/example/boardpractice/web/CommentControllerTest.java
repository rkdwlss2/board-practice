package com.example.boardpractice.web;

import com.example.boardpractice.web.dto.Board.BoardCreateResponseDto;
import com.example.boardpractice.web.dto.Board.BoardRequestDto;
import com.example.boardpractice.web.dto.comment.CommentCreateRequestDto;
import com.example.boardpractice.web.dto.comment.CommentCreateResponseDto;
import com.example.boardpractice.web.dto.comment.CommentUpdateResponseDto;
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
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    private UserLoginRequestDto signup() throws Exception {
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail("comment" + suffix + "@gmail.com");
        userSignupRequestDto.setPassword("Asdf!12345");
        userSignupRequestDto.setNickname("c" + suffix);

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
        boardRequestDto.setTitle("댓글 테스트 게시글");
        boardRequestDto.setContent("댓글 테스트 게시글 내용");

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

    private Long createComment(Long boardId, MockHttpSession session) throws Exception {
        CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                .content("This is a comment")
                .build();

        MvcResult result = mvc.perform(post("/boards/posts/{boardId}/comment", boardId)
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(commentCreateRequestDto)))
                .andExpect(status().isOk())
                .andReturn();

        CommentCreateResponseDto responseDto = om.readValue(
                result.getResponse().getContentAsString(),
                CommentCreateResponseDto.class
        );
        return responseDto.getCommentId();
    }

    @Test
    public void commentList_get_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        Long boardId = createBoard(session);
        Long commentId = createComment(boardId, session);

        ResultActions resultActions = mvc.perform(get("/boards/posts/{boardId}/comment", boardId));
        resultActions.andExpect(status().isOk());

        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        List<CommentUpdateResponseDto> commentResponseDtoList = om.readValue(
                responseBody,
                new TypeReference<List<CommentUpdateResponseDto>>() {}
        );

        assertThat(commentResponseDtoList).hasSize(1);
        assertThat(commentResponseDtoList.get(0).getCommentId()).isEqualTo(commentId);
    }

    @Test
    public void board_create_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        Long boardId = createBoard(session);
        CommentCreateRequestDto commentCreateRequestDto = CommentCreateRequestDto.builder()
                .content("This is a comment")
                .build();

        ResultActions resultActions = mvc.perform(post("/boards/posts/{boardId}/comment", boardId)
                .session(session)
                .content(om.writeValueAsString(commentCreateRequestDto))
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void board_update_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        Long boardId = createBoard(session);
        Long commentId = createComment(boardId, session);

        ResultActions resultActions = mvc.perform(put("/boards/posts/comments/{commentId}", commentId)
                .session(session)
                .content("{\"content\":\"updated comment\"}")
                .contentType(MediaType.APPLICATION_JSON));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void comment_update_forbidden_when_not_owner_test() throws Exception {
        UserLoginRequestDto ownerLoginRequestDto = signup();
        MockHttpSession ownerSession = login(ownerLoginRequestDto);
        UserLoginRequestDto otherLoginRequestDto = signup();
        MockHttpSession otherSession = login(otherLoginRequestDto);
        Long boardId = createBoard(ownerSession);
        Long commentId = createComment(boardId, ownerSession);

        mvc.perform(put("/boards/posts/comments/{commentId}", commentId)
                        .session(otherSession)
                        .content("{\"content\":\"updated comment\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void board_delete_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        Long boardId = createBoard(session);
        Long commentId = createComment(boardId, session);

        ResultActions resultActions = mvc.perform(delete("/boards/posts/comment/{commentId}", commentId)
                .session(session));

        resultActions.andExpect(status().isOk());
    }

    @Test
    public void comment_delete_forbidden_when_not_owner_test() throws Exception {
        UserLoginRequestDto ownerLoginRequestDto = signup();
        MockHttpSession ownerSession = login(ownerLoginRequestDto);
        UserLoginRequestDto otherLoginRequestDto = signup();
        MockHttpSession otherSession = login(otherLoginRequestDto);
        Long boardId = createBoard(ownerSession);
        Long commentId = createComment(boardId, ownerSession);

        mvc.perform(delete("/boards/posts/comment/{commentId}", commentId)
                        .session(otherSession))
                .andExpect(status().isForbidden());
    }
}
