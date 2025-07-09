package com.johnchoi96.webservice.services;

import com.google.api.services.drive.Drive;
import com.johnchoi96.webservice.properties.api.GoogleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    private final Drive driveService;

    private final GoogleProperties googleProperties;

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
