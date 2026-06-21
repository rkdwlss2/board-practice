package com.example.boardpractice.entity;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BaseTimeEntity {
    private LocalDateTime deleteDate;
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;
}
