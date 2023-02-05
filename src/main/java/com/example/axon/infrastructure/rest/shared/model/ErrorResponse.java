package com.example.axon.infrastructure.rest.shared.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorResponse {
    private String cause;
    private String detail;
    private Integer httpCode;
    private LocalDateTime timestamp;
}
