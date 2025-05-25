package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.services.ResumeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/resume")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Resume")
public class ResumeController {

    private final ResumeService resumeService;

    @GetMapping(path = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadResume() {
        try {
            final byte[] resume = resumeService.getResume();
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"resume.pdf\"")
                    .body(resume);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
