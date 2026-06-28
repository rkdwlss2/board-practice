package com.example.boardpractice.web.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {
    private String content;
    private String title;
}
