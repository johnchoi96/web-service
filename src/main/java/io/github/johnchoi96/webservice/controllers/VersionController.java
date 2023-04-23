package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.models.AppVersion;
import io.github.johnchoi96.webservice.services.VersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/version")
@Slf4j
public class VersionController {

    @Autowired
    private VersionService versionService;

    @GetMapping(value = "/app", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AppVersion> getAppVersion(@RequestParam("appName") final String appName) {
        log.info("GET /api/version/app");
        final AppVersion appVersion = versionService.getLatestVersion(appName);
        if (appVersion == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(appVersion);
    }
}
