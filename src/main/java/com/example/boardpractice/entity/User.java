package com.example.boardpractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "\"user\"")
@Entity
public class User {
    @Id @GeneratedValue
    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String nickname;

    public User(Long id){
        this.id = id;
    }

    public void makeUserNickname(String nickname){
        this.nickname = nickname;
    }

    public void checkPasswordConfirm(String confirmPassword){
        this.confirmPassword= confirmPassword;
    }

    public void updateEmailUser(String email){
        this.email = email;
    }
}
