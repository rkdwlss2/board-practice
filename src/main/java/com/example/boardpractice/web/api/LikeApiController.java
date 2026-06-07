package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.Post;
import com.example.boardpractice.service.LikeService;
import com.example.boardpractice.web.dto.Board.PostRequestDto;
import lombok.Delegate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/boards/likes/{boardId}")
    public ResponseEntity<?> increaseLikes(@PathVariable Long boardId){
        likeService.increaseLikes(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/boards/likes/{boardId}")
    public ResponseEntity<?> updateDetailPost(@PathVariable Long boardId){
        likeService.increaseLikes(boardId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
