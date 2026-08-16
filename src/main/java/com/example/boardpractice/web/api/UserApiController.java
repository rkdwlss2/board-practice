package com.example.boardpractice.web.api;

import com.example.boardpractice.common.utill.LoginRequired;
import com.example.boardpractice.common.utill.LoginUser;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.service.FileService;
import com.example.boardpractice.service.UserService;
import com.example.boardpractice.web.dto.file.FileInfoDto;
import com.example.boardpractice.web.dto.user.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final FileService fileService;
    private final UserService userService;

    @PostMapping("/users/signup")
    public ResponseEntity<?> createUser(@RequestBody @Valid UserSignupRequestDto userRequestDto, HttpServletRequest request){

        String email = userRequestDto.getEmail();
        String nickname = userRequestDto.getNickname();
        String password = userRequestDto.getPassword();
        Users responseUsers = userService.registerUser(email,nickname,password);
        authenticateUser(responseUsers, request);
        return new ResponseEntity<>(new UserResponseDto(responseUsers), HttpStatus.CREATED);
    }

    @PutMapping("/users/me")
//    @LoginRequired
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,@AuthenticationPrincipal  SessionUser user)
    {
        Users responseUsers = userService.updateUserNickname(user.getUserId(),userUpdateRequestDto.getNickname());

        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
//    @LoginRequired
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto,@AuthenticationPrincipal SessionUser user){
        String  email = userDeleteRequestDto.getEmail();
        userService.deleteUser(user.getUserId(),email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/users/me/password")
//    @LoginRequired
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateRequestDto passwordUpdateRequestDto,@AuthenticationPrincipal SessionUser user)
    {
        String password = passwordUpdateRequestDto.getPassword();
        String confirmPassword = passwordUpdateRequestDto.getConfirmPassword();
        Users responseUsers = userService.updateUserPassword(user.getUserId(),password,confirmPassword);

        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
    }

//    @PostMapping("/users/login")
//    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto, HttpServletRequest request){
//        String email = userLoginRequestDto.getEmail();
//        String password = userLoginRequestDto.getPassword();
//
//        Users users = userService.loginUser(email,password);
//
//        SessionUser sessionUser = new SessionUser(users);
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(sessionUser,null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
//        SecurityContext context = SecurityContextHolder.createEmptyContext();
//        context.setAuthentication(authentication);
//
//        SecurityContextHolder.setContext(context);
//
//        HttpSession session = request.getSession(true);
//        System.out.println("로그인 시 세션 ID: " + session.getId());
//        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
//
//        return new ResponseEntity<>(new UserResponseDto(users),HttpStatus.OK);
//    }

    @GetMapping("/users/me")
//    @LoginRequired
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal SessionUser user) {
        Users responseUser = userService.getUserInfo(user.getUserId());
        return ResponseEntity.ok(new UserResponseDto(responseUser));
    }

    public record UserImageRequest(String imageUrl) {
    }

    @PostMapping(value = "/users/me/image")
    public ResponseEntity<?> addFile(
            @RequestBody UserImageRequest request, @AuthenticationPrincipal SessionUser user) throws FileUploadException {
        return uploadUserImage( user.getUserId(),request.imageUrl());
    }



    @PostMapping(value = "/users/me/{userId}/image")
    public ResponseEntity<?> addFileWithUserId(
            @PathVariable Long userId,
            @RequestBody UserImageRequest request,
            @AuthenticationPrincipal SessionUser user) throws FileUploadException {
        if (!user.getUserId().equals(userId)) {
            throw new AccessDeniedException("본인의 프로필 이미지만 수정할 수 있습니다.");
        }
        return uploadUserImage(user.getUserId(),request.imageUrl());
    }

    private ResponseEntity<?> uploadUserImage(Long userId,String imageUrl) throws FileUploadException {
//        FileInfoDto fileinfo = fileService.uploadFile(file);	//s3 스토리지 저장
        //Long success = fileService.insertFileInfo(fileinfo);	//데이터베이스에 파일 정보 저장
        userService.updateUserImage(userId,imageUrl);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/me/presigned-url")
    public ResponseEntity<?> getPresignedUrl(@RequestBody Map<String, String> request) {
        String filename = request.get("filename");
        String filteType = request.get("filetype");
        String presignedUrl = fileService.generatePresignedUrl(filename, filteType);
        return new ResponseEntity<>(Map.of("presignedUrl",presignedUrl), HttpStatus.OK);
    }

    private void authenticateUser(Users users, HttpServletRequest request) {
        SessionUser sessionUser = new SessionUser(users);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                sessionUser,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

}
