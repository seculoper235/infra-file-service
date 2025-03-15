package com.example.infrafileservice.infra;

import com.example.infrafileservice.environment.JooqIntegrationTest;
import com.example.infrafileservice.model.FileStatus;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.FileRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jooq.generated.Tables.FILE;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileDSLTest extends JooqIntegrationTest {
    @Autowired
    DSLContext dslContext;

    @BeforeEach
    void setup() {
        dslContext.insertInto(FILE,
                FILE.ID,
                FILE.NAME,
                FILE.CONTENT_TYPE,
                FILE.PATH,
                FILE.SIZE,
                FILE.STATUS,
                FILE.CREATED_AT,
                FILE.UPDATED_AT
        ).values(
                "619a3167-428a-43a4-b246-c0462286d962",
                "temp-file",
                "text/plain",
                "/post/image/619a3167-428a-43a4-b246-c0462286d962",
                1000L,
                "TEMP",
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().minusMinutes(10)
        ).values(
                "619a3167-428a-43a4-b246-c0462286d963",
                "delete-file",
                "text/plain",
                "/post/image/619a3167-428a-43a4-b246-c0462286d963",
                1000L,
                "DELETE",
                LocalDateTime.now().minusMinutes(10),
                LocalDateTime.now().minusMinutes(10)
        ).execute();
    }

    @Test
    @DisplayName("특정 시간 내의 특정 Status를 업데이트한다")
    public void update_status_specific_time_and_status() {
        LocalDateTime now = LocalDateTime.now();

        List<String> num = dslContext.update(FILE)
                .set(FILE.STATUS, FileStatus.DELETE.name())
                .where(FILE.STATUS.eq(FileStatus.TEMP.name())
                        .and(FILE.UPDATED_AT.lessThan(now.minusMinutes(3)))
                )
                .returning()
                .stream().map(FileRecord::getStatus).toList();

        assertTrue(num.stream().allMatch(s -> Objects.equals(s, FileStatus.DELETE.name())));
    }

    @Test
    @DisplayName("특정 시간 내의 특정 Status인 데이터를 삭제한다")
    public void delete_data_specific_time_and_status() {
        LocalDateTime now = LocalDateTime.now();

        List<String> num = dslContext.delete(FILE)
                .where(FILE.STATUS.eq(FileStatus.DELETE.name())
                        .and(FILE.UPDATED_AT.lessThan(now.minusMinutes(1)))
                )
                .returning()
                .stream().map(FileRecord::getStatus).toList();

        long count = dslContext.selectFrom(FILE).stream().count();

        assertThat(num.size()).isEqualTo(1);
        assertThat(count).isEqualTo(1);
    }
}
