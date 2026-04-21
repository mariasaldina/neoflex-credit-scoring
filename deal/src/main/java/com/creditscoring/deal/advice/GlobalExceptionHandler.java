package com.creditscoring.deal.advice;

import com.creditscoring.deal.exception.ApplicationStatusConflictException;
import com.creditscoring.deal.exception.InvalidSesCodeException;
import com.creditscoring.deal.exception.StatementNotFoundException;
import com.creditscoring.deal.exception.dto.ApiError;
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

    private ApiError createApiError(HttpStatusCode status, String message, List<String> details) {
        return new ApiError(UUID.randomUUID(), status.value(), message, details, Instant.now());
    }

    private ApiError createApiError(HttpStatusCode status, String message) {
        return new ApiError(UUID.randomUUID(), status.value(), message, List.of(), Instant.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException e) {
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                createApiError(
                    HttpStatus.BAD_REQUEST,
                    "Ошибка валидации",
                    errors
                )
        );
    }

    @ExceptionHandler(StatementNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(createApiError(HttpStatus.NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(ApplicationStatusConflictException.class)
    public ResponseEntity<ApiError> handleConflictExceptions(Exception e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(createApiError(HttpStatus.CONFLICT, e.getMessage()));
    }

    @ExceptionHandler(InvalidSesCodeException.class)
    public ResponseEntity<ApiError> handleInvalidSesCodeException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(createApiError(HttpStatus.FORBIDDEN, e.getMessage()));
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
