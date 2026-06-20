package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @OneToMany(mappedBy = "user")
    private List<Boards> boards = new ArrayList<>();;

    public void addBoards(Boards board) {
        this.boards.add(board); // User → board 컬렉션 추가
        if (board.getUser() !=this){
            board.assignUser(this);   // board → User 참조 설정
        }
    }

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
