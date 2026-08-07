package com.example.boardpractice.web.dto.ai;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FitCheckResponseDto {
    private String result;
    private String label;
    private String sizeAdvice;
    private String boardImageUrl;
    private List<String> reasons;
}
