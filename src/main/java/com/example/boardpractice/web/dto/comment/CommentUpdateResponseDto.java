package com.example.boardpractice.web.dto.comment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateResponseDto {
    private Long commentId;
    private String writer;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
