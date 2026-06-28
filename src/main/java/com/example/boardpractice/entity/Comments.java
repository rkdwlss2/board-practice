package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE comments SET delete_date = CURRENT_TIMESTAMP WHERE comment_id = ?")
@SQLRestriction("delete_date IS NULL")
@Entity
public class Comments {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Boards board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @Builder.Default
    @Embedded
    BaseTimeEntity baseTimeEntity = new BaseTimeEntity();

    public void changeContent(String content) {
        if (content != null) {
            this.content = content;
        }
    }
}
