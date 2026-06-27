package com.example.boardpractice.service;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.Board.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public Page<BoardResponseDto> getAllPosts(Pageable pageable){
        return boardRepository.findAllWithCounts(pageable);
    }

    public BoardDetailResponseDto getPost(Long boardId){
        return boardRepository.findByIdWithCounts(boardId).orElseThrow(() -> new IllegalArgumentException("게시글 찾지 못했습니다."));
    }

    public Boards findBoardById(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("게시글 찾지 못했습니다."));
    }

    @Transactional
    public BoardCreateResponseDto createPost(Long userId, String title, String content) {
        Users user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Boards requestBoard = Boards.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        Boards responseBoard =boardRepository.save(requestBoard);
        return  BoardCreateResponseDto.builder()
                .boardId(responseBoard.getBoardId())
                .build();
    }

    @Transactional
    public BoardUpdateResponseDto updatePost(Long boardId, String title, String content) {
        Boards board = findBoardById(boardId);
        board.changeTitle(title);
        board.changeContent(content);
        return BoardUpdateResponseDto.builder()
                .boardId(board.getBoardId())
                .build();
    }

    @Transactional
    public void deletePost(Long boardId) {
        Boards board = findBoardById(boardId);
        boardRepository.deleteById(board.getBoardId());
    }
}
