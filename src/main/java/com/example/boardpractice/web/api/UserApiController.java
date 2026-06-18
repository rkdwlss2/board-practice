package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.User;
import com.example.boardpractice.service.FileService;
import com.example.boardpractice.service.UserService;
import com.example.boardpractice.web.dto.file.FileInfoDto;
import com.example.boardpractice.web.dto.user.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final FileService fileService;
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
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto, HttpSession session)
    {
        String email = (String) session.getAttribute("LOGIN_USER");
        User responseUser = userService.updateUserNickname(userUpdateRequestDto.getNickname(),email);

        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto){
        userService.deleteUser(userDeleteRequestDto.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/users/me/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateRequestDto passwordUpdateRequestDto, HttpSession session)
    {
        User user = User.builder()
                .password(passwordUpdateRequestDto.getPassword())
                .confirmPassword(passwordUpdateRequestDto.getConfirmPassword())
                .build();
        String email = (String) session.getAttribute("LOGIN_USER");
        user.updateEmailUser(email);
        User responseUser = userService.updateUserPassword(user);

        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto){
        User user = User.builder()
                .email(userLoginRequestDto.getEmail())
                .password(userLoginRequestDto.getPassword())
                .build();
        User responseUser = userService.loginUser(user);
        return new ResponseEntity<>(new UserResponseDto(responseUser),HttpStatus.OK);
    }

    @PostMapping(value = "/users/me/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFile(
            @RequestPart("multipartFile")
            MultipartFile file) throws FileUploadException {
        FileInfoDto fileinfo = fileService.uploadFile(file);	//서버 내부 스토리지 저장
        //Long success = fileService.insertFileInfo(fileinfo);	//데이터베이스에 파일 정보 저장

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
