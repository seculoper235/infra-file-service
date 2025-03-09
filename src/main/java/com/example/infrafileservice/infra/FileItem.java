package com.example.infrafileservice.infra;

import com.example.infrafileservice.model.File;
import com.example.infrafileservice.service.FileReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.type.NumericBooleanConverter;

@Entity
@Table(name = "\"FILE\"")
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class FileItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "content_type", length = 50)
    private String contentType;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private Long size;

    @Convert(converter = NumericBooleanConverter.class)
    @Builder.Default
    private Boolean used = false;

    public void changeUseStatus(Boolean used) {
        this.used = used;
    }

    public File toModel() {
        return new File(id, name, contentType, path, size, used);
    }

    public FileReference toReference() {
        return new FileReference(id, path);
    }
}
