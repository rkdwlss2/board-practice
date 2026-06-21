package com.example.boardpractice.web.dto.Board;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailResponseDto {
    private Long boardId;
    private String title;
    private String writer;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;
    private String boardImageUrl;
    private String content;
    private LocalDateTime writeDate;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
