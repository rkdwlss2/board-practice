package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, length = 10)
    private String nickname;
    @Column(nullable=false)
    private String email;
    @Column(nullable = false, length = 20)
    private String password;
    @Transient
    private String confirmPassword;
    private String deleteDate;
    private String createDate;
    private String updatedDate;
    private String userImageUrl;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    public Users(Long userId){
        this.userId = userId;
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
