package com.example.boardpractice.service;

import com.example.boardpractice.entity.Comments;
import com.example.boardpractice.web.dto.comment.CommentResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {
    public List<CommentResponseDto> getAllComments(Long boardId){
        List<CommentResponseDto> commentList = new ArrayList<>();
        commentList.add(
                CommentResponseDto.builder()
                        .commentId(2026L)
                        .writer("더미작성자1")
                        .content("댓글 내용1")
                        .writeDate("2014-01-02 00:01:00")
                        .createDate("2014-01-02 00:01:00")
                        .updatedDate("2014-01-02 00:01:00")
                        .build()
        );
        commentList.add(
                CommentResponseDto.builder()
                        .commentId(2025L)
                        .writer("더미작성자2")
                        .content("댓글 내용2")
                        .writeDate("2014-01-02 00:01:00")
                        .createDate("2014-01-02 00:01:00")
                        .updatedDate("2014-01-02 00:01:00")
                        .build()
        );

        return commentList;
    }

    public void createComment(Comments comment){
        // commentRepository.createComment(comment);
    }

    public void updateComment(Comments comment){
        // commentRepository.updateComment(comment);
    }

    public void deleteComment(Long commentId,Long boardId){
        // commentRepository.deleteComment(commentId,boardId);
    }

}
