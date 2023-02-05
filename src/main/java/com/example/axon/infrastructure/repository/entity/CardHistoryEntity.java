package com.example.axon.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.persistence.*;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Profile("query")
@Entity
@Table(name = "CardHistory")
public class CardHistoryEntity {
    @Id
    @Column(name = "CARDHISTORY_ID")
    private String id;

    @Column(name = "CARDHISTORY_CARD_ID")
    private String giftCardId;

    @Column(name = "CARDSUMMARY_OPERATION_VALUE")
    private Integer operationValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "CARDSUMMARY_OPERATION_TYPE")
    private OperationType operationType;

    @Column(name = "CARDSUMMARY_OPERATION_TIME")
    private Instant operationTime;

    @Column(name = "CARDSUMMARY_OPERATION_USER")
    private String operationUser;
}
