package com.creditscoring.dossier.advice;

import com.creditscoring.dossier.exception.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ApiError createApiError(HttpStatusCode status, String message, List<String> details) {
        return new ApiError(UUID.randomUUID(), status.value(), message, details, Instant.now());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatusExceptions(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                createApiError(
                        e.getStatusCode(),
                        e.getReason(),
                        List.of()
            )
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception e) {
        ApiError apiError = createApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера",
                List.of(e.getMessage())
        );

        log.error("INTERNAL SERVER ERROR [id: {}]: {}", apiError.id(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
