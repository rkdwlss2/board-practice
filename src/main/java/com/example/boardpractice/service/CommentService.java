package com.example.boardpractice.service;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.entity.Comments;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.CommentRepository;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.comment.CommentCreateResponseDto;
import com.example.boardpractice.web.dto.comment.CommentUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public List<CommentUpdateResponseDto> getAllComments(Long boardId){
        List<Comments> commentList = commentRepository.findAllByPostIdWithUser(boardId);

        List<CommentUpdateResponseDto> commentResponseDtoList = new ArrayList<>();
        for (Comments comments : commentList){
            CommentUpdateResponseDto.builder()
                    .commentId(comments.getCommentId())
                    .content(comments.getContent())
                    .writer(comments.getUser().getNickname())
                    .build();
        }
        return commentResponseDtoList;
    }

    public Boards findBoardById(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("게시글 찾지 못했습니다."));
    }

    public Users findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("해당하는 사용자가 없습니다."));
    }

    public Comments findCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(()->new IllegalArgumentException("해당하는 댓글 존재하지 않습니다."));
    }

    @Transactional
    public CommentCreateResponseDto createComment(Long boardId, Long userId, String content){
        Boards board = findBoardById(boardId);
        Users user = findUserById(userId);
        Comments requestComment = Comments.builder()
                .board(board)
                .content(content)
                .user(user)
                .build();
        Comments responseComment = commentRepository.save(requestComment);
        return CommentCreateResponseDto.builder()
                .commentId(responseComment.getCommentId())
                .build();
    }

    @Transactional
    public CommentUpdateResponseDto updateComment( Long commentId,  String content){
        Comments comment = findCommentById(commentId);
        comment.changeContent(content);
        return CommentUpdateResponseDto.builder()
                .commentId(commentId)
                .build();
    }

    @Transactional
    public void deleteComment(Long commentId){
        Comments comment = findCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

}
