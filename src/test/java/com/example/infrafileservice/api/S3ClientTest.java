package com.example.infrafileservice.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;


@Testcontainers
public class S3ClientTest {
    @Container
    public LocalStackContainer localstack = new LocalStackContainer(DockerImageName.parse("localstack/localstack:latest"))
            .withServices(LocalStackContainer.Service.S3);

    public S3Client s3;

    private static final String BUCKET_NAME = "file-bucket";

    @BeforeEach
    public void setup() {
        s3 = S3Client.builder()
                .endpointOverride(localstack.getEndpointOverride(LocalStackContainer.Service.S3))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey()))
                ).region(Region.of(localstack.getRegion()))
                .build();

        s3.createBucket(CreateBucketRequest.builder().bucket(BUCKET_NAME).build());
    }

    @Test
    @DisplayName("오브젝트 생성 테스트")
    public void upload_file_with_S3Client() throws IOException {
        String path = "post/image";
        String name = "test1.txt";

        MockMultipartFile file = new MockMultipartFile(
                "test1",
                name,
                "text/plain",
                "Post file service!".getBytes()
        );

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
                .key(path + "/" + name)
                .bucketKeyEnabled(true)
                .build();

        RequestBody requestBody = RequestBody
                .fromInputStream(file.getInputStream(), file.getSize());

        PutObjectResponse result = s3.putObject(request, requestBody);

        assertTrue(result.sdkHttpResponse().isSuccessful());
    }

    @Test
    @DisplayName("파일 삭제 시, 아무것도 반환하지 않는다")
    public void delete_file_with_S3Client() throws IOException {
        String path = "post/image";
        String name = "test1.txt";

        MockMultipartFile file = new MockMultipartFile(
                "test1",
                name,
                "text/plain",
                "Post file service!".getBytes()
        );

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
                .key(path + "/" + name)
                .bucketKeyEnabled(true)
                .build();

        RequestBody requestBody = RequestBody
                .fromInputStream(file.getInputStream(), file.getSize());

        s3.putObject(request, requestBody);

        ObjectIdentifier identifier = ObjectIdentifier.builder().key(path + "/" + name).build();

        s3.deleteObjects(DeleteObjectsRequest.builder()
                .bucket(BUCKET_NAME).delete(
                        Delete.builder().objects(identifier).build()
                ).build());
    }
}
