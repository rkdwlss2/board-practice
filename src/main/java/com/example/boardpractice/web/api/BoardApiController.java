package com.example.boardpractice.web.api;

import com.example.boardpractice.common.utill.LoginRequired;
import com.example.boardpractice.common.utill.LoginUser;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.service.BoardService;
import com.example.boardpractice.service.FileService;
import com.example.boardpractice.web.dto.Board.*;
import com.example.boardpractice.web.dto.file.FileInfoDto;
import com.example.boardpractice.web.dto.user.SessionUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    public ResponseEntity<?> getDetailPost(@PathVariable Long boardId,@AuthenticationPrincipal SessionUser sessionUser){
        String currentUserNickname = (sessionUser==null)?null:sessionUser.getNickname();
        BoardDetailResponseDto boardDetailResponseDto = boardService.getPost(boardId,currentUserNickname);
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

    @PostMapping(value = "/boards/posts/{boardId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFile(
            @RequestPart("multipartFile")
            MultipartFile file,@PathVariable Long boardId,@AuthenticationPrincipal SessionUser user) throws FileUploadException {
        FileInfoDto fileinfo = fileService.uploadFile(file);	//서버 내부 스토리지 저장
        //Long success = fileService.insertFileInfo(fileinfo);	//데이터베이스에 파일 정보 저장
        boardService.updateBoardImage(boardId,user.getUserId(),fileinfo.getFilePath());
        return new ResponseEntity<>(fileinfo,HttpStatus.OK);
    }

//    @GetMapping(value = "/boards/posts/search")
//    public ResponseEntity<?> searchPosts(@RequestParam("keyword") String keyword,@PageableDefault(page=0,size=10) Pageable pageable){
//        Page<BoardSearchResponseDto> result = boardService.searchPostsByKeyword(keyword,pageable);
//        return new ResponseEntity<>(result, HttpStatus.OK);
//    }

    @GetMapping("/boards/posts/search")
    public ResponseEntity<List<PostDto>> search(
            @RequestParam(name = "keyword") String keyword,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) throws IOException {
        List<PostDto> result = boardService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(result);
    }


}
