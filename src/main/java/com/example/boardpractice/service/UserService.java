package com.example.boardpractice.service;

import com.example.boardpractice.entity.Users;
import com.example.boardpractice.exception.NotFoundException;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static aQute.bnd.annotation.headers.Category.database;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Users registerUser(String email, String nickname, String password) {
        Users users = Users.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
        return userRepository.save(users);
    }

    public Users findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()-> new NotFoundException("사용자를 찾을 수 없습니다."));
    }

    @Transactional
    public Users updateUserNickname(Long userId,String nickname) {
        Users users = findById(userId);
        users.makeUserNickname(nickname);
        return users;
    }
    @Transactional
    public Users updateUserPassword(Long userId, String password, String confirmPassword) {
        Users users = findById(userId);
        users.checkPasswordConfirm(password, confirmPassword);
        return users;
    }
    @Transactional
    public void deleteUser(Long userId, String email){
        Users users = findById(userId);
        userRepository.delete(users);
    }

    public Users loginUser(String email, String loginPassword) {
        Users usersReq = userRepository.findByEmail(email).orElseThrow(()->new NotFoundException("이메일이 일치하지 않습니다.")); //만약 일치하면 영속성 컨텍스트에 비밀번호 존재

        usersReq.checkPassword(loginPassword); // 영속성 컨텍스트에 있는 비밀번호와 지금 로그인하려는 비밀번호를 체크한다.

        return usersReq;
    }
}
