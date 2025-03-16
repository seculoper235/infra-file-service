package com.example.infrafileservice.api;

import com.example.infrafileservice.service.FileReference;
import com.example.infrafileservice.service.FileService;
import com.example.infrafileservice.web.FileController;
import com.example.infrafileservice.web.UploadRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(FileController.class)
public class FileControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    private FileService fileService;

    @Autowired
    protected ObjectMapper objectMapper;

    @Test
    @DisplayName("파일 업로드 시, FileTable에 데이터가 저장되고 S3에 파일이 업로드 된다")
    public void upload_file_save_table_upload_s3() throws Exception {
        FileReference expected = new FileReference(UUID.randomUUID(), "file1", "/post/image/file1.txt");

        MockMultipartFile file = new MockMultipartFile(
                "multipartFile",
                "test1.txt",
                "text/plain",
                "Post file service!".getBytes()
        );

        UploadRequest request = new UploadRequest("post/image");
        String content = objectMapper.writeValueAsString(request);

        MockMultipartFile uploadRequest = new MockMultipartFile(
                "uploadRequest",
                "uploadRequest",
                "application/json",
                content.getBytes(StandardCharsets.UTF_8));

        given(fileService.upload(any(), any())).willReturn(Either.right(expected));

        mockMvc.perform(multipart(HttpMethod.POST, "/api/file/upload")
                        .file(uploadRequest)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expected.id().toString())));
    }
}
