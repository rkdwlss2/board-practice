package com.example.boardpractice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@RequiredArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Entity
@Table(name = "likes", uniqueConstraints = {
        @UniqueConstraint(
                name = "likes_unique",
                columnNames = {"user_id", "board_id"}
        )
})
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id",nullable = false)
    private Boards board;

    @Builder.Default
    @Embedded
    BaseTimeEntity baseTimeEntity = new BaseTimeEntity();
}
