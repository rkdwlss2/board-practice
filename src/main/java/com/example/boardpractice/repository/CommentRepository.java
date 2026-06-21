package com.example.boardpractice.repository;

import com.example.boardpractice.entity.Comments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comments,Long> {

    @Query("select c from Comments c join fetch c.user where c.board.boardId = :boardId")
    List<Comments> findAllByPostIdWithUser(@Param("boardId") Long boardId);
}
