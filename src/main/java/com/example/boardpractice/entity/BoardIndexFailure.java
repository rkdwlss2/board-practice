package com.example.boardpractice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BoardIndexFailure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long failureId;

    @Column(nullable = false)
    private Long boardId;

    @Column(nullable = false, length = 100)
    private String indexName;

    @Column(nullable = false, length = 30)
    private String operation;

    @Column(nullable = false, length = 2000)
    private String reason;

    @Builder.Default
    @Column(nullable = false)
    private boolean resolved = false;

    @Builder.Default
    @Column(nullable = false)
    private int retryCount = 0;

    @Builder.Default
    @Embedded
    private BaseTimeEntity baseTimeEntity = new BaseTimeEntity();

    public static BoardIndexFailure create(Long boardId, String indexName, String operation, Exception exception) {
        return BoardIndexFailure.builder()
                .boardId(boardId)
                .indexName(indexName)
                .operation(operation)
                .reason(toReason(exception))
                .build();
    }

    private static String toReason(Exception exception) {
        String reason = exception.getMessage();
        if (reason == null || reason.isBlank()) {
            reason = exception.getClass().getName();
        }
        return reason.length() > 2000 ? reason.substring(0, 2000) : reason;
    }
}
