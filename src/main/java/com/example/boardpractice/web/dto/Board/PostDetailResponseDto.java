package com.example.boardpractice.web.dto.Board;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class PostDetailResponseDto {
    private Long boardId;
    private String title;
    private String writer;
    private Long commentCount;
    private String boardImageUrl;
    private Long viewCount;
    private String writeDate;
    private String createDate;
    private String updatedDate;
}
