package com.creditscoring.gateway.exception;

import com.creditscoring.gateway.dto.exception.ApiError;
import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

    private final ApiError err;

    public ExternalApiException(ApiError err) {
        this.err = err;
    }
}
