package com.example.infrafileservice.infra;

import com.example.infrafileservice.model.File;
import com.example.infrafileservice.model.FileStatus;
import com.example.infrafileservice.service.FileReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "\"FILE\"")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    private FileStatus status = FileStatus.TEMP;

    @Column(name = "mapped_by", length = 40)
    private String mappedBy;

    public void changeStatus(FileStatus status) {
        this.status = status;
    }

    public File toModel() {
        return new File(id, name, contentType, path, size, status, mappedBy);
    }

    public FileReference toReference() {
        return new FileReference(name, path);
    }
}
