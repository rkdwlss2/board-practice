package com.example.boardpractice.service;

import com.example.boardpractice.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    public User saveUser(User user){
        Map<String,String> database = new HashMap<>();

        if (!database.containsKey(user.getEmail())){
            database.put(user.getEmail(),user.getPassword());
        }
        return new User(1L);
    }

    public User updateUser(String nickname){
        Map<String,String> database = new HashMap<>();
        if (!database.containsKey(nickname)){

        }
        return new User(1L);
    }
}
