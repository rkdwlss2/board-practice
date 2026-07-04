package com.example.boardpractice.web;

import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.service.UserService;
import com.example.boardpractice.web.dto.user.UserSignupRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("회원가입시, 입력한 이메일에 해당하는 유저정보가 있어 true를 리턴해야 된다. 그리고 저장한 유저 정보를 반환해야한다.")
    public void 회원가입_서비스테스트() {
        // given
        UserSignupRequestDto dto = new UserSignupRequestDto();
        dto.setEmail("russell@gmail.com");
        dto.setPassword("Kka!12345");
        dto.setNickname("russell");

        Users user = Users.builder()
                .email("russell@gmail.com")
                .build();

        when(userRepository.existsByEmail(dto.getEmail()))
                .thenReturn(false);

        when(userRepository.save(any(Users.class)))
                .thenReturn(user);

        //when
        Users result = userService.registerUser("russell@gmail.com","russell","Kka!12345");

        //then
        assertEquals("russell@gmail.com",result.getEmail());

        verify(userRepository).existsByEmail(dto.getEmail());
        verify(userRepository).save(any(Users.class));
    }
}
