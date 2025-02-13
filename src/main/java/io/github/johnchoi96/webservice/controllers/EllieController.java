package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.models.ellie.ElliePayload;
import io.github.johnchoi96.webservice.services.EllieService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestController
@RequestMapping(value = "/api/ellie")
@RequiredArgsConstructor
@Slf4j
@Hidden
public class EllieController {

    private final EllieService service;

    private final Environment environment;

    @GetMapping(value = "/check")
    public ResponseEntity<Boolean> checkPassword(@RequestParam final String password) {
        if (!isLocal() && !isAfterValentines()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.checkPassword(password));
    }

    @GetMapping(value = "/payload")
    public ResponseEntity<ElliePayload> getPayload() {
        if (!isLocal() && !isAfterValentines()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(service.getPayload());
    }

    private boolean isAfterValentines() {
        final ZonedDateTime targetDate = ZonedDateTime.of(
                2025, 2, 14, 17, 0, 0, 0,
                ZoneId.of("America/New_York")
        );
        final ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/New_York"));
        return now.isAfter(targetDate);
    }

    private boolean isLocal() {
        return environment.acceptsProfiles(Profiles.of("local"));
    }
}
