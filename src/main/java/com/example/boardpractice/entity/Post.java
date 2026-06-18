package com.example.boardpractice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
public class Post {
    @Id @GeneratedValue
    private Long boardId;
    private String title;
    private String content;
    private String writer;

    private Long likeCount;
    private Long commentCount;
    private Long viewCount;

    private String writeDate;
    private String createDate;
    private String updatedDate;

    private String boardImageUrl;
}
