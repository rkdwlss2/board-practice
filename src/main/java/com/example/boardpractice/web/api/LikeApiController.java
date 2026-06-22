package com.example.boardpractice.web.api;

import com.example.boardpractice.service.BoardlikeService;
import com.example.boardpractice.web.dto.like.LikeCreateRequestDto;
import com.example.boardpractice.web.dto.like.LikeDeleteRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final BoardlikeService boardlikeService;

    @PostMapping("/boards/likes/{boardId}")
    public ResponseEntity<?> increaseLikes(@PathVariable Long boardId,@RequestBody LikeCreateRequestDto likeCreateRequestDto) {
        Long userId = likeCreateRequestDto.getUserId();
        boardlikeService.increaseLikes(boardId,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/boards/likes/{boardId}")
    public ResponseEntity<?> decreaseLikes(@PathVariable Long boardId,@RequestBody LikeDeleteRequestDto likeDeleteRequestDto) {
        Long userId = likeDeleteRequestDto.getUserId();
        boardlikeService.decreaseLikes(boardId,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
