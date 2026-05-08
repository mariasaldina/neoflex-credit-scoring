package com.creditscoring.gateway.exception;

import lombok.Getter;

@Getter
public class ExternalApiException extends RuntimeException {

    private final ApiError err;

    public ExternalApiException(ApiError err) {
        this.err = err;
    }
}
