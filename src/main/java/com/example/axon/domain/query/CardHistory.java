package com.example.axon.domain.query;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@AllArgsConstructor
public class CardHistory {
    String giftCardId;
    Integer operationValue;
    String operationType;
    LocalDateTime operationTime;
    String operationUser;
}
