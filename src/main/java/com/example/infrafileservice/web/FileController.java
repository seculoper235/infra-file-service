package com.example.infrafileservice.web;

import com.example.infrafileservice.service.FileReference;
import com.example.infrafileservice.service.FileService;
import com.example.infrafileservice.web.exception.model.EntityNotFoundException;
import com.example.infrafileservice.web.exception.model.PutObjectException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    ) throws EntityNotFoundException {
        List<FileReference> result = fileService.mapping(mappingRequest.mappedBy(), mappingRequest.files())
                .getOrElseThrow(it -> it);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteByMapping(
            @RequestParam(value = "mapping") String mapping
    ) {
        fileService.delete(mapping);

        return ResponseEntity.noContent().build();
    }
}
