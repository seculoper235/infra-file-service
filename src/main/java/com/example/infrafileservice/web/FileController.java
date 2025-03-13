package com.example.infrafileservice.web;

import com.example.infrafileservice.service.FileReference;
import com.example.infrafileservice.service.FileService;
import com.example.infrafileservice.web.exception.PutObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileReference> upload(
            @RequestPart UploadRequest uploadRequest,
            @RequestPart(name = "multipartFile") MultipartFile multipartFile
    ) throws IOException, PutObjectException {
        FileReference result = fileService.upload(uploadRequest.path(), multipartFile)
                .getOrElseThrow(it -> it);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/mapping")
    public ResponseEntity<List<FileReference>> mapping(
            @RequestBody MappingRequest mappingRequest
            ) {
        String mapping = mappingRequest.mappedBy();
        List<UUID> files = mappingRequest.files().stream().map(UUID::fromString).toList();

        List<FileReference> result = fileService.mapping(mapping, files);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{mapping}")
    public ResponseEntity<?> deleteByMapping(
            @PathVariable String mapping
    ) {
        fileService.delete(mapping);

        return ResponseEntity.noContent().build();
    }
}
