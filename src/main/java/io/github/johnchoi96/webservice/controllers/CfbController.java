package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.models.cfb.UpsetGame;
import io.github.johnchoi96.webservice.services.CfbService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/cfb")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "College Football Controller")
public class CfbController {

    private final CfbService cfbService;

    @GetMapping(value = "/upsets", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the list of upset matches in the current week.")
    public ResponseEntity<List<UpsetGame>> getCurrentUpsets() throws JsonProcessingException {
        log.info("GET /api/cfb/upsets");
        final List<UpsetGame> upsetGames = cfbService.collectUpsetGames(Instant.now());
        return ResponseEntity.ok(upsetGames);
    }

    @GetMapping(value = "/upsets/{timestamp}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the list of upset matches in the week based on given timestamp.")
    public ResponseEntity<?> getPastUpsets(@PathVariable String timestamp) throws JsonProcessingException {
        log.info("GET /api/cfb/upsets/{}", timestamp);
        try {
            // Parse the date string as LocalDate
            final LocalDate localDate = LocalDate.parse(timestamp);
            // Convert LocalDate to Instant (assuming midnight as the time)
            final Instant convertedTimestamp = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            final List<UpsetGame> upsetGames = cfbService.collectUpsetGames(convertedTimestamp);
            return ResponseEntity.ok(upsetGames);
        } catch (final DateTimeParseException e) {
            final String errorMessage = String.format("Invalid timestamp: %s\n"
                    + "Make sure timestamp is in this format: yyyy-MM-dd", timestamp);
            log.info(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }
    }
}
