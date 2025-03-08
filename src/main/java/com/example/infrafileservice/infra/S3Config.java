package com.example.infrafileservice.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3Config {
    @Bean
    public S3Client s3Client(
    ) {
        return S3Client.builder()
                .region(Region.of("ap-northeast-2"))
                .endpointOverride(URI.create("http://localhost:4566"))
                // FIXME: https://github.com/aws/aws-sdk-java-v2/issues/3587
                .forcePathStyle(true)
                .build();
    }
}
