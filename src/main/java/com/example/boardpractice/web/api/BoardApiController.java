package com.example.boardpractice.web.api;

import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.service.BoardService;
import com.example.boardpractice.service.FileService;
import com.example.boardpractice.web.dto.Board.PostDetailResponseDto;
import com.example.boardpractice.web.dto.Board.PostResponseDto;
import com.example.boardpractice.web.dto.Board.PostRequestDto;
import com.example.boardpractice.web.dto.Board.PostUpdateResponseDto;
import com.example.boardpractice.web.dto.file.FileInfoDto;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardApiController {

    private final FileService fileService;
    private final BoardService boardService;

    @GetMapping("/boards/posts")
    public ResponseEntity<?> getPosts(@RequestParam int page, @RequestParam int size){
        Page<PostResponseDto> postResponseDtoList= boardService.getAllPosts(page,size);
        return new ResponseEntity<>(postResponseDtoList, HttpStatus.OK);
    }

    @GetMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> getDetailPost(@PathVariable Long boardId){
        PostDetailResponseDto postDetailResponseDto = boardService.getPost(boardId);
        return new ResponseEntity<>(postDetailResponseDto,HttpStatus.OK);
    }


    @PostMapping("/boards/posts/{userId}")
    public ResponseEntity<?> createDetailPost(@RequestBody PostRequestDto postRequestDto,@PathVariable Long userId){
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        boardService.createPost(userId,title,content);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/boards/posts/{boardId}")
    public ResponseEntity<?> updateDetailPost(@PathVariable Long boardId, @RequestBody PostRequestDto postRequestDto){
        String title = postRequestDto.getTitle();
        String content = postRequestDto.getContent();
        PostUpdateResponseDto postUpdateResponseDto = boardService.updatePost(boardId,title,content);
        return new ResponseEntity<>(postUpdateResponseDto,HttpStatus.OK);
    }

    @PostMapping(value = "/boards/posts/{boardId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFile(
            @RequestPart("multipartFile")
            MultipartFile file,@PathVariable Long boardId) throws FileUploadException {
        FileInfoDto fileinfo = fileService.uploadFile(file);	//서버 내부 스토리지 저장
        //Long success = fileService.insertFileInfo(fileinfo);	//데이터베이스에 파일 정보 저장

        return new ResponseEntity<>(HttpStatus.OK);
    }



}
