package com.example.axon.domain.query;

import com.example.axon.domain.event.CreateGiftCardEvt;
import com.example.axon.domain.event.IssuedEvt;
import com.example.axon.domain.event.RedeemedEvt;
import com.example.axon.infrastructure.repository.CardSummaryRepository;
import com.example.axon.infrastructure.repository.entity.CardSummaryEntity;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

@Profile("query")
@ProcessingGroup("amqpEvents")
@Component
public class CardSummaryProjection {

    private final CardSummaryRepository cardSummaryRepository;

    public CardSummaryProjection(CardSummaryRepository cardSummaryRepository) {
        this.cardSummaryRepository = cardSummaryRepository;
    }

    @EventHandler
    public void handle(CreateGiftCardEvt createGiftCardEvt) {
        CardSummaryEntity cardSummaryEntity = new CardSummaryEntity();
        cardSummaryEntity.setId(createGiftCardEvt.getGiftCardId());
        cardSummaryEntity.setCurrentValue(createGiftCardEvt.getInitialAmount());
        cardSummaryEntity.setInitialValue(createGiftCardEvt.getInitialAmount());
        cardSummaryEntity.setUserUpdate(createGiftCardEvt.getUser());
        cardSummaryEntity.setLastupdate(Instant.now());
        cardSummaryRepository.save(cardSummaryEntity);
    }

    @EventHandler
    public void handle(IssuedEvt issuedEvt) {
        Optional<CardSummaryEntity> cardSummaryOptional = cardSummaryRepository.findById(issuedEvt.getGiftCardId());
        if(cardSummaryOptional.isPresent()) {
            CardSummaryEntity cardSummaryEntity = cardSummaryOptional.get();
            cardSummaryEntity.setCurrentValue(cardSummaryEntity.getCurrentValue() + issuedEvt.getAmount());
            cardSummaryRepository.save(cardSummaryEntity);
        }
    }

    @EventHandler
    public void handle(RedeemedEvt redeemedEvt) {
        Optional<CardSummaryEntity> cardSummaryOptional = cardSummaryRepository.findById(redeemedEvt.getGiftCardId());
        if(cardSummaryOptional.isPresent()) {
            CardSummaryEntity cardSummaryEntity = cardSummaryOptional.get();
            cardSummaryEntity.setCurrentValue(cardSummaryEntity.getCurrentValue() - redeemedEvt.getAmount());
            cardSummaryRepository.save(cardSummaryEntity);
        }
    }

    @QueryHandler
    public CardSummary handle(FindCardByIdQuery findCardByIdQuery) {
       return cardSummaryRepository.findById(findCardByIdQuery.getGiftCardId())
           .map(this::fromEntity)
           .orElseThrow(() -> new IllegalArgumentException("CardSummary not found ¡¡"));
    }

    private CardSummary fromEntity(CardSummaryEntity cardSummaryEntity) {
        return new CardSummary(
            cardSummaryEntity.getId(),
            cardSummaryEntity.getInitialValue(),
            cardSummaryEntity.getCurrentValue(),
            LocalDateTime.from(cardSummaryEntity.getLastupdate().atZone(ZoneId.systemDefault())),
            cardSummaryEntity.getUserUpdate());
    }

}