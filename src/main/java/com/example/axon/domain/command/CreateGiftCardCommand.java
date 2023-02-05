package com.example.axon.domain.command;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@AllArgsConstructor
public class CreateGiftCardCommand {
    @TargetAggregateIdentifier
    String giftCardId;
    String user;
    Integer initialAmount;
}
