package com.example.boardpractice.service;

import com.example.boardpractice.entity.Users;
import com.example.boardpractice.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final Map<String, Users> database = new HashMap<>();

    public Users saveUser(Users users){

        if (!database.containsKey(users.getEmail())){
            database.put(users.getEmail(),
                    Users.builder()
                            .email(users.getPassword())
                            .build());
        }
        return new Users(1L);
    }

    public Users updateUserNickname(String nickname, String email){
        Users users = database.get(email);
        if (users == null){
            throw new NotFoundException("사용자를 찾을수 없습니다.");
        }
        users.makeUserNickname(nickname);
        return users;
    }

    public Users updateUserPassword(Users users){
        Users usersReq = database.get(users.getEmail());
        if (users ==null){
            throw new NotFoundException("사용자를 찾을수 없습니다.");
        }

        if (!users.getPassword().equals(users.getConfirmPassword())){
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        users.checkPasswordConfirm(users.getConfirmPassword());
        return usersReq;
    }

    public void deleteUser(String email){
        Map<String, String> database = new HashMap<>();
    }

    public Users loginUser(Users users){
        Users usersReq = database.get(users.getEmail());
        String storedPassword = usersReq.getPassword();

        if (storedPassword == null || !storedPassword.equals(users.getPassword())) {
            throw new NotFoundException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        return usersReq;
    }
}
