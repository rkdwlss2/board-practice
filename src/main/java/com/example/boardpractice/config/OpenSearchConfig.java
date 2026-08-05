package com.example.boardpractice.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.HttpHost;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class OpenSearchConfig {
    @Value("${spring.opensearch.uris}")
    private String openSearchUrl;

    @Bean
    public OpenSearchClient openSearchClient() throws Exception {
        log.info("OpenSearch url: {}", openSearchUrl);

        RestClient restClient = RestClient.builder(
                HttpHost.create(openSearchUrl)
        ).build();

        OpenSearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        return new OpenSearchClient(transport);
    }
}
