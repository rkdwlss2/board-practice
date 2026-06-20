package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Boards {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;
    private Long userId;
    private String title;
    private String content;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;
    private String createDate;
    private String updatedDate;
    private String deleteDate;
    private String boardImageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    // 연관관계 편의 메서드
    public void assignUser(Users user){
        this.user = user;
        if (!user.getBoards().contains(this)){
            user.addBoards(this);
        }
    }
}
