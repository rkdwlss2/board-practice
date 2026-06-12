package com.example.boardpractice.service;

import com.example.boardpractice.entity.User;
import com.example.boardpractice.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String,User> database = new HashMap<>();

    public User saveUser(User user){

        if (!database.containsKey(user.getEmail())){
            database.put(user.getEmail(),
                    User.builder()
                            .email(user.getPassword())
                            .build());
        }
        return new User(1L);
    }

    public User updateUserNickname(String nickname,String email){
        User user = database.get(email);
        if (user == null){
            throw new NotFoundException("사용자를 찾을수 없습니다.");
        }
        user.setNickname(nickname);
        return user;
    }

    public User updateUserPassword(User user){
        User userReq = database.get(user.getEmail());
        if (user==null){
            throw new NotFoundException("사용자를 찾을수 없습니다.");
        }
        user.setPassword(user.getConfirmPassword());
        return userReq;
    }

    public void deleteUser(String email){
        Map<String, String> database = new HashMap<>();
    }

    public User loginUser(User user){
        User userReq = database.get(user.getEmail());
        String storedPassword = userReq.getPassword();

        if (storedPassword == null || !storedPassword.equals(user.getPassword())) {
            throw new NotFoundException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return userReq;
    }
}
