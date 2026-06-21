package com.example.boardpractice.web.dto.comment;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateResponseDto {
    private Long commentId;
    private String writer;
    private String content;
    private String writeDate;
    private String createDate;
    private String updatedDate;
}
