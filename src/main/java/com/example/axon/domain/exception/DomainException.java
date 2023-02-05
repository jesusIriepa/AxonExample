package com.example.axon.domain.exception;

public class DomainException extends RuntimeException {
    public DomainException() {
    }

    public DomainException(String message) {
        super(message);
    }
}
