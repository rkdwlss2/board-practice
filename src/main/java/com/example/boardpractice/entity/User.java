package com.example.boardpractice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
    public User(Long id){
        this.id = id;
    }
    public User(String password,String confirmPassword){
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
