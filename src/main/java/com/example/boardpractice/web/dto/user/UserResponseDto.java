package com.example.boardpractice.web.dto.user;

import com.example.boardpractice.entity.Users;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto
{
    private Long id;
    private String email;
    private String nickname;
    public UserResponseDto(Users users){
        this.id = users.getUserId();
        this.email= users.getEmail();
        this.nickname = users.getNickname();
    }
}
