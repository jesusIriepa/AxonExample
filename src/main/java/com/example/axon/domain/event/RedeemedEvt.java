package com.example.axon.domain.event;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@AllArgsConstructor
public class RedeemedEvt {

    @TargetAggregateIdentifier
    String giftCardId;
    String user;
    Integer amount;

}