package com.example.boardpractice.web.dto.Board;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
}
