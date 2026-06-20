package com.example.boardpractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
}
