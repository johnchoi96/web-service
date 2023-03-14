package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.models.AppVersion;
import io.github.johnchoi96.webservice.services.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/version")
public class VersionController {

    @Autowired
    private VersionService versionService;

    @GetMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppVersion> getAppVersion(@RequestParam("bundleId") final String bundleId) throws IOException {
        final AppVersion appVersion = versionService.getLatestVersion(bundleId);
        if (appVersion == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        return ResponseEntity.ok(appVersion);
    }
}
