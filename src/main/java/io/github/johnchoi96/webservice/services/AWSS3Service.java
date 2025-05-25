package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.properties.api.aws.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class AWSS3Service {

    private final S3Properties s3Properties;

    private final S3Client s3Client;

    private final EmailService emailService;

    public void uploadResume(final Path path) {
        s3Client.putObject(PutObjectRequest.builder()
                .bucket(s3Properties.getResume().getBucketName())
                .key(s3Properties.getResume().getObjectName())
                .contentType("application/pdf")
                .build(), path);
    }

    public byte[] getResumeObject() throws IOException {
        final GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Properties.getResume().getBucketName())
                .key(s3Properties.getResume().getObjectName())
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest)) {
            return s3Object.readAllBytes();
        } catch (final IOException e) {
            log.error("Unable to fetch resume object from S3", e);
            emailService.notifyException(e);
            throw e;
        }
    }
}
