package com.example.axon.infrastructure.rest.shared;

import com.example.axon.domain.exception.ApplicationException;
import com.example.axon.domain.exception.DomainException;
import com.example.axon.infrastructure.rest.shared.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class RestErrorController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
      HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("Rest Method Argument Exception", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .cause(e.getMessage())
            .detail(e.getBindingResult().getFieldErrors().stream()
                .map(objectError -> objectError.getField() + " - >" + objectError.getDefaultMessage())
                .collect(Collectors.joining(" || ")))
            .httpCode(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainArgumentException(DomainException e) {
        log.error("Domain Exception", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .cause(e.getCause().getMessage())
            .detail(e.getMessage())
            .httpCode(HttpStatus.BAD_REQUEST.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationNotFoundException(ApplicationException e) {
        log.error("Application Exception", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .cause(e.getCause().getMessage())
            .detail(e.getMessage())
            .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception e) {
        log.error("Rest Global Exception", e);
        ErrorResponse errorResponse = ErrorResponse.builder()
            .cause(e.getMessage())
            .detail(Arrays.stream(e.getStackTrace())
                .limit(5)
                .map(StackTraceElement::toString)
                .collect(Collectors.joining("\n")))
            .httpCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .timestamp(LocalDateTime.now())
            .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
