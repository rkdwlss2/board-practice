package com.example.boardpractice.web.dto.comment;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDeleteRequestDto {
    private Long commentId;
}
