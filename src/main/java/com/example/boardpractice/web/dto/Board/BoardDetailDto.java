package com.example.boardpractice.web.dto.Board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDetailDto {
    private Long boardId;
    private String title;
    private String writer;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;
    private String boardImageUrl;
    private String content;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deleteDate;

}
