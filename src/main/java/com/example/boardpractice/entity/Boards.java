package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE boards SET delete_date = CURRENT_TIMESTAMP WHERE board_id = ?")
@SQLRestriction("delete_date IS NULL")
@Entity
public class Boards {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private String title;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;

    @Embedded
    private BaseTimeEntity baseTimeEntity;

    private String boardImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany
    @JoinColumn(name = "board")
    private List<Likes> likes = new ArrayList<>();

    // 연관관계 편의 메서드
    public void assignUser(Users user){
        this.user = user;
        if (!user.getBoards().contains(this)){
            user.addBoards(this);
        }
    }
}
