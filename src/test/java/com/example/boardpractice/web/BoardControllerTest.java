package com.example.boardpractice.web;

import com.example.boardpractice.web.dto.Board.BoardDetailResponseDto;
import com.example.boardpractice.web.dto.Board.BoardResponseDto;
import com.example.boardpractice.web.dto.Board.BoardRequestDto;
import com.example.boardpractice.web.dto.Board.BoardCreateResponseDto;
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
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;


    private UserLoginRequestDto signup() throws Exception{
        String suffix = UUID.randomUUID().toString().substring(0, 8);

        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto();
        userSignupRequestDto.setEmail("russell" + suffix + "@gmail.com");
        userSignupRequestDto.setPassword("Asdf!12345");
        userSignupRequestDto.setNickname("r" + suffix);
        // 회원가입
        mvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(userSignupRequestDto)))
                .andExpect(status().isCreated());

        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail(userSignupRequestDto.getEmail());
        userLoginRequestDto.setPassword(userSignupRequestDto.getPassword());
        return userLoginRequestDto;
    }

    private MockHttpSession login(UserLoginRequestDto userLoginRequestDto) throws Exception{
        // 로그인
        MvcResult result = mvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(userLoginRequestDto)))
                .andExpect(status().isOk())
                .andReturn();
        return (MockHttpSession) result.getRequest().getSession(true);
    }

    private Long makeBoard(BoardRequestDto boardRequestDto,MockHttpSession session) throws Exception{
        MvcResult result = mvc.perform(post("/boards/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(session)
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
    public void boardList_get_success_test() throws Exception {
        // Given 데이터 세팅
        int page = 0;
        int size = 10;
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        // Given 데이터 세팅
        BoardRequestDto board = new BoardRequestDto();
        board.setContent("게시글 내용");
        board.setTitle("오늘의 게시글 제목");
        makeBoard(board,session);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(get("/boards/posts")
                .queryParam("page", Integer.toString(page))
                .queryParam("size",Integer.toString(size))
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> responseMap = om.readValue(responseBody, new TypeReference<Map<String, Object>>() {});
        List<BoardResponseDto> boardResponseDtoList = om.convertValue(
                responseMap.get("content"),
                new TypeReference<List<BoardResponseDto>>() {}
        );


        System.out.println("responseBody = "+responseBody);
        // Then 결과 검증 - 상태코드 확인
        assertThat(boardResponseDtoList).hasSize(1);
        assertThat(boardResponseDtoList.get(0).getTitle()).isEqualTo("오늘의 게시글 제목");
        resultActions.andExpect(status().isOk());
    }

    @Test
    public void boardDetail_get_success_test() throws Exception {

        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        // Given 데이터 세팅
        BoardRequestDto board = new BoardRequestDto();
        board.setContent("게시글 내용");
        board.setTitle("오늘의 게시글 제목");
        Long boardId = makeBoard(board,session);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(get("/boards/posts/{boardId}",boardId)
                .session(session)
        );
        resultActions.andExpect(status().isOk());
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(responseBody);
        // 직렬화 수행
        BoardDetailResponseDto boardDetailResponseDto = om.readValue(
                responseBody,
                BoardDetailResponseDto.class
        );

        System.out.println("responseBody = "+ boardDetailResponseDto.toString());
        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        assertThat(boardDetailResponseDto.getBoardId()).isEqualTo(boardId);
        assertThat(boardDetailResponseDto.getTitle()).isEqualTo("오늘의 게시글 제목");
    }

    @Test
    public void board_update_success_test() throws Exception {

        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);

        BoardRequestDto board = new BoardRequestDto();
        board.setContent("게시글 내용");
        board.setTitle("오늘의 게시글 제목");
        Long boardId = makeBoard(board,session);
        // Given 데이터 세팅
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("게시물 제목");
        boardRequestDto.setContent("게시물 내용123123");

        String requestBody = om.writeValueAsString(boardRequestDto);
        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(put("/boards/posts/{boardId}",boardId)
                .content(requestBody)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        resultActions.andExpect(status().isOk())
                .andDo(print());;
    }

    @Test
    public void board_update_forbidden_when_not_owner_test() throws Exception {
        UserLoginRequestDto ownerLoginRequestDto = signup();
        MockHttpSession ownerSession = login(ownerLoginRequestDto);
        UserLoginRequestDto otherLoginRequestDto = signup();
        MockHttpSession otherSession = login(otherLoginRequestDto);

        BoardRequestDto board = new BoardRequestDto();
        board.setContent("게시글 내용");
        board.setTitle("오늘의 게시글 제목");
        Long boardId = makeBoard(board, ownerSession);

        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("다른 사용자의 수정 시도");
        boardRequestDto.setContent("다른 사용자의 수정 내용");

        mvc.perform(put("/boards/posts/{boardId}", boardId)
                        .content(om.writeValueAsString(boardRequestDto))
                        .session(otherSession)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void board_delete_forbidden_when_not_owner_test() throws Exception {
        UserLoginRequestDto ownerLoginRequestDto = signup();
        MockHttpSession ownerSession = login(ownerLoginRequestDto);
        UserLoginRequestDto otherLoginRequestDto = signup();
        MockHttpSession otherSession = login(otherLoginRequestDto);

        BoardRequestDto board = new BoardRequestDto();
        board.setContent("게시글 내용");
        board.setTitle("오늘의 게시글 제목");
        Long boardId = makeBoard(board, ownerSession);

        mvc.perform(delete("/boards/posts/{boardId}", boardId)
                        .session(otherSession))
                .andExpect(status().isForbidden());
    }

    @Test
    public void board_create_success_test() throws Exception {
        UserLoginRequestDto userLoginRequestDto = signup();
        MockHttpSession session = login(userLoginRequestDto);
        // Given 데이터 세팅
        BoardRequestDto boardRequestDto = new BoardRequestDto();
        boardRequestDto.setTitle("게시물 제목11");
        boardRequestDto.setContent("게시물 내용1223");

        String requestBody = om.writeValueAsString(boardRequestDto);
        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/boards/posts")
                .content(requestBody)
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();


        // Then 결과 검증 - 상태코드 확인 , 게시판ID확인
        resultActions.andExpect(status().isCreated())
                .andDo(print());;
    }
}
