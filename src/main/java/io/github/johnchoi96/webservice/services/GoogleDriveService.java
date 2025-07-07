package io.github.johnchoi96.webservice.services;

import com.google.api.services.drive.Drive;
import io.github.johnchoi96.webservice.properties.api.GoogleProperties;
import io.github.johnchoi96.webservice.properties.api.aws.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    public final Drive driveService;

    public final GoogleProperties googleProperties;

    public final S3Client s3Client;

    private final S3Properties s3Properties;

    /**
     * Exports the Resume Google Doc as a PDF.
     *
     * @return raw resume file in byte array
     * @throws IOException if the download fails
     */
    public byte[] downloadResume() throws IOException {
        // Download PDF into memory
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        driveService.files()
                .export(googleProperties.getResumeDocId(), "application/pdf")
                .executeMediaAndDownloadTo(outputStream);

        return outputStream.toByteArray();
    }
}
