package com.example.boardpractice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {

    private Long id;
    private String email;
    private String password;
    private String nickname;
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }
    public User(Long id){
        this.id = id;
    }
}
