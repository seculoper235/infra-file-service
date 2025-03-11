package com.example.infrafileservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record File(
        Long id,
        String name,
        String contentType,
        String path,
        Long size,
        FileStatus status,
        String mappedBy
) {
}
