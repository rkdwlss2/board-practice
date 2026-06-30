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

    @PutMapping("/users/me")
//    @LoginRequired
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto,@AuthenticationPrincipal Users user)
    {
        Users responseUsers = userService.updateUserNickname(user.getUserId(),userUpdateRequestDto.getNickname());

        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
    }

    @DeleteMapping("/users/me")
//    @LoginRequired
    public ResponseEntity<?> deleteAccount(@RequestBody @Valid UserDeleteRequestDto userDeleteRequestDto,@AuthenticationPrincipal Users user){
        String  email = userDeleteRequestDto.getEmail();
        userService.deleteUser(user.getUserId(),email);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/users/me/password")
//    @LoginRequired
    public ResponseEntity<?> updatePassword(@RequestBody @Valid PasswordUpdateRequestDto passwordUpdateRequestDto,@AuthenticationPrincipal Users user)
    {
        String password = passwordUpdateRequestDto.getPassword();
        String confirmPassword = passwordUpdateRequestDto.getConfirmPassword();
        Users responseUsers = userService.updateUserPassword(user.getUserId(),password,confirmPassword);

        return new ResponseEntity<>(new UserResponseDto(responseUsers),HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<?> userLogin(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto, HttpServletRequest request){
        String email = userLoginRequestDto.getEmail();
        String password = userLoginRequestDto.getPassword();

        Users users = userService.loginUser(email,password);

        SessionUser sessionUser = new SessionUser(users);

        Authentication authentication = new UsernamePasswordAuthenticationToken(sessionUser,null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        HttpSession session = request.getSession(true);
        System.out.println("로그인 시 세션 ID: " + session.getId());
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);

//        Users responseUsers = userService.loginUser(email,password);
//
//        HttpSession session = request.getSession();
//        //System.out.println("로그인 시 세션 ID: " + session.getId());
//        session.setAttribute("loginUser",new SessionUser(responseUsers));

        return new ResponseEntity<>(new UserResponseDto(users),HttpStatus.OK);
    }

    @GetMapping("/user/me")
//    @LoginRequired
    public ResponseEntity<?> getUserInfo(@AuthenticationPrincipal Users user) {
        return ResponseEntity.ok(user);
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
