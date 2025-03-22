package com.example.infrafileservice.service;

import com.example.infrafileservice.domain.S3Provider;
import com.example.infrafileservice.infra.FileItem;
import com.example.infrafileservice.infra.FileRepository;
import com.example.infrafileservice.model.FileStatus;
import com.example.infrafileservice.web.exception.model.EntityNotFoundException;
import com.example.infrafileservice.web.exception.model.PutObjectException;
import io.vavr.control.Either;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static com.example.infrafileservice.domain.S3Provider.BUCKET_NAME;
import static org.jooq.generated.Tables.FILE;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final DSLContext dslContext;
    private final S3Provider s3Provider;

    @Transactional
    public Either<PutObjectException, FileReference> upload(
            String path,
            MultipartFile multipartFile
    ) throws IOException {
        UUID id = UUID.randomUUID();
        String key = path + "/" + id;

        PutObjectResponse response = s3Provider.createObject(BUCKET_NAME, key, multipartFile);

        if (response.sdkHttpResponse().isSuccessful()) {
            log.info("파일 업로드를 완료하였습니다: {}", id);

            FileItem fileItem = FileItem.builder()
                    .id(id)
                    .name(multipartFile.getName())
                    .contentType(multipartFile.getContentType())
                    .path(key)
                    .size(multipartFile.getSize())
                    .build();

            return Either.right(fileRepository.save(fileItem).toReference());
        } else {
            return Either.left(new PutObjectException("PutObject 도중에 문제가 발생하였습니다"));
        }
    }

    /**
     * 매핑 관계를 재설정합니다
     *
     * @param mappedBy 연관관계의 주인 데이터의 id
     * @param files    연관관계로 등록할 TEMP 상태의 파일 id
     * @throws NoSuchElementException 매개변수 <b>files</b> 에 해당하는 데이터가 없을 경우 발생
     */
    @Transactional
    public Either<EntityNotFoundException, List<FileReference>> mapping(
            String mappedBy,
            List<UUID> files
    ) {
        List<FileItem> fileItemList = fileRepository.findAllById(files);

        if (files.size() != fileItemList.size()) {
            return Either.left(new EntityNotFoundException("파일이 존재하지 않습니다"));
        } else {
            dslContext.update(FILE)
                    .set(FILE.STATUS, FileStatus.DELETE.name())
                    .where(FILE.MAPPED_BY.eq(mappedBy))
                    .execute();

            List<FileReference> result = fileItemList.stream()
                    .peek(item -> {
                        item.mapping(mappedBy);
                        item.changeStatus(FileStatus.USE);
                    })
                    .map(FileItem::toReference)
                    .toList();

            return Either.right(result);
        }
    }

    public void delete(
            String mappedBy
    ) {
        dslContext.update(FILE)
                .set(FILE.STATUS, FileStatus.DELETE.name())
                .where(FILE.MAPPED_BY.eq(mappedBy))
                .execute();

        log.info("파일이 삭제처리 되었습니다: mappedBy {}", mappedBy);
    }
}
