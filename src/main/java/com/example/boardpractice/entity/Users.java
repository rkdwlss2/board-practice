package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE users SET delete_date = CURRENT_TIMESTAMP WHERE user_id = ?")
@SQLRestriction("delete_date IS NULL")
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(nullable = false, length = 10,unique = true)
    private String nickname;
    @Column(nullable=false,unique = true)
    private String email;
    @Column(nullable = false, length = 100)
    private String password;
    @Transient
    private String confirmPassword;
    @Builder.Default
    @Embedded
    BaseTimeEntity baseTimeEntity = new BaseTimeEntity();

    @Column( length = 1000)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "user")
    private List<Boards> boards = new ArrayList<>();;

    @OneToMany(mappedBy = "user")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Comments> comments = new ArrayList<>();

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



    public void changePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void checkPassword(String loginPassword){
        if (!loginPassword.equals(this.password)){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }

    public void updateEmailUser(String email){
        this.email = email;
    }

    public void changeUserImageUrl(String imageUrl){
        this.profileImageUrl = imageUrl;
    }

    public void reactivate(String nickname, String encodedPassword) {
        this.nickname = nickname;
        this.password = encodedPassword;
        this.profileImageUrl = null;
        this.baseTimeEntity.clearDeleteDate();
    }
}
