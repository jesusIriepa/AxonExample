package com.example.axon.domain.query;

import com.example.axon.domain.event.CreateGiftCardEvt;
import com.example.axon.domain.event.IssuedEvt;
import com.example.axon.domain.event.RedeemedEvt;
import com.example.axon.infrastructure.repository.CardHistoryRepository;
import com.example.axon.infrastructure.repository.entity.CardHistoryEntity;
import com.example.axon.infrastructure.repository.entity.OperationType;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Profile("query")
@ProcessingGroup("amqpEvents")
@Component
public class CardHistoryProjection {

    private final CardHistoryRepository cardHistoryRepository;

    public CardHistoryProjection(CardHistoryRepository cardHistoryRepository) {
        this.cardHistoryRepository = cardHistoryRepository;
    }

    @EventHandler
    public void handle(CreateGiftCardEvt createGiftCardEvt) {
        CardHistoryEntity cardHistoryEntity = new CardHistoryEntity();
        cardHistoryEntity.setId(UUID.randomUUID().toString());
        cardHistoryEntity.setGiftCardId(createGiftCardEvt.getGiftCardId());
        cardHistoryEntity.setOperationValue(createGiftCardEvt.getInitialAmount());
        cardHistoryEntity.setOperationType(OperationType.CREATED);
        cardHistoryEntity.setOperationUser(createGiftCardEvt.getUser());
        cardHistoryEntity.setOperationTime(Instant.now());
        cardHistoryRepository.save(cardHistoryEntity);
    }

    @EventHandler
    public void handle(IssuedEvt issuedEvt) {
        CardHistoryEntity cardHistoryEntity = new CardHistoryEntity();
        cardHistoryEntity.setId(UUID.randomUUID().toString());
        cardHistoryEntity.setGiftCardId(issuedEvt.getGiftCardId());
        cardHistoryEntity.setOperationValue(issuedEvt.getAmount());
        cardHistoryEntity.setOperationType(OperationType.ISSUED);
        cardHistoryEntity.setOperationUser(issuedEvt.getUser());
        cardHistoryEntity.setOperationTime(Instant.now());
        cardHistoryRepository.save(cardHistoryEntity);
    }

    @EventHandler
    public void handle(RedeemedEvt redeemedEvt) {
        CardHistoryEntity cardHistoryEntity = new CardHistoryEntity();
        cardHistoryEntity.setId(UUID.randomUUID().toString());
        cardHistoryEntity.setGiftCardId(redeemedEvt.getGiftCardId());
        cardHistoryEntity.setOperationValue(redeemedEvt.getAmount());
        cardHistoryEntity.setOperationType(OperationType.REDEEM);
        cardHistoryEntity.setOperationUser(redeemedEvt.getUser());
        cardHistoryEntity.setOperationTime(Instant.now());
        cardHistoryRepository.save(cardHistoryEntity);
    }

    @QueryHandler
    public List<CardHistory> handle(FetchCardHistoryQuery fetchCardHistoryQuery) {
        Page<CardHistoryEntity> page = cardHistoryRepository.findAllByGiftCardId(
            fetchCardHistoryQuery.getGiftCardId(), Pageable.ofSize(fetchCardHistoryQuery.getSize()));
        return page.get()
            .map(this::fromEntity)
            .collect(Collectors.toList());
    }

    private CardHistory fromEntity(CardHistoryEntity cardHistoryEntity) {
        return new CardHistory(
            cardHistoryEntity.getGiftCardId(),
            cardHistoryEntity.getOperationValue(),
            cardHistoryEntity.getOperationType().toString(),
            LocalDateTime.from(cardHistoryEntity.getOperationTime().atZone(ZoneId.systemDefault())),
            cardHistoryEntity.getOperationUser());
    }
}
