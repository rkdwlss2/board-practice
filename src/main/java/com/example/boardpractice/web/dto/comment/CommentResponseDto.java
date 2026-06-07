package com.example.boardpractice.web.dto.comment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long commentId;
    private String writer;
    private String content;
    private String writeDate;
    private String createDate;
    private String updatedDate;
}
