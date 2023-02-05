package com.example.axon.domain.query;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FindCardByIdQuery {
    private final String giftCardId;
}
