package com.example.infrafileservice.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record FileReference(
        String id,
        String name,
        String path
) {
}
