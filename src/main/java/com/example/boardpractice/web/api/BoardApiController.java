package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.Post;
import com.example.boardpractice.service.BoardService;
import com.example.boardpractice.web.dto.Board.PostDetailResponseDto;
import com.example.boardpractice.web.dto.Board.PostResponseDto;
import com.example.boardpractice.web.dto.Board.PostRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final BoardService boardService;

    @GetMapping("/boards/posts")
    public ResponseEntity<?> getPosts(@RequestParam int page, @RequestParam int size){
        List<PostResponseDto> postResponseDtoList= boardService.getAllPosts(page,size);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> getDetailPost(@PathVariable Long boardId){
        PostDetailResponseDto postDetailResponseDto = boardService.getPost(boardId);
        return new ResponseEntity<>(postDetailResponseDto,HttpStatus.OK);
    }

    @PutMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> updateDetailPost(@PathVariable Long boardId, @RequestBody PostRequestDto postRequestDto){
        Post post = Post.builder()
                .boardId(boardId)
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();

        boardService.updatePost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/boards/posts")
    public ResponseEntity<?> createDetailPost(@RequestBody PostRequestDto postRequestDto){
        Post post = Post.builder()
                .title(postRequestDto.getTitle())
                .content(postRequestDto.getContent())
                .build();
        boardService.createPost(post);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
