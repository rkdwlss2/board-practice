package com.example.boardpractice.web.dto.Board;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

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
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
    private LocalDateTime deleteDate;
    private Boolean isOwner;

    public void createIsOnwer(String currentUserNickname){
        this.isOwner = Objects.equals(this.writer, currentUserNickname);
    }
}
