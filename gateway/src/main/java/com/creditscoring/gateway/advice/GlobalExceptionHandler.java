package com.creditscoring.gateway.advice;

import com.creditscoring.gateway.dto.exception.ApiError;
import com.creditscoring.gateway.exception.ExternalApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Stream;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
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
                new ApiError(
                    HttpStatus.BAD_REQUEST,
                    "Ошибка прескоринга",
                    errors
                )
        );
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiError> handleStatusExceptions(ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(
                new ApiError(
                        e.getStatusCode(),
                        e.getReason(),
                        List.of()
                )
        );
    }

    @ExceptionHandler(ExternalApiException.class)
    public ResponseEntity<ApiError> handleExternalApiException(ExternalApiException e) {
        return ResponseEntity.status(e.getErr().status()).body(e.getErr());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception e) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Внутренняя ошибка сервера",
                List.of(e.getMessage())
        );

        log.error("INTERNAL SERVER ERROR [id: {}]: {}", apiError.id(), e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }
}
