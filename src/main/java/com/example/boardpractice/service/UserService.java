package com.example.boardpractice.service;

import com.example.boardpractice.entity.User;
import com.example.boardpractice.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String,String> database = new HashMap<>();

    public User saveUser(User user){

        if (!database.containsKey(user.getEmail())){
            database.put(user.getEmail(),user.getPassword());
        }
        return new User(1L);
    }

    public User updateUserNickname(String nickname){
        if (!database.containsKey(nickname)){

        }
        return new User(1L);
    }

    public User updateUserPassword(User user){

        return new User(1L);
    }

    public void deleteUser(String email){
        Map<String, String> database = new HashMap<>();
    }

    public User loginUser(User user){
        String storedPassword = database.get(user.getEmail());

        if (storedPassword == null || !storedPassword.equals(user.getPassword())) {
            throw new NotFoundException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return new User(1L);
    }
}
