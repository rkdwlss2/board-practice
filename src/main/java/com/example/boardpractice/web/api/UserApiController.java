package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.User;
import com.example.boardpractice.service.UserService;
import com.example.boardpractice.web.dto.user.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserSignupRequestDto userRequestDto){
        User user = User.builder()
                .email(userRequestDto.getEmail())
                .nickname(userRequestDto.getNickname())
                .password(userRequestDto.getPassword())
                .build();
        User responseUser = userService.saveUser(user);
        return new ResponseEntity<>(new UserResponseDto(responseUser), HttpStatus.CREATED);
    }

    @PutMapping("/users/me")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto)
    {
        User responseUser = userService.updateUserNickname(userUpdateRequestDto.getNickname());

        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto){
        userService.deleteUser(userDeleteRequestDto.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/users/me/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateRequestDto passwordUpdateRequestDto)
    {
        User user = User.builder()
                .password(passwordUpdateRequestDto.getPassword())
                .confirmPassword(passwordUpdateRequestDto.getConfirmPassword())
                .build();
        User responseUser = userService.updateUserPassword(user);

        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto){
        User user = User.builder()
                .email(userLoginRequestDto.getEmail())
                .password(userLoginRequestDto.getPassword())
                .build();
        User responseUser = userService.loginUser(user);
        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

}
