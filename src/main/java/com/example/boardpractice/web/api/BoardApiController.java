package com.example.boardpractice.web.api;

import com.example.boardpractice.common.utill.ClientUtils;
import com.example.boardpractice.service.BoardService;
import com.example.boardpractice.service.FileService;
import com.example.boardpractice.web.dto.Board.*;
import com.example.boardpractice.web.dto.user.SessionUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final FileService fileService;
    private final BoardService boardService;

    @GetMapping("/boards/posts")
    public ResponseEntity<?> getPosts(@PageableDefault(page = 0, size = 10) Pageable pageable){
        Page<BoardListResponseDto> postResponseDtoList = boardService.getAllPosts(pageable);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> getDetailPost(@PathVariable Long boardId,@AuthenticationPrincipal SessionUser sessionUser,
                                           HttpServletRequest request){
        // IP 추출
        String clientIp = ClientUtils.getClientIp(request);
        String currentUserNickname = (sessionUser==null)?null:sessionUser.getNickname();
        BoardDetailResponseDto boardDetailResponseDto = boardService.getPost(boardId,currentUserNickname,clientIp);
        return new ResponseEntity<>(boardDetailResponseDto,HttpStatus.OK);
    }


    @PostMapping("/boards/posts")
//    @LoginRequired
    public ResponseEntity<?> createDetailPost(@RequestBody @Valid BoardRequestDto boardRequestDto, @AuthenticationPrincipal SessionUser user){
        String title = boardRequestDto.getTitle();
        String content = boardRequestDto.getContent();
        BoardCreateResponseDto boardCreateResponseDto = boardService.createPost(user.getUserId(), title,content);
        return new ResponseEntity<>(boardCreateResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> updateDetailPost(@PathVariable Long boardId, @RequestBody BoardRequestDto boardRequestDto,@AuthenticationPrincipal SessionUser user){
        String title = boardRequestDto.getTitle();
        String content = boardRequestDto.getContent();
        BoardUpdateResponseDto boardUpdateResponseDto = boardService.updatePost(boardId,title,content,user.getUserId());
        return new ResponseEntity<>(boardUpdateResponseDto,HttpStatus.OK);
    }

    @DeleteMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> deleteDetailPost(@PathVariable Long boardId,@AuthenticationPrincipal SessionUser user){
        boardService.deletePost(boardId,user.getUserId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/me/posts")
    public ResponseEntity<?> getUserPosts(@PageableDefault(page=0,size = 10) Pageable pageable,@AuthenticationPrincipal SessionUser user){
        Page<BoardListResponseDto> posts = boardService.getMyPosts(user.getUserId(),pageable);
        return ResponseEntity.ok(posts);
    }

    public record BoardImageRequest(String imageUrl) {
    }

    @PostMapping("/boards/posts/{boardId}/image")
    public ResponseEntity<?> addFile(
            @RequestBody BoardImageRequest request,
            @PathVariable Long boardId,
            @AuthenticationPrincipal SessionUser user) {
        boardService.updateBoardImage(boardId, user.getUserId(), request.imageUrl());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/boards/posts/presigned-url")
    public ResponseEntity<?> getPresignedUrl(@RequestBody Map<String, String> request) {
        String filename = request.get("filename");
        String fileType = request.get("filetype");
        String presignedUrl = fileService.generatePresignedUrl(filename, fileType);
        return ResponseEntity.ok(Map.of("presignedUrl", presignedUrl));
    }

    @GetMapping("/boards/posts/search")
    public ResponseEntity<List<BoardListResponseDto>> search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) throws IOException {
        List<BoardListResponseDto> result = boardService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/boards/posts/search/fulltext")
    public ResponseEntity<List<BoardListResponseDto>> searchByFullText(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<BoardListResponseDto> result = boardService.searchPostsByFullText(keyword, page, size);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/boards/posts/search/like")
    public ResponseEntity<List<BoardListResponseDto>> searchByLike(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        List<BoardListResponseDto> result = boardService.searchPostsByLike(keyword, page, size);
        return ResponseEntity.ok(result);
    }

}
