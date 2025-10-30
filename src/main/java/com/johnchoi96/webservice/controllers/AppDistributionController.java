package com.johnchoi96.webservice.controllers;

import com.johnchoi96.webservice.models.app_distribution.response.AppMetadataResponse;
import com.johnchoi96.webservice.services.AppDistributionService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/app-distribution")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "App Distribution", description = "APIs related to app distribution.")
public class AppDistributionController {

    private final AppDistributionService appDistributionService;

    @Hidden
    @GetMapping(value = "/apps")
    public ResponseEntity<?> getAppsForUser(@AuthenticationPrincipal final Jwt jwt) {
        log.info("GET /api/app-distribution/apps");
        try {
            final String email = jwt.getClaimAsString("email");
            final AppMetadataResponse response = appDistributionService.getAppsForUser(email);
            return ResponseEntity.ok(response);
        } catch (final IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (final Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(
            summary = "Upload an IPA file for distribution",
            security = {@SecurityRequirement(name = "basicAuth")},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "IPA uploaded successfully"
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid ID passed"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadIpaFile(
            @RequestParam final String appId,
            @RequestParam final String newVersion,
            @Parameter(description = "The .ipa file to upload")
            @RequestPart("file") final MultipartFile file) {
        log.info("POST /api/app-distribution/upload");
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty.");
            }
            if (file.getOriginalFilename() == null || !file.getOriginalFilename().endsWith(".ipa")) {
                return ResponseEntity.badRequest().body("IPA file required.");
            }
            if (appId.isEmpty() || newVersion.isEmpty()) {
                return ResponseEntity.badRequest().body("App ID and version are required.");
            }

            appDistributionService.uploadIpa(appId, newVersion, file);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (final IOException e) {
            log.error("Error while uploading to S3", e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        } catch (final IllegalArgumentException e) {
            log.error("Invalid app ID passed.", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
