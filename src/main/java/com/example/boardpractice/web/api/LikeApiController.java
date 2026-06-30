package com.example.boardpractice.web.api;

import com.example.boardpractice.common.utill.LoginRequired;
import com.example.boardpractice.common.utill.LoginUser;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.service.BoardlikeService;
import com.example.boardpractice.web.dto.like.LikeCreateRequestDto;
import com.example.boardpractice.web.dto.like.LikeDeleteRequestDto;
import com.example.boardpractice.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class LikeApiController {

    private final BoardlikeService boardlikeService;

    @PostMapping("/boards/likes/{boardId}")
//    @LoginRequired
    public ResponseEntity<?> increaseLikes(@PathVariable Long boardId, @AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        boardlikeService.increaseLikes(boardId,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/boards/likes/{boardId}")
//    @LoginRequired
    public ResponseEntity<?> decreaseLikes(@PathVariable Long boardId, @AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        boardlikeService.decreaseLikes(boardId,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
