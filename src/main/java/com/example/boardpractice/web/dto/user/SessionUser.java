package com.example.boardpractice.web.dto.user;

import com.example.boardpractice.entity.Users;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private Long userId;
    private String email;
    private String nickname;

    public SessionUser(Users user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
    }
}
