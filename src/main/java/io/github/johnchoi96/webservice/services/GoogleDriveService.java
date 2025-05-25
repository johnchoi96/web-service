package io.github.johnchoi96.webservice.services;

import com.google.api.services.drive.Drive;
import io.github.johnchoi96.webservice.properties.api.GoogleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleDriveService {

    public final Drive driveService;

    public final GoogleProperties googleProperties;

    public Path downloadResume() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        driveService.files().export(googleProperties.getResumeDocId(), "application/pdf")
                .executeMediaAndDownloadTo(outputStream);

        return Files.createTempFile("doc-", ".pdf");
    }
}
