package com.example.infrafileservice.service;

import com.example.infrafileservice.domain.S3Provider;
import com.example.infrafileservice.infra.FileItem;
import com.example.infrafileservice.infra.FileRepository;
import com.example.infrafileservice.model.FileStatus;
import com.example.infrafileservice.web.exception.model.EntityNotFoundException;
import com.example.infrafileservice.web.exception.model.PutObjectException;
import io.vavr.control.Either;
import org.jooq.*;
import org.jooq.generated.tables.records.FileRecord;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    @InjectMocks
    FileService fileService;

    S3Provider s3Provider = mock(S3Provider.class);

    DSLContext dslContext = mock(DSLContext.class);
    UpdateSetFirstStep<FileRecord> firstStep = mock(UpdateSetFirstStep.class);
    UpdateSetMoreStep<FileRecord> moreStep = mock(UpdateSetMoreStep.class);
    UpdateConditionStep<FileRecord> conditionStep = mock(UpdateConditionStep.class);

    FileRepository fileRepository = mock(FileRepository.class);

    List<FileItem> files = List.of(FileItem.builder()
                    .id(UUID.randomUUID())
                    .name("test1")
                    .contentType("text/plain")
                    .path("post/image/test1.txt")
                    .size(20L)
                    .status(FileStatus.DELETE)
                    .build(),
            FileItem.builder()
                    .id(UUID.randomUUID())
                    .name("test2")
                    .contentType("text/plain")
                    .path("post/image/test2.txt")
                    .size(20L)
                    .status(FileStatus.USE)
                    .mappedBy("post1")
                    .build()
    );

    @Test
    @DisplayName("파일 업로드 시, S3에 파일이 업로드가 성공했다면 파일 정보를 반환한다")
    public void upload_file_success_s3_return_file_info() throws IOException {
        String path = "post/image";

        MockMultipartFile file = new MockMultipartFile(
                "test",
                "test.txt",
                "text/plain",
                "File service test!".getBytes()
        );

        FileItem expected = FileItem.builder()
                .id(UUID.randomUUID())
                .name(file.getName())
                .contentType(file.getContentType())
                .path("path")
                .size(file.getSize())
                .build();

        PutObjectResponse successResponse = (PutObjectResponse) PutObjectResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(200).build())
                .build();

        given(s3Provider.createObject(any(), any(), any())).willReturn(successResponse);
        given(fileRepository.save(any())).willReturn(expected);

        Either<PutObjectException, FileReference> result = fileService.upload(path, file);

        assertTrue(result.isRight());
    }

    @Test
    @DisplayName("파일 업로드 시, S3에 파일이 업로드가 실패했다면 에러를 던진다")
    public void upload_file_fail_s3_upload_return_error() throws IOException {
        String path = "post/image";

        MockMultipartFile file = new MockMultipartFile(
                "test",
                "test.txt",
                "text/plain",
                "File service test!".getBytes()
        );

        PutObjectResponse errorResponse = (PutObjectResponse) PutObjectResponse.builder()
                .sdkHttpResponse(SdkHttpResponse.builder().statusCode(400).build())
                .build();

        given(s3Provider.createObject(any(), any(), any())).willReturn(errorResponse);

        Either<PutObjectException, FileReference> result = fileService.upload(path, file);

        assertTrue(result.isLeft());
        assertThrows(PutObjectException.class,
                () -> result.getOrElseThrow(it -> it));
    }

    // FIXME: JOOQ @MockitoBean 테스트 구현방법 고민 필요
    @Test
    @DisplayName("파일 매핑 시, 해당 파일이 존재하지 않는다면 에러를 던진다")
    public void mapping_file_not_exist_files_throw_error() {
        String mappedBy = "post1";
        List<FileItem> temp = List.of(files.get(0));
        List<UUID> expected = temp.stream().map(item -> item.toModel().id()).toList();

        given(fileRepository.findAllById(any())).willReturn(Collections.emptyList());

        Either<EntityNotFoundException, List<FileReference>> references = fileService.mapping(mappedBy, expected);

        assertTrue(references.isLeft());
        assertThrows(EntityNotFoundException.class,
                () -> references.getOrElseThrow(it -> it));
    }

    @Test
    @DisplayName("파일 매핑 시, 해당 파일이 존재한다면 매핑된 파일들을 반환한다")
    public void mapping_file_exist_files_return_update_file_info() {
        String mappedBy = "post1";
        List<FileItem> temp = List.of(files.get(0));
        List<UUID> expected = temp.stream().map(item -> item.toModel().id()).toList();

        given(fileRepository.findAllById(any())).willReturn(temp);
        when(dslContext.update(any(Table.class))).thenReturn(firstStep);
        when(firstStep.set(any(Field.class), anyString())).thenReturn(moreStep);
        when(moreStep.where(any(Condition.class))).thenReturn(conditionStep);
        when(conditionStep.execute()).thenReturn(1);

        given(fileRepository.findAllById(any())).willReturn(temp);

        Either<EntityNotFoundException, List<FileReference>> references = fileService.mapping(mappedBy, expected);

        assertTrue(references.isRight());
        assertThat(references.get().stream().map(FileReference::id).toList()).isEqualTo(expected);
    }
}
