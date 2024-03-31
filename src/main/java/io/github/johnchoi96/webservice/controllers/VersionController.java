package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.models.AppVersion;
import io.github.johnchoi96.webservice.services.VersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/version")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Version")
public class VersionController {

    private final VersionService versionService;

    @GetMapping(value = "/app-version")
    @Operation(summary = "Returns the latest app version for the appId.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid appId supplied",
                    content = {@Content(mediaType = "text/plain")}
            )
    })
    public ResponseEntity<?> getAppVersion(@RequestParam("appId") final String appId) {
        log.info("GET /api/version/app-version");
        final AppVersion appVersion = versionService.getLatestVersion(appId);
        if (appVersion == null)
            return ResponseEntity.badRequest().body("Invalid appId supplied");
        return ResponseEntity.ok(appVersion);
    }
}
