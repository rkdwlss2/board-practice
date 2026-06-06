package com.example.boardpractice.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BaseUserDto {

    @NotBlank
    @Pattern(regexp = "^\\S{1,10}$")
   // @Size(min = 1,max = 10)
    private String nickname;

}
