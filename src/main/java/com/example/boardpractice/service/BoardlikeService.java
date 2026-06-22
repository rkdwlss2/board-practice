package com.example.boardpractice.service;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.entity.Likes;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.BoardlikesRepository;
import com.example.boardpractice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardlikeService {

    private final BoardlikesRepository boardlikesRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void increaseLikes(Long boardId, Long userId) {
        if (boardlikesRepository.existsByUser_UserIdAndBoard_BoardId(userId, boardId)) {
            throw new IllegalArgumentException("이미 좋아요 누른 게시글입니다.");
        }

        Boards board = boardRepository.findById(boardId).orElseThrow();
        Users user = userRepository.findById(userId).orElseThrow();

        Likes boardLike = Likes.builder()
                .board(board)
                .user(user)
                .build();

        boardlikesRepository.save(boardLike);
    }

    @Transactional
    public void decreaseLikes(Long boardId, Long userId) {
        // 1. 조회 후 삭제
        Likes like = boardlikesRepository.findByUser_UserIdAndBoard_BoardId(userId, boardId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누르지 않았습니다."));

        boardlikesRepository.delete(like);
    }

}
