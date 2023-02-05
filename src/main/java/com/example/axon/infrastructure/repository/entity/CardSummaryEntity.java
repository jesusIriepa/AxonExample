package com.example.axon.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Profile("query")
@Entity
@Table(name = "CardSummary")
public class CardSummaryEntity {
    @Id
    @Column(name = "CARDSUMMARY_ID")
    private String id;

    @Column(name = "CARDSUMMARY_CURRENT_VALUE")
    private Integer currentValue;

    @Column(name = "CARDSUMMARY_INITIAL_VALUE")
    private Integer initialValue;

    @Column(name = "CARDSUMMARY_LAST_UPDATE")
    private Instant lastupdate;

    @Column(name = "CARDSUMMARY_USER_UPDATE")
    private String userUpdate;
}

