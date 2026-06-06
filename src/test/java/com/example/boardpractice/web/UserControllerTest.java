package com.example.boardpractice.web;

import com.example.boardpractice.web.dto.user.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    public void create_success_test() throws Exception {
        // Given 데이터 세팅
       UserSignupRequestDto userRequestDto = new UserSignupRequestDto();
        userRequestDto.setEmail("rkdwlss2@google.com");
        userRequestDto.setPassword("Kang!1234");
        userRequestDto.setNickname("kang");

        String requestBody = om.writeValueAsString(userRequestDto);
        System.out.println("requestBody = "+requestBody);
        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/users/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                        .getResponse()
                        .getContentAsString();
        System.out.println("responseBody = "+responseBody);
        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isCreated());
    }

    @Test
    public void email_success_test() throws Exception {
        // Given 데이터 세팅
       UserSignupRequestDto userRequestDto = new UserSignupRequestDto();
        userRequestDto.setEmail("kang@google.com");
        userRequestDto.setPassword("Kang!1234");
        userRequestDto.setNickname("kang");

        String requestBody = om.writeValueAsString(userRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/users/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);
        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isCreated());
    }

    // email 유효성 테스트
    @Test
    public void email_fail_test() throws Exception {
        // Given 데이터 세팅
       UserSignupRequestDto userRequestDto = new UserSignupRequestDto();
        userRequestDto.setEmail("@google.com");
        userRequestDto.setPassword("Kang!1234");
        userRequestDto.setNickname("kang");

        String requestBody = om.writeValueAsString(userRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/users/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);
        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }

    // 패스워드 실패 테스트
    @Test
    public void password_fail_test() throws Exception {
        // Given 데이터 세팅
       UserSignupRequestDto userRequestDto = new UserSignupRequestDto();
        userRequestDto.setEmail("kang2@google.com");
        userRequestDto.setPassword("ang!1234");
        userRequestDto.setNickname("kang");

        String requestBody = om.writeValueAsString(userRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/users/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }

    // 패스워드 특수문자 제거 테스트
    @Test
    public void password_Symbols_fail_test() throws Exception {
        // Given 데이터 세팅
       UserSignupRequestDto userRequestDto = new UserSignupRequestDto();
        userRequestDto.setEmail("kang2@google.com");
        userRequestDto.setPassword("Kang1234");
        userRequestDto.setNickname("kang");

        String requestBody = om.writeValueAsString(userRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/users/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }

    // 닉네임 공백 테스트
    @Test
    public void update_nickname_blank_fail_test() throws Exception {
        // Given 데이터 세팅
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setNickname("");

        String requestBody = om.writeValueAsString(userUpdateRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(put("/users/me")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }

    // 닉네임 띄어쓰기 테스트
    @Test
    public void update_nickname_space_fail_test() throws Exception {
        // Given 데이터 세팅
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setNickname("e e");

        String requestBody = om.writeValueAsString(userUpdateRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(put("/users/me")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }

    // 닉네임 1~10 자리 테스트
    @Test
    public void update_nickname_size_fail_test() throws Exception {
        // Given 데이터 세팅
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setNickname("12345678910");

        String requestBody = om.writeValueAsString(userUpdateRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(put("/users/me")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }


    // 회원 탈퇴 테스트
    @Test
    public void delete_user_success_test() throws Exception {
        // Given 데이터 세팅
        UserDeleteRequestDto userDeleteRequestDto = new UserDeleteRequestDto();
        userDeleteRequestDto.setEmail("russell@gmail.com");

        String requestBody = om.writeValueAsString(userDeleteRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(delete("/users/me")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isOk());
    }


    // 회원 탈퇴 이메일 오류 테스트
    @Test
    public void delete_user_faill_test() throws Exception {
        // Given 데이터 세팅
        UserDeleteRequestDto userDeleteRequestDto = new UserDeleteRequestDto();
        userDeleteRequestDto.setEmail("@gmail.com");

        String requestBody = om.writeValueAsString(userDeleteRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(delete("/users/me")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isUnprocessableContent());
    }

    // 유저 로그인 테스트
    @Test
    public void login_success_test() throws Exception {
        // Given 데이터 세팅
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setEmail("russell@gmail.com");
        userLoginRequestDto.setPassword("Asdf!12345");

        String requestBody = om.writeValueAsString(userLoginRequestDto);
        System.out.println("requestBody = "+requestBody);

        // When 테스트 동작 수행 - API 호출
        ResultActions resultActions = mvc.perform(post("/user/login")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
        );
        String responseBody = resultActions.andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println("responseBody = "+responseBody);

        // Then 결과 검증 - 상태코드 확인
        resultActions.andExpect(status().isOk())
                .andDo(print());
    }

}
