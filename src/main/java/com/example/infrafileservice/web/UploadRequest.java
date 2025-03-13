package com.example.infrafileservice.web;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public record UploadRequest(
        String path
) {
}
