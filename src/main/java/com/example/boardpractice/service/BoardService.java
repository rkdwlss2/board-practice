package com.example.boardpractice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.boardpractice.entity.BoardIndexFailure;
import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.BoardIndexFailureRepository;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.Board.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardIndexFailureRepository boardIndexFailureRepository;
    private final ElasticsearchClient esClient;

    public Page<BoardListResponseDto> getAllPosts(Pageable pageable){
        return boardRepository.findAllWithCounts(pageable);
    }

    public BoardDetailResponseDto getPost(Long boardId,String currentUserNickname){
        BoardDetailDto boardDetailDto = boardRepository.findByIdWithCounts(boardId).orElseThrow(() -> new IllegalArgumentException("게시글 찾지 못했습니다."));
        BoardDetailResponseDto boardDetailResponseDto = BoardDetailResponseDto.builder()
                .boardId(boardId)
                .title(boardDetailDto.getTitle())
                .writer(boardDetailDto.getWriter())
                .likeCount(boardDetailDto.getLikeCount())
                .commentCount(boardDetailDto.getCommentCount())
                .viewCount(boardDetailDto.getViewCount())
                .boardImageUrl(boardDetailDto.getBoardImageUrl())
                .profileImageUrl(boardDetailDto.getProfileImageUrl())
                .content(boardDetailDto.getContent())
                .createDate(boardDetailDto.getCreateDate())
                .updatedDate(boardDetailDto.getUpdatedDate())
                .deleteDate(boardDetailDto.getDeleteDate())
                .build();
        boardDetailResponseDto.createIsOnwer(currentUserNickname);
        return boardDetailResponseDto;
    }

    public Boards findBoardById(Long boardId){
        return boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("게시글 찾지 못했습니다."));
    }

    @Transactional
    public BoardCreateResponseDto createPost(Long userId, String title, String content) {
        Users user = userRepository.findById(userId).orElseThrow(()->new IllegalArgumentException("사용자가 존재하지 않습니다."));
        Boards requestBoard = Boards.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
        Boards responseBoard =boardRepository.save(requestBoard);
        indexBoard(responseBoard, "INDEX");
        return  BoardCreateResponseDto.builder()
                .boardId(responseBoard.getBoardId())
                .build();
    }

    @Transactional
    public BoardUpdateResponseDto updatePost(Long boardId, String title, String content, Long userId) {
        Boards board = findBoardById(boardId);

        if (!board.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글만 업데이트 할수 있습니다.");
        }

        board.changeTitle(title);
        board.changeContent(content);
        indexBoard(board, "UPDATE");
        return BoardUpdateResponseDto.builder()
                .boardId(board.getBoardId())
                .build();
    }

    @Transactional
    public void deletePost(Long boardId,Long userId) {
        Boards board = findBoardById(boardId);
        if (!board.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글만 삭제 할수 있습니다.");
        }
        boardRepository.deleteById(board.getBoardId());
        deleteBoardIndex(board.getBoardId());
    }

    public List<BoardListResponseDto> searchPosts(String keyword, int page, int size) throws IOException {
        int from = page * size;

        SearchResponse<BoardDocument> response = esClient.search(s -> s
                        .index("boards")
                        .from(from)
                        .size(size)
                        .query(q -> q
                                .multiMatch(mm -> mm
                                        .fields("title^2", "content")
                                        .query(keyword)
                                )
                        )
                        .sort(so -> so
                                .field(f -> f
                                        .field("createdAt")
                                        .order(SortOrder.Desc)
                                )
                        ),
                BoardDocument.class
        );

        List<Long> boardIds = response.hits().hits().stream()
                .map(this::extractBoardId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (boardIds.isEmpty()) {
            return List.of();
        }

        Map<Long, Integer> searchOrder = new HashMap<>();
        for (int i = 0; i < boardIds.size(); i++) {
            searchOrder.put(boardIds.get(i), i);
        }

        return boardRepository.findAllWithCountsByBoardIdIn(boardIds).stream()
                .sorted(Comparator.comparingInt(board -> searchOrder.getOrDefault(board.getBoardId(), Integer.MAX_VALUE)))
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateBoardImage(Long boardId,Long userId, String imageUrl) {
        Boards board = boardRepository.findById(boardId).orElseThrow(()->new IllegalArgumentException("게시글 찾지 못했습니다."));
        if (!board.getUser().getUserId().equals(userId)) {
            throw new AccessDeniedException("본인이 작성한 글만 이미지를 등록할 수 있습니다.");
        }
        board.changeBoardImageUrl(imageUrl);
    }

    private void indexBoard(Boards board, String operation) {
        BoardDocument doc = BoardDocument.from(board);
        try {
            // Kibana Dev Tools:
            // PUT /boards/_doc/{boardId}
            // { "boardId": 1, "title": "...", "content": "...", "writer": "...", "createdAt": "..." }
            esClient.index(i -> i
                    .index("boards")
                    .id(board.getBoardId().toString())
                    .document(doc)
            );
        } catch (Exception e) {
            log.error("Elasticsearch {} failed. boardId={}", operation, board.getBoardId(), e);
            boardIndexFailureRepository.save(
                    BoardIndexFailure.create(board.getBoardId(), "boards", operation, e)
            );
        }
    }

    private void deleteBoardIndex(Long boardId) {
        try {
            // Kibana Dev Tools:
            // DELETE /boards/_doc/{boardId}
            esClient.delete(d -> d
                    .index("boards")
                    .id(boardId.toString())
            );
        } catch (Exception e) {
            log.error("Elasticsearch DELETE failed. boardId={}", boardId, e);
            boardIndexFailureRepository.save(
                    BoardIndexFailure.create(boardId, "boards", "DELETE", e)
            );
        }
    }

    private Long extractBoardId(Hit<BoardDocument> hit) {
        BoardDocument source = hit.source();
        if (source != null && source.getBoardId() != null) {
            return source.getBoardId();
        }

        try {
            return Long.valueOf(hit.id());
        } catch (NumberFormatException e) {
            log.warn("Elasticsearch hit id is not a board id. id={}", hit.id());
            return null;
        }
    }
}
