package com.example.axon.domain;

import com.example.axon.domain.command.CreateGiftCardCommand;
import com.example.axon.domain.event.CreateGiftCardEvt;
import com.example.axon.domain.exception.DomainException;
import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

class GiftCardTest {

    private FixtureConfiguration<GiftCard> fixtureConfiguration;

    @BeforeEach
    public void setUp() {
        fixtureConfiguration = new AggregateTestFixture<>(GiftCard.class);
    }

    @Test
    void should_create_a_valid_giftCard() {
        String giftCardId = UUID.randomUUID().toString();
        String user = "TestUser";
        Integer initialAmount = 100;
        CreateGiftCardCommand createGiftCardCommand = new CreateGiftCardCommand(giftCardId, user, initialAmount);
        fixtureConfiguration.givenCommands(createGiftCardCommand)
            .whenTimeElapses(Duration.ofSeconds(5))
            .expectState(giftCard -> {
                Assertions.assertEquals(giftCardId, giftCard.getGiftCardId());
                Assertions.assertEquals(initialAmount, giftCard.getRemainingAmount());});
    }

    @Test
    void should_throw_initial_amount_exception_creating_a_new_giftCard() {
        String giftCardId = UUID.randomUUID().toString();
        String user = "TestUser";
        Integer initialAmount = -100;
        CreateGiftCardCommand createGiftCardCommand = new CreateGiftCardCommand(giftCardId, user, initialAmount);
        fixtureConfiguration
            .givenCommands()
            .when(createGiftCardCommand)
            .expectException(DomainException.class);
    }

}