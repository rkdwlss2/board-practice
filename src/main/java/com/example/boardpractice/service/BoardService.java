package com.example.boardpractice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.boardpractice.config.RabbitMQConfig;
import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.entity.Users;
import com.example.boardpractice.repository.BoardRepository;
import com.example.boardpractice.repository.CommentRepository;
import com.example.boardpractice.repository.UserRepository;
import com.example.boardpractice.web.dto.Board.*;
import com.example.boardpractice.web.dto.user.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ElasticsearchClient esClient;

    public Page<BoardResponseDto> getAllPosts(Pageable pageable){
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
        // 2. Elasticsearch에 등록 (Spring 내부에서 처리)
        BoardDocument doc = BoardDocument.from(responseBoard);
        try {
            esClient.index(i -> i
                    .index("boards")
                    .id(responseBoard.getBoardId().toString())
                    .document(doc)
            );
        } catch (Exception e) {
            // ES 저장 실패 시 로깅 또는 예외 처리
            log.error("Elasticsearch indexing failed", e);
        }
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
    }

    public List<PostDto> searchPosts(String keyword, int page, int size) throws IOException {
        int from = page * size;

        SearchResponse<PostDto> response = esClient.search(s -> s
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
                PostDto.class
        );

        // 검색 결과 DTO 변환
        return response.hits().hits().stream()
                .map(Hit::source)
                .collect(Collectors.toList());
    }

    public Page<BoardSearchResponseDto> searchPostsByKeyword(String keyword,Pageable pageable){
        return boardRepository.findByContent(keyword,pageable);
    }
}
