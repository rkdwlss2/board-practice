package com.example.boardpractice.entity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class UsersTest {

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Rollback(false)
    void idTest(){
        //Users users = new Users(null,"russellNickname","russell@gmail.com","1234","1234","2025-04-22 00:00:01","2025-04-22 00:00:01","2025-04-22 00:00:01","imageUrl.com/123123",UserRole.USER);
        Users users = Users.builder()
                .userId(null)
                .nickname("russell")
                .email("russell@gmail.com")
                .password("1234")
                .createDate("2025-04-22 00:00:01")
                .deleteDate("2025-04-22 00:00:01")
                .updatedDate("2025-04-22 00:00:01")
                .userImageUrl("imageUrl.com/123123")
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