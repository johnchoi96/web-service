package com.johnchoi96.webservice.controllers;

import com.johnchoi96.webservice.models.app_distribution.response.AppMetadataResponse;
import com.johnchoi96.webservice.services.AppDistributionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/app-distribution")
@RequiredArgsConstructor
@Slf4j
public class AppDistributionController {

    private final AppDistributionService appDistributionService;

    @GetMapping(value = "/apps", produces = MediaType.APPLICATION_JSON_VALUE)
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
}
