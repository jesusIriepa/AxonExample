package com.example.axon.domain.query;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FetchCardHistoryQuery {

    private final String giftCardId;
    private final Integer size;
}