package com.example.boardpractice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardIndexInitializer implements ApplicationRunner {
    private static final String INDEX_NAME = "boards";
    private static final String ANALYZER_NAME = "boards_content_analyzer";

    private final ElasticsearchClient esClient;

    @Override
    public void run(ApplicationArguments args) {
        try {
            if (esClient.indices().exists(e -> e.index(INDEX_NAME)).value()) {
                log.info("Elasticsearch index already exists: {}", INDEX_NAME);
                return;
            }

            createBoardsIndex();
            log.info("Elasticsearch index created: {}", INDEX_NAME);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to initialize Elasticsearch index: " + INDEX_NAME, e);
        }
    }

    private void createBoardsIndex() throws IOException {
        esClient.indices().create(c -> c
                .index(INDEX_NAME)
                .settings(s -> s
                        .numberOfShards("1")
                        .numberOfReplicas("1")
                        .analysis(a -> a
                                .analyzer(ANALYZER_NAME, analyzer -> analyzer
                                        .custom(custom -> custom
                                                .tokenizer("nori_tokenizer")
                                                .filter(List.of(
                                                        "nori_part_of_speech",
                                                        "nori_readingform",
                                                        "lowercase"
                                                ))
                                        )
                                )
                        )
                )
                .mappings(m -> m
                        .properties("boardId", p -> p.long_(l -> l))
                        .properties("title", p -> p.text(t -> t.analyzer(ANALYZER_NAME)))
                        .properties("content", p -> p.text(t -> t.analyzer(ANALYZER_NAME)))
                        .properties("writer", p -> p.keyword(k -> k
                                .fields("text", f -> f.text(t -> t.analyzer(ANALYZER_NAME)))
                        ))
                        .properties("createdAt", p -> p.date(d -> d))
                        .properties("isDeleted", p -> p.boolean_(b -> b))
                )
        );
    }
}
