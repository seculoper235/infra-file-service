package com.example.infrafileservice.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.Instant;
import java.util.UUID;

@JsonSerialize
public record File(
        UUID id,
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
