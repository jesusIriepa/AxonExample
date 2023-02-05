package com.example.axon.domain.event;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@Value
@AllArgsConstructor
public class IssuedEvt {

    @TargetAggregateIdentifier
    String giftCardId;
    String user;
    Integer amount;

}