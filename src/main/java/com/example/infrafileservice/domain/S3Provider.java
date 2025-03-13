package com.example.infrafileservice.domain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class S3Provider {
    private final S3Client s3Client;

    public S3Provider(S3Client s3Client) {
        this.s3Client = s3Client;

        this.s3Client.createBucket(CreateBucketRequest.builder().bucket("file-bucket").build());
    }

    public PutObjectResponse createObject(String bucket, String key, MultipartFile multipartFile) throws IOException {
        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .acl(ObjectCannedACL.BUCKET_OWNER_FULL_CONTROL)
                .key(key)
                .bucketKeyEnabled(true)
                .build();

        RequestBody requestBody = RequestBody
                .fromInputStream(multipartFile.getInputStream(), multipartFile.getSize());

        return s3Client.putObject(request, requestBody);
    }

    public GetObjectResponse getObject(String bucket, String key) {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        return s3Client.getObject(request).response();
    }

    public DeleteObjectsResponse deleteObjects(String bucket, List<String> keyList) {
        List<ObjectIdentifier> identifiers = keyList.stream()
                .map(key -> ObjectIdentifier.builder().key(key).build())
                .toList();

        return s3Client.deleteObjects(DeleteObjectsRequest.builder()
                .bucket(bucket).delete(
                        Delete.builder().objects(identifiers).build()
                ).build());
    }
}
