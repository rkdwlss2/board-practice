package com.example.boardpractice.web.dto.user;

import com.example.boardpractice.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto
{
    private Long id;
    private String email;
    private String nickname;
    public UserResponseDto(User user){
        this.id = user.getUserId();
        this.email= user.getEmail();
        this.nickname = user.getNickname();
    }
}
