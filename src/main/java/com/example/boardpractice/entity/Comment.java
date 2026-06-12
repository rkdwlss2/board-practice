package com.example.boardpractice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Comment {
    private Long commentId;
    private Long boardId;
    private String content;

    private String writer;

    private String writeDate;
    private String createDate;
    private String updatedDate;
}
