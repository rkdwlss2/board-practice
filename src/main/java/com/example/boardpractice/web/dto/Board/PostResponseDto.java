package com.example.boardpractice.web.dto.Board;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {
    private Long boardId;
    private String title;
    private String writer;
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;
    private String writeDate;
    private String createDate;
    private String updatedDate;
}
