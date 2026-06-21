package com.example.boardpractice.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class UsersTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Rollback(false)
    void idTest(){
        Users users = Users.builder()
                .userId(null)
                .nickname("russell")
                .email("russell@gmail.com")
                .password("1234")
                .baseTimeEntity(new BaseTimeEntity(LocalDateTime.MAX,LocalDateTime.MAX,LocalDateTime.MAX))
                .profileImageFile(new FileInfo("imagenUL","test"))
                .build();
        entityManager.persist(users);
        entityManager.flush();
    }

    @Test
    @Rollback(false)
    void persistUserTest(){
        //Users user = new Users("tester@adapterz.kr", "123aS!", "Adapterz");
        Users user = Users.builder()
                .email("tester@adapterz.kr")
                .password("1234as")
                .nickname("russell")
                .build();
        entityManager.persist(user);
    }
}