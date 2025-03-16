package com.example.infrafileservice.infra;

import com.example.infrafileservice.environment.JpaIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class FileRepositoryTest extends JpaIntegrationTest {
    @Autowired
    FileRepository fileRepository;

    @Test
    @DisplayName("UUID 조회 테스트")
    void uuid_find_test() {
        UUID id1 = UUID.fromString("619a3167-428a-43a4-b246-c0462286d962");
        UUID id2 = UUID.fromString("619a3167-428a-43a4-b246-c0462286d963");
        List<UUID> params = List.of(id1, id2);

        assertDoesNotThrow(() -> {
            List<FileItem> result = fileRepository.findAllById(params);
            assertThat(result.size()).isEqualTo(0);
        });
    }
}
