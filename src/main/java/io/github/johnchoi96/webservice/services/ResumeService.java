package io.github.johnchoi96.webservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final AWSS3Service s3Service;

    private final GoogleDriveService googleDriveService;

    private final EmailService emailService;

    private final S3Client s3Client;

    public void refreshResume() {
        try {
            // try download the resume doc
            final Path tempFilePath = googleDriveService.downloadResume();
            // try to upload to S3
            s3Service.uploadResume(tempFilePath);
            Files.deleteIfExists(tempFilePath);
        } catch (final IOException e) {
            log.error("Could not update the resume doc");
            emailService.notifyException(e);
        }
    }

    public byte[] getResume() throws IOException {
        return s3Service.getResumeObject();
    }
}
