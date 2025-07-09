package com.johnchoi96.webservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeService {

    private final AWSS3Service s3Service;

    private final GoogleDriveService googleDriveService;

    private final S3Client s3Client;

    public void refreshResume() throws IOException {
        try {
            // get the byte array of the resume
            final byte[] resumeFile = googleDriveService.downloadResume();
            // upload to S3
            s3Service.uploadResume(resumeFile);
        } catch (final IOException e) {
            log.error("Could not update the resume doc", e);
            throw e;
        }
    }

    public byte[] getResume() throws IOException {
        return s3Service.getResumeObject();
    }
}
