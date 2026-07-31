package com.example.boardpractice.web.dto.Board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // ES 문서에 추가 필드가 있어도 에러 안 나도록 설정
public class PostDto {
    private Long boardId;
    private String id; // 문서 ID (_id)

    private String title; // 게시물 제목

    private String content; // 게시물 본문

    private String author; // 작성자

    private String category; // 카테고리 (예: free, notice 등)

    @JsonProperty("view_count")
    private Integer viewCount; // 조회수 (ES 스네이크 케이스 필드 매핑)

    @JsonProperty("like_count")
    private Integer likeCount; // 좋아요 수

    @JsonProperty("is_deleted")
    private Boolean isDeleted; // 삭제 여부

    @JsonProperty("created_at")
    private String createdAt;
}
