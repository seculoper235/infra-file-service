package com.example.infrafileservice.web.exception.model;

import lombok.Getter;

@Getter
public enum ExceptionStatus {
    FS001("Entity Not Found"),
    FS002("S3 PutObject Exception"),
    FS003("S3 Exception");

    private final String label;

    ExceptionStatus(String label) {
        this.label = label;
    }
}
