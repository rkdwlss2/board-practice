package com.example.boardpractice.repository;

import com.example.boardpractice.entity.Boards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Boards,Long> {
}
