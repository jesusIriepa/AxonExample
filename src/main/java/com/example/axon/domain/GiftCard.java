package com.example.axon.domain;

import com.example.axon.domain.command.CreateGiftCardCommand;
import com.example.axon.domain.command.IssueAmountCommand;
import com.example.axon.domain.command.RedeemAmountCommand;
import com.example.axon.domain.event.CreateGiftCardEvt;
import com.example.axon.domain.event.IssuedEvt;
import com.example.axon.domain.event.RedeemedEvt;
import com.example.axon.domain.exception.DomainException;
import lombok.Getter;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Getter
@Profile("command")
@Aggregate
public class GiftCard {

    @AggregateIdentifier
    private String giftCardId;

    private Integer remainingAmount;

    public GiftCard() {
        /* Mandatory Axon constructor */
    }

    @CommandHandler
    public GiftCard(CreateGiftCardCommand createGiftCardCommand) {
        createGuard(createGiftCardCommand.getGiftCardId(), createGiftCardCommand.getInitialAmount());
        apply(new CreateGiftCardEvt(createGiftCardCommand.getGiftCardId(), createGiftCardCommand.getUser(),
            createGiftCardCommand.getInitialAmount()));
    }

    @CommandHandler
    public void issuedAmount(IssueAmountCommand issueAmountCommand) {
        issuedAmountGuard(issueAmountCommand.getAmount());
        AggregateLifecycle.apply(new IssuedEvt(this.giftCardId, issueAmountCommand.getUser(),
            issueAmountCommand.getAmount()));
    }

    @CommandHandler
    public void redeemAmount(RedeemAmountCommand redeemAmountCommand) {
        redeemAmountGuard(redeemAmountCommand.getAmount());
        AggregateLifecycle.apply(new RedeemedEvt(this.giftCardId, redeemAmountCommand.getUser(),
            redeemAmountCommand.getAmount()));
    }

    @EventSourcingHandler
    public void handle(CreateGiftCardEvt createGiftCardEvt) {
        this.giftCardId = createGiftCardEvt.getGiftCardId();
        this.remainingAmount = createGiftCardEvt.getInitialAmount();
    }

    @EventSourcingHandler
    public void handle(RedeemedEvt redeemedEvt) {
        this.remainingAmount = this.remainingAmount - redeemedEvt.getAmount();
    }

    @EventSourcingHandler
    public void handle(IssuedEvt issuedEvt) {
        this.remainingAmount = this.remainingAmount + issuedEvt.getAmount();
    }

    private void createGuard(String giftCardId, Integer initialAmount) {
        if (initialAmount == null) {
            throw new DomainException("Initial Amount can't be null");
        }
        if (initialAmount < 0) {
            throw new DomainException("Initial Amount can't be lower than 0");
        }
        try {
            UUID.fromString(giftCardId);
        } catch (Exception e) {
            throw new DomainException(String.format("GiftCard ID wrong value %s", giftCardId));
        }
    }

    private void issuedAmountGuard(Integer issuedAmount) {
        if (issuedAmount == null) {
            throw new DomainException("Issued Amount can't be null");
        }
        if (issuedAmount < 0) {
            throw new DomainException("Issued Amount can't be lower than 0");
        }
    }

    private void redeemAmountGuard(Integer redeemAmount) {
        if (redeemAmount == null) {
            throw new DomainException("Redeem Amount can't be null");
        }
        if (redeemAmount < 0) {
            throw new DomainException("Redeem Amount can't be lower than 0");
        }
        if (this.remainingAmount < redeemAmount) {
            throw new DomainException("Not enough Remaining Amount");
        }
    }
}