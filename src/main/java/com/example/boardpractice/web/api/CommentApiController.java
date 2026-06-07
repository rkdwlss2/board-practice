package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.Comment;
import com.example.boardpractice.service.CommentService;
import com.example.boardpractice.web.dto.Board.CommentDeleteRequestDto;
import com.example.boardpractice.web.dto.Board.CommentRequestDto;
import com.example.boardpractice.web.dto.Board.CommentResponseDto;
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
        List<CommentResponseDto> commentResponseDtoList = commentService.getAllComments(boardId);
        return new ResponseEntity<>(commentResponseDtoList, HttpStatus.OK);
    }

    @PostMapping("/boards/posts/{boardId}/comment")
    public ResponseEntity<?> createComment(@PathVariable Long boardId,@RequestBody CommentRequestDto commentRequestDto){
        Comment comment = Comment.builder()
                .boardId(boardId)
                .content(commentRequestDto.getContent())
                .build();
        commentService.createComment(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/boards/posts/{boardId}/comment")
    public ResponseEntity<?> updateComment(@PathVariable Long boardId,@RequestBody CommentRequestDto commentRequestDto){
        Comment comment = Comment.builder()
                .boardId(boardId)
                .content(commentRequestDto.getContent())
                .build();
        commentService.updateComment(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/boards/posts/{boardId}/comment")
    public ResponseEntity<?> deleteComment(@PathVariable Long boardId,@RequestBody CommentDeleteRequestDto commentDeleteRequestDto){
        commentService.deleteComment(commentDeleteRequestDto.getCommentId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
