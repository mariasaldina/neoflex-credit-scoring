package com.creditscoring.calculator.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    public record ApiError(
            UUID id,
            Integer status,
            String message,
            List<String> details,
            Instant timestamp
    ) {}

    private ApiError createApiError(HttpStatusCode status, String message, List<String> details) {
        return new ApiError(UUID.randomUUID(), status.value(), message, details, Instant.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> fieldErrors = e.getBindingResult().getFieldErrors()
                .stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .toList();

        List<String> globalErrors = e.getBindingResult().getGlobalErrors()
                .stream()
                .map(g -> g.getObjectName() + ": " + g.getDefaultMessage())
                .toList();

        List<String> errors = Stream.concat(fieldErrors.stream(), globalErrors.stream())
                .toList();

        return createApiError(
                HttpStatus.BAD_REQUEST,
                "Ошибка прескоринга",
                errors
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatusExceptions(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                createApiError(
                        e.getStatusCode(),
                        e.getMessage(),
                        e.getReason() != null ? List.of(e.getReason()) : List.of()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception e) {
        ApiError apiError = createApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера",
                List.of()
        );

        log.error("INTERNAL SERVER ERROR [id: {}]: {}", apiError.id, e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
