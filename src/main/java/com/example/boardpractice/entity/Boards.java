package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
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
    @Builder.Default
    @Column(nullable = false)
    private Long viewCount = 0L;

    @Builder.Default
    @Embedded
    BaseTimeEntity baseTimeEntity = new BaseTimeEntity();

    private String boardImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToMany(mappedBy = "board")
//    @JoinColumn(name = "board_id")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Comments> comments = new ArrayList<>();

    // 연관관계 편의 메서드
    public void assignUser(Users user){
        this.user = user;
        if (!user.getBoards().contains(this)){
            user.addBoards(this);
        }
    }

    //
    public void changeTitle(String title){
        if (title != null) {
            this.title = title;
        }
    }

    public void changeContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }
}
