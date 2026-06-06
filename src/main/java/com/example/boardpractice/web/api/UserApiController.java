package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.User;
import com.example.boardpractice.service.UserService;
import com.example.boardpractice.web.dto.user.BaseUserDto;
import com.example.boardpractice.web.dto.user.UserResponseDto;
import com.example.boardpractice.web.dto.user.UserSignupRequestDto;
import com.example.boardpractice.web.dto.user.UserUpdateRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserSignupRequestDto userRequestDto){
        User user = new User(
                userRequestDto.getEmail(),
                userRequestDto.getNickname(),
                userRequestDto.getPassword()
        );
        User responseUser = userService.saveUser(user);
        return new ResponseEntity<>(new UserResponseDto(responseUser), HttpStatus.CREATED);
    }

    @PutMapping("/users/me")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto)
    {
        User responseUser = userService.updateUser(userUpdateRequestDto.getNickname());

        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

}
