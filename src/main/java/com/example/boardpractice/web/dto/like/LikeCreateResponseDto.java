package com.example.boardpractice.web.dto.like;

import com.example.boardpractice.entity.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LikeCreateResponseDto {
    Long likeId;
}
