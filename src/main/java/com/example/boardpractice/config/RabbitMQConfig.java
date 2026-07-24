package com.example.boardpractice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "board.exchange";
    public static final String QUEUE_NAME = "board.es.queue";
    public static final String ROUTING_KEY = "board.create";

    // 1. Exchange (우체국) 생성
    @Bean
    public TopicExchange boardExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 2. Queue (우체통) 생성
    @Bean
    public Queue boardQueue() {
        return new Queue(QUEUE_NAME);
    }

    // 3. Exchange와 Queue 연결 (Routing Key 기준)
    @Bean
    public Binding binding(Queue boardQueue, TopicExchange boardExchange) {
        return BindingBuilder.bind(boardQueue).to(boardExchange).with(ROUTING_KEY);
    }

    // 4. 객체를 JSON 형태로 큐에 넣기 위한 컨버터
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}