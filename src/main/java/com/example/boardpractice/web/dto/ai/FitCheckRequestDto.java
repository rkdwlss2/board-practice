package com.example.boardpractice.web.dto.ai;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FitCheckRequestDto {
    @NotNull(message = "키를 입력해 주세요.")
    @Min(value = 100, message = "키는 100cm 이상이어야 합니다.")
    @Max(value = 230, message = "키는 230cm 이하여야 합니다.")
    private Integer height;

    @NotNull(message = "몸무게를 입력해 주세요.")
    @Min(value = 30, message = "몸무게는 30kg 이상이어야 합니다.")
    @Max(value = 200, message = "몸무게는 200kg 이하여야 합니다.")
    private Integer weight;

    private String usualTopSize;
    private String usualBottomSize;
    private String usualShoeSize;

    @NotBlank(message = "원하는 핏을 선택해 주세요.")
    private String preferredFit;
}
