package com.example.boardpractice.web;

import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입시, 입력한 이메일에 해당하는 유저정보가 있어 true를 리턴해야 된다. 그리고 저장한 유저 정보를 반환해야한다.")
    public void signupUser() {
        // given


    }
}
