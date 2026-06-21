package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.Users;
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

        String email = userRequestDto.getEmail();
        String nickname = userRequestDto.getNickname();
        String password = userRequestDto.getPassword();
        Users responseUsers = userService.registerUser(email,nickname,password);
        return new ResponseEntity<>(new UserResponseDto(responseUsers), HttpStatus.CREATED);
    }

    @PutMapping("/users/me/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,@PathVariable Long userId)
    {
        Users responseUsers = userService.updateUserNickname(userId,userUpdateRequestDto.getNickname());

        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
    }

    @DeleteMapping("/users/me/{userId}")
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto,@PathVariable Long userId){
        String  email = userDeleteRequestDto.getEmail();
        userService.deleteUser(userId,email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/users/me/{userId}/password")
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateRequestDto passwordUpdateRequestDto,@PathVariable Long userId)
    {
        String password = passwordUpdateRequestDto.getPassword();
        String confirmPassword = passwordUpdateRequestDto.getConfirmPassword();
        Users responseUsers = userService.updateUserPassword(userId,password,confirmPassword);

        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto){
        String email = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();
        Users responseUsers = userService.loginUser(email,password);
        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
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
