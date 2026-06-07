package com.example.boardpractice.service;

import com.example.boardpractice.entity.Comment;
import com.example.boardpractice.web.dto.comment.CommentResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LikeService {

    public void increaseLikes(Long boardId){
        // likeRepository.updateComment(boardId);
    }

    public void decreaseLikes(Long boardId){
        // likeRepository.updateComment(boardId);
    }

}
