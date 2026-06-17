package com.example.boardpractice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class H2CheckRunner implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;
    public H2CheckRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public void run(String... args) {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM books", Integer.class);
        System.out.println("[H2 체크] books 행 개수 = " + count);
    }
}
