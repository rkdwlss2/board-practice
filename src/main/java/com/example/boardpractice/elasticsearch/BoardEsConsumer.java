package com.example.boardpractice.elasticsearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.boardpractice.config.RabbitMQConfig;
import com.example.boardpractice.entity.Boards;
import com.example.boardpractice.web.dto.Board.BoardDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardEsConsumer {
    private final ElasticsearchClient esClient;

    // 지정한 큐에 메시지가 들어오면 이 메서드가 자동으로 실행됨
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void syncToElasticsearch(Boards savedBoard) {
        log.info("RabbitMQ 메시지 수신 - ES 동기화 시작: {}", savedBoard.getBoardId());

        try {
            BoardDocument doc = BoardDocument.from(savedBoard);

            esClient.index(i -> i
                    .index("boards")
                    .id(savedBoard.getBoardId().toString())
                    .document(doc)
            );

            log.info("ES 동기화 완료: {}", savedBoard.getBoardId());
        } catch (Exception e) {
            log.error("ES 동기화 실패 (Board ID: {})", savedBoard.getBoardId(), e);
            // ※ 나중에는 여기서 실패하면 Dead Letter Queue(DLQ)로 보내서
            // 수동으로 재처리하는 고급 로직을 짤 수 있습니다!
        }
    }
}
