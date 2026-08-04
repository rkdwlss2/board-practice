package com.example.boardpractice.repository;

import com.example.boardpractice.entity.BoardIndexFailure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardIndexFailureRepository extends JpaRepository<BoardIndexFailure, Long> {
}
