package com.example.boardpractice.web.api;

import com.example.boardpractice.service.CommentService;
import com.example.boardpractice.web.dto.comment.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentApiController {

    private final CommentService commentService;

    @GetMapping("/boards/posts/{boardId}/comment")
    public ResponseEntity<?> getComments(@PathVariable Long boardId){
        List<CommentUpdateResponseDto> commentResponseDtoList = commentService.getAllComments(boardId);
        return new ResponseEntity<>(commentResponseDtoList, HttpStatus.OK);
    }

    @PostMapping("/boards/posts/{boardId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long boardId,@RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto){
        String content = commentCreateRequestDto.getContent();
        Long userId = commentCreateRequestDto.getUserId();
        CommentCreateResponseDto commentCreateResponseDto =commentService.createComment(boardId,userId,content);
        return new ResponseEntity<>(commentCreateResponseDto,HttpStatus.OK);
    }

    @PutMapping("/boards/posts/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,@RequestBody CommentUpdateRequestDto commentUpdateRequestDto){
        String content = commentUpdateRequestDto.getContent();
        CommentUpdateResponseDto commentUpdateResponseDto = commentService.updateComment(commentId,content);
        return new ResponseEntity<>(commentUpdateResponseDto,HttpStatus.OK);
    }

    @DeleteMapping("/boards/posts/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,@RequestBody CommentDeleteRequestDto commentDeleteRequestDto){
        commentService.deleteComment(commentDeleteRequestDto.getCommentId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
