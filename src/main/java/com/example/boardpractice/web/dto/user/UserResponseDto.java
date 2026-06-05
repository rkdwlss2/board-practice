package com.example.boardpractice.web.dto.user;

import com.example.boardpractice.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto
{
    private Long id;
    public UserResponseDto(User user){
        this.id = user.getId();
    }
}
