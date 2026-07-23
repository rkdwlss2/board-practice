package com.example.boardpractice.service;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.Board.*;
import com.example.boardpractice.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
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

    public BoardDetailResponseDto getPost(Long boardId,String currentUserNickname){
        BoardDetailDto boardDetailDto = boardRepository.findByIdWithCounts(boardId).orElseThrow(() -> new IllegalArgumentException("게시글 찾지 못했습니다."));
        BoardDetailResponseDto boardDetailResponseDto = BoardDetailResponseDto.builder()
                .boardId(boardId)
                .title(boardDetailDto.getTitle())
                .writer(boardDetailDto.getWriter())
                .likeCount(boardDetailDto.getLikeCount())
                .commentCount(boardDetailDto.getCommentCount())
                .viewCount(boardDetailDto.getViewCount())
                .boardImageUrl(boardDetailDto.getBoardImageUrl())
                .content(boardDetailDto.getContent())
                .createDate(boardDetailDto.getCreateDate())
                .updatedDate(boardDetailDto.getUpdatedDate())
                .deleteDate(boardDetailDto.getDeleteDate())
                .build();
        boardDetailResponseDto.createIsOnwer(currentUserNickname);
        return boardDetailResponseDto;
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
    public BoardUpdateResponseDto updatePost(Long boardId, String title, String content, Long userId) {
        Boards board = findBoardById(boardId);

        if (!board.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글만 업데이트 할수 있습니다.");
        }

        board.changeTitle(title);
        board.changeContent(content);
        return BoardUpdateResponseDto.builder()
                .boardId(board.getBoardId())
                .build();
    }

    @Transactional
    public void deletePost(Long boardId,Long userId) {
        Boards board = findBoardById(boardId);
        if (!board.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글만 삭제 할수 있습니다.");
        }
        boardRepository.deleteById(board.getBoardId());
    }

    public Page<BoardSearchResponseDto> searchPostsByKeyword(String keyword,Pageable pageable){
        return boardRepository.findByContent(keyword,pageable);
    }
}
