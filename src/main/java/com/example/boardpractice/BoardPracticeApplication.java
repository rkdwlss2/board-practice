package com.example.boardpractice;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.File;

@EnableJpaAuditing
@SpringBootApplication
public class BoardPracticeApplication {

    @Value("${upload.directory}")
    private String uploadDirectory;

    public static void main(String[] args) {
        SpringApplication.run(BoardPracticeApplication.class, args);
    }

    @PostConstruct
    public void init() {
        File directory = new File(uploadDirectory);
        if (!directory.exists()) {
            directory.mkdirs(); // 디렉터리 생성
        }
    }

}