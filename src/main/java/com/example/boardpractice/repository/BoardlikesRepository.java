package com.example.boardpractice.repository;

import com.example.boardpractice.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardlikesRepository extends JpaRepository<Likes, Long> {
    // 1. 특정 유저가 특정 게시글에 좋아요를 눌렀는지 확인 (중복 체크용)
    boolean existsByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);

    // 2. 좋아요 취소를 위해 엔티티 조회
    Optional<Likes> findByUser_UserIdAndBoard_BoardId(Long userId, Long boardId);
}