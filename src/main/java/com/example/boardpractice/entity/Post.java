package com.example.boardpractice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Post {
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
