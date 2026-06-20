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
public class Comment {
    @Id @GeneratedValue
    private Long commentId;
    private Long boardId;
    private String content;


    private String writeDate;
    private String createDate;
    private String updatedDate;
}
