package com.example.infrafileservice.infra;

import com.example.infrafileservice.model.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileItem, UUID> {
    @Query("update FileItem f set f.status = :status where f.mappedBy = :mappedBy")
    @Modifying
    void updateStatusByMappedBy(FileStatus status, String mappedBy);
}
