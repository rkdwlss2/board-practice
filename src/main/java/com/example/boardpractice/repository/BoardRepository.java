package com.example.boardpractice.repository;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.web.dto.Board.BoardDetailDto;
import com.example.boardpractice.web.dto.Board.BoardListResponseDto;
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
}
