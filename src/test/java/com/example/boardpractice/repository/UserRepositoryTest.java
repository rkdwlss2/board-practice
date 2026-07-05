package com.example.boardpractice.repository;


import com.example.boardpractice.entity.Users;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
public class UserRepositoryTest {
    @Autowired UserRepository userRepository;

    @Test
    @DisplayName("이메일로 회원 조회")
    void findByEmail_테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        userRepository.save(user);

        // when
        Optional<Users> result = userRepository.findByEmail("russell@test.com");

        //then
        assertThat(result.get().getEmail()).isEqualTo("russell@test.com");

    }

    @Test
    @DisplayName("이메일에 맞는 회원 존재하여 true를 반환하는 경우")
    void exsitsByEmail_성공테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        userRepository.save(user);

        //when
        boolean result = userRepository.existsByEmail("russell@test.com");

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("이메일이 맞는 유저 존재하지 않아 false를 반환하는 경우")
    void exsitsByEmail_실패테스트(){
        //given
        Users user = Users.builder()
                .email("russell@test.com")
                .password("Kka!12345")
                .nickname("russell")
                .build();
        userRepository.save(user);

        //when
        boolean result = userRepository.existsByEmail("ruswl@test.com");

        //then
        assertThat(result).isFalse();
    }


}
