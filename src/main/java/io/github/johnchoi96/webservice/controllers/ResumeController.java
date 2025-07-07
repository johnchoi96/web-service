package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
import io.github.johnchoi96.webservice.services.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.http.HttpStatusCode;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/resume")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Resume")
public class ResumeController {

    private final ResumeService resumeService;

    private final AdminKeysProperties adminKeysProperties;

    @Operation(
            summary = "Download Resume as PDF",
            description = "Fetches the resume document and returns it as a PDF file.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "PDF file downloaded successfully",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_PDF_VALUE,
                                    schema = @Schema(type = "string", format = "binary")
                            )
                    ),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping(path = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadResume() {
        try {
            final byte[] resume = resumeService.getResume();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"resume.pdf\"")
                    .body(resume);
        } catch (final IOException e) {
            log.error("Unable to fetch resume", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(
            summary = "Triggers resume refresh on S3 bucket",
            description = "Fetches the resume document from Google Docs and uploads to S3 bucket.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Resume refreshed successfully"
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "500", description = "Resume did not refresh successfully")
            }
    )
    @PutMapping(value = "/refresh")
    public ResponseEntity<?> refreshResume(@Parameter(description = "admin key") @RequestParam final String key) {
        if (!adminKeysProperties.getAdminKey().equals(key)) {
            return ResponseEntity.status(HttpStatusCode.UNAUTHORIZED).body("Invalid admin key");
        }
        try {
            resumeService.refreshResume();
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
