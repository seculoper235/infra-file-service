package com.example.infrafileservice.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

@JsonSerialize
public record MappingRequest(
        String mappedBy,
        List<String> files
) {
}
