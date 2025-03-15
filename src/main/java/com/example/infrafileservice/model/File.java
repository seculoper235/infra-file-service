package com.example.infrafileservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;

@JsonSerialize
public record File(
        String id,
        String name,
        String contentType,
        String path,
        Long size,
        FileStatus status,
        String mappedBy,
        Instant createdAt,
        Instant updatedAt
) {
}
