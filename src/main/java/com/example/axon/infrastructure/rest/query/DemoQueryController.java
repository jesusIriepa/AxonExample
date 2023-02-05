package com.example.axon.infrastructure.rest.query;

import com.example.axon.domain.query.CardHistory;
import com.example.axon.domain.query.CardSummary;
import com.example.axon.domain.query.FetchCardHistoryQuery;
import com.example.axon.domain.query.FindCardByIdQuery;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Profile("query")
@RestController
@RequestMapping("/card")
public class DemoQueryController {

    private final QueryGateway queryGateway;

    public DemoQueryController(QueryGateway queryGateway) {
        this.queryGateway = queryGateway;
    }

    @GetMapping("/{giftCardId}")
    public CompletableFuture<CardSummary> getCardSummary(@PathVariable String giftCardId) {
        log.info("Card Summary Request: {}", giftCardId);
        return queryGateway.query(new FindCardByIdQuery(giftCardId),
                ResponseTypes.instanceOf(CardSummary.class));
    }

    @GetMapping("/{giftCardId}/history")
    public CompletableFuture<List<CardHistory>> getCardHistory(
        @PathVariable String giftCardId,
        @RequestParam(defaultValue = "10") Integer limit) {
        log.info("Card Movement History Request: {}", giftCardId);
        return queryGateway.query(new FetchCardHistoryQuery(giftCardId, limit),
            ResponseTypes.multipleInstancesOf(CardHistory.class));
    }

}
