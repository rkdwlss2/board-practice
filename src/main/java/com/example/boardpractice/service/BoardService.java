package com.example.boardpractice.service;

import com.example.boardpractice.entity.Post;
import com.example.boardpractice.web.dto.Board.PostDetailResponseDto;
import com.example.boardpractice.web.dto.Board.PostResponseDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    public List<PostResponseDto> getAllPosts(int page,int size){
        List<PostResponseDto> postList = new ArrayList<>();
        postList.add(
                PostResponseDto.builder()
                        .boardId(2024L)
                        .title("제목1")
                        .writer("더미작성자1")
                        .likeCount(2L)
                        .commentCount(3L)
                        .viewCount(4L)
                        .writeDate("2014-01-02 00:01:00")
                        .createDate("2014-01-02 00:01:00")
                        .updatedDate("2014-01-02 00:01:00")
                .build()
        );
        postList.add(
                PostResponseDto.builder()
                        .boardId(2025L)
                        .title("제목2")
                        .writer("더미작성자2")
                        .likeCount(4L)
                        .commentCount(32L)
                        .viewCount(43L)
                        .writeDate("2014-01-02 00:01:00")
                        .createDate("2014-01-02 00:01:00")
                        .updatedDate("2014-01-02 00:01:00")
                        .build()
        );
        return postList;
    }

    public PostDetailResponseDto getPost(Long boardId){
        PostDetailResponseDto postDetailResponseDto = PostDetailResponseDto.builder()
                .boardId(2025L)
                .title("제목2")
                .writer("더미작성자2")
                .commentCount(32L)
                .boardImageUrl("www.google.com/urltest")
                .viewCount(43L)
                .writeDate("2014-01-02 00:01:00")
                .createDate("2014-01-02 00:01:00")
                .updatedDate("2014-01-02 00:01:00")
                .build();
        return postDetailResponseDto;
    }

    public void updatePost(Post post) {
//        Post dbPost = repository.get(post.getBoardId());
//        if (dbPost==null){
//           throw new RuntimeException("해당 글의 ID가 없습니다.");
//        }
//         repositroy.updatePost(); //구현필요
    }

    public void createPost(Post post){
        // Long newId = idGenerator.getAndIncrement();
        //
        // repositroy.createPost(newId); 구현필요
    }
}
