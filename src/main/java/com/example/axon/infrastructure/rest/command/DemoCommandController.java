package com.example.axon.infrastructure.rest.command;

import com.example.axon.domain.command.CreateGiftCardCommand;
import com.example.axon.domain.command.IssueAmountCommand;
import com.example.axon.domain.command.RedeemAmountCommand;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Profile("command")
@RestController
@RequestMapping("/card")
public class DemoCommandController {

    private final CommandGateway commandGateway;

    public DemoCommandController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<String>> createGiftCard(
        @RequestBody CreateGiftRequest createGiftRequest) {
        String newGiftCardId = UUID.randomUUID().toString();
        log.info("Create Gift Card request: {}", newGiftCardId);
        return commandGateway.send(new CreateGiftCardCommand(
            newGiftCardId,
            createGiftRequest.getUser(),
            createGiftRequest.getInitialAmount()))
            .thenApply(r-> ResponseEntity.status(HttpStatus.CREATED).body(newGiftCardId));
    }

    @PutMapping("/{giftCardId}/issue")
    public CompletableFuture<ResponseEntity<Void>> issueAmount(
        @PathVariable String giftCardId,
        @RequestBody IssueAmountRequest issueAmountRequest) {
        log.info("Issued Gift Card request: {} - {}€", giftCardId, issueAmountRequest.amount);
        return commandGateway.send(new IssueAmountCommand(
            giftCardId,
            issueAmountRequest.getUser(),
            issueAmountRequest.getAmount()))
            .thenApply(r-> ResponseEntity.ok().build());
    }

    @PutMapping("/{giftCardId}/redeem")
    public CompletableFuture<ResponseEntity<Void>> redeemAmount(
        @PathVariable String giftCardId,
        @RequestBody RedeemAmountRequest redeemAmountRequest) {
        log.info("Redeem Gift Card request: {} - {}€", giftCardId, redeemAmountRequest.amount);
        return commandGateway.send(new RedeemAmountCommand(
            giftCardId,
            redeemAmountRequest.getUser(),
            redeemAmountRequest.getAmount()))
            .thenApply(r-> ResponseEntity.ok().build());
    }


    @Data
    @NoArgsConstructor
    private static class CreateGiftRequest {
        private String user;
        private Integer initialAmount;
    }

    @Data
    @NoArgsConstructor
    private static class IssueAmountRequest {
        private String user;
        private int amount;
    }

    @Data
    @NoArgsConstructor
    private static class RedeemAmountRequest {
        private String user;
        private int amount;
    }
}
