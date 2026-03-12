package com.creditscoring.calculator.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(
            Integer status,
            String message,
            List<String> details,
            Instant timestamp
    ) {}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        return new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка прескоринга",
                errors,
                Instant.now()
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatusExceptions(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                new ApiError(
                        e.getStatusCode().value(),
                        e.getMessage(),
                        e.getReason() != null ? List.of(e.getReason()) : List.of(),
                        Instant.now()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiError(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Внутренняя ошибка сервера",
                        List.of(e.getMessage()),
                        Instant.now()
                )
        );
    }
}
