package com.example.infrafileservice.web.exception.handler;

import com.example.infrafileservice.web.exception.model.ExceptionStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@JsonSerialize
@Getter
@Builder
@RequiredArgsConstructor
public class ExceptionResponse {

    private final String timestamp;

    private final String code;

    private final String message;

    private final String detail;

    public ExceptionResponse(String timestamp, ExceptionStatus status, String detail) {
        this.timestamp = timestamp;
        this.code = status.name();
        this.message = status.getLabel();
        this.detail = detail;
    }
}
