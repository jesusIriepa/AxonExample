package com.example.axon.domain.query;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CardSummary {
    private final String id;
    private final Integer initialAmount;
    private final Integer remainingAmount;
    private final LocalDateTime lastupdate;
    private final String lastUserUpdate;
}