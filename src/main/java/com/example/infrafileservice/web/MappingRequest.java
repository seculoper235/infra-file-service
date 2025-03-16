package com.example.infrafileservice.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;
import java.util.UUID;

@JsonSerialize
public record MappingRequest(
        String mappedBy,
        List<UUID> files
) {
}
