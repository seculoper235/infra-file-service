package com.example.infrafileservice.service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record FileReference(
        String name,
        String path
) {
}
