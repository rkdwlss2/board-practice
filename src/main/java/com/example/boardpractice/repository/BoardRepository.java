package com.example.boardpractice.repository;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.web.dto.Board.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Boards,Long> {

    @Query("SELECT new com.example.boardpractice.web.dto.Board.BoardListResponseDto(b.boardId,b.title,u.nickname,count(DISTINCT l),count(DISTINCT c),b.viewCount,u.profileImageUrl,b.baseTimeEntity.createDate,b.baseTimeEntity.updatedDate,b.baseTimeEntity.deleteDate) " +
           "FROM Boards b "+
           "LEFT JOIN b.likes l "+
           "LEFT JOIN b.user u " +
           "LEFT JOIN b.comments c "+
           "GROUP BY b.boardId"
    )
    Page<BoardListResponseDto> findAllWithCounts(Pageable pageable);

    @Query("SELECT new com.example.boardpractice.web.dto.Board.BoardDetailDto(b.boardId,b.title,u.nickname,count(DISTINCT l),count(DISTINCT c),b.viewCount,b.boardImageUrl,u.profileImageUrl,b.content,b.baseTimeEntity.createDate,b.baseTimeEntity.updatedDate,b.baseTimeEntity.deleteDate) "+
           "FROM Boards b "+
           "LEFT JOIN b.likes l "+
           "LEFT JOIN b.user u " +
           "LEFT JOIN b.comments c "+
           "where b.boardId = :boardId"
    )
    Optional<BoardDetailDto> findByIdWithCounts(@Param("boardId")Long boardId);
    @Query(value =
            "SELECT b.board_id, b.title, u.nickname, " +
                    "       COUNT(DISTINCT l.like_id) AS likeCount, " +
                    "       COUNT(DISTINCT c.comment_id) AS commentCount, " +
                    "       b.board_image_url, b.view_count, b.create_date, b.updated_date, b.delete_date " +
                    "FROM boards b " +
                    "LEFT JOIN users u ON u.user_id = b.user_id AND u.delete_date IS NULL " +
                    "LEFT JOIN likes l ON l.board_id = b.board_id " +
                    "LEFT JOIN comments c ON c.board_board_id = b.board_id AND c.delete_date IS NULL " +
                    "WHERE b.delete_date IS NULL " +
                    "  AND MATCH(b.content) AGAINST(:keyword IN BOOLEAN MODE) " + // 올바른 MySQL 문법
                    "GROUP BY b.board_id, b.title, u.nickname, b.view_count, b.create_date, b.updated_date, b.delete_date",
            nativeQuery = true)
    Page<BoardSearchResponseDto> findByContent(String keyword, Pageable pageable);
}
