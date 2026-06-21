package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SQLDelete(sql = "UPDATE users SET delete_date = CURRENT_TIMESTAMP WHERE user_id = ?")
@SQLRestriction("delete_date IS NULL")
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

    @Embedded
    private BaseTimeEntity baseTimeEntity;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "imageUrl",column = @Column(name = "profile_image_url")),
            @AttributeOverride(name = "introduction",column = @Column(name = "profile_introduction"))
    })
    private FileInfo profileImageFile;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Boards> boards = new ArrayList<>();;

    @OneToMany(mappedBy = "user")
    private List<Likes> likes = new ArrayList<>();

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

    public void checkPasswordConfirm(String newPassword, String confirmPassword){
        if (!newPassword.equals(confirmPassword)){ // 비밀번호 일치 불일치 로직
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }
        this.password=newPassword;
    }

    public void checkPassword(String loginPassword){
        if (!loginPassword.equals(this.password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updateEmailUser(String email){
        this.email = email;
    }
}
