package com.backend.rate_limiter.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ClientNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, Object> handleClientNotFound(
            ClientNotFoundException exception) {

        return Map.of(
                "success", false,
                "message", exception.getMessage()
        );
    }

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimitExceeded(
            RateLimitExceededException exception) {

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header(
                        "Retry-After",
                        String.valueOf(exception.getRetryAfterSeconds())
                )
                .body(
                        Map.of(
                                "success", false,
                                "message", exception.getMessage()
                        )
                );
    }
}  

    

