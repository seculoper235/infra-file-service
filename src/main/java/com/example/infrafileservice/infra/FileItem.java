package com.example.infrafileservice.infra;

import com.example.infrafileservice.model.File;
import com.example.infrafileservice.model.FileStatus;
import com.example.infrafileservice.service.FileReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "\"FILE\"")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileItem {
    @Id
    private UUID id;

    @Column(nullable = false, length = 40)
    private String name;

    @Column(nullable = false, name = "content_type", length = 50)
    private String contentType;

    @Column(nullable = false, length = 100)
    private String path;

    @Column(nullable = false)
    private Long size;

    @Enumerated(EnumType.STRING)
    @Column(length = 10, nullable = false)
    @Builder.Default
    private FileStatus status = FileStatus.TEMP;

    @Column(name = "mapped_by", length = 40)
    private String mappedBy;

    public void changeStatus(FileStatus status) {
        this.status = status;
    }

    public void mapping(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    public File toModel() {
        return new File(id.toString(), name, contentType, path, size, status, mappedBy);
    }

    public FileReference toReference() {
        return new FileReference(id.toString(), name, path);
    }
}
