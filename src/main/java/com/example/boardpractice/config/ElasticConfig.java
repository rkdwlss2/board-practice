package com.example.boardpractice.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ElasticConfig {
    @Value("${spring.elasticsearch.uris}")
    private String esUrl;
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        log.info("Elasticsearch url: {}", esUrl);
        // 1. Low Level RestClient 생성 (호스트 및 포트 지정)
        RestClient restClient = RestClient.builder(
                HttpHost.create(esUrl) // 예: http://localhost:9200
        ).build();

        // 2. Jackson 기반의 Transport 생성 (JSON 객체 변환 처리)
        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        // 3. High Level ElasticsearchClient 반환
        return new ElasticsearchClient(transport);
    }
}
