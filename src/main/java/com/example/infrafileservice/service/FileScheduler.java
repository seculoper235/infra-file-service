package com.example.infrafileservice.service;

import com.example.infrafileservice.domain.S3Provider;
import com.example.infrafileservice.model.FileStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.generated.tables.records.FileRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.infrafileservice.domain.S3Provider.BUCKET_NAME;
import static org.jooq.generated.Tables.FILE;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileScheduler {
    private final DSLContext dslContext;
    private final S3Provider s3Provider;

    @Transactional
    @Scheduled(cron = "${spring.task.scheduling.temp}")
    public void changeToDelete() {
        LocalDateTime now = LocalDateTime.now();

        int num = dslContext.update(FILE)
                .set(FILE.STATUS, FileStatus.DELETE.name())
                .where(FILE.STATUS.eq(FileStatus.TEMP.name())
                        .and(FILE.UPDATED_AT.lessThan(now.minusDays(3)))
                )
                .execute();

        log.info("파일이 삭제처리 되었습니다: {} rows", num);
    }

    @Transactional
    @Scheduled(cron = "${spring.task.scheduling.delete}")
    public void deleteFile() {
        LocalDateTime now = LocalDateTime.now();

        List<String> pathList = dslContext.delete(FILE)
                .where(FILE.STATUS.eq(FileStatus.DELETE.name())
                        .and(FILE.UPDATED_AT.lessThan(now.minusDays(1)))
                )
                .returning()
                .stream().map(FileRecord::getStatus).toList();

        s3Provider.deleteObjects(BUCKET_NAME, pathList);

        log.info("파일이 완전히 삭제되었습니다: {} rows", pathList.size());
    }
}
