package com.example.boardpractice.web.dto.Board;

import com.example.boardpractice.entity.Boards;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDocument {
    private Long boardId;
    private String title;
    private String content;
    private String writer;
    private String createdAt; // ISO-8601 문자열 형태 (예: "2026-07-24T18:00:00")

    // RDBMS의 Board Entity를 BoardDocument로 변환해 주는 편의 메서드 (선택사항)
    public static BoardDocument from(Boards board) {
        return BoardDocument.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .content(board.getContent())
                .writer(board.getUser().getNickname())
                .createdAt(board.getBaseTimeEntity().getCreateDate().toString())
                .build();
    }
}
