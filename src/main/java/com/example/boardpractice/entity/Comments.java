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
public class Comments {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private Long boardId;
    private Long userId;
    private String content;

    @Embedded
    private BaseTimeEntity baseTimeEntity;
}
