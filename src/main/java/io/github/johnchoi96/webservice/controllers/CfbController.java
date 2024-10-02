package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.models.cfb.api_response.CfbUpsetMatchResponse;
import io.github.johnchoi96.webservice.services.CfbService;
import io.github.johnchoi96.webservice.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping(value = "/api/cfb")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "College Football", description = "Endpoints could take around 2 minutes to generate a response.")
public class CfbController {

    private final CfbService cfbService;

    private final EmailService emailService;

    @GetMapping(value = "/upsets", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the list of upset matches in the current week.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Week is not a CFB season.",
                    content = {@Content(mediaType = "text/plain")}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Week and Year must be supplied",
                    content = {@Content(mediaType = "text/plain")}
            )
    })
    public ResponseEntity<?> getCurrentUpsets(
            @Parameter(description = "Optional week of CFB season") @RequestParam(required = false) final Integer week,
            @Parameter(description = "Optional year of CFB season") @RequestParam(required = false) final Integer year) {
        log.info("GET /api/cfb/upsets");
        try {
            CfbUpsetMatchResponse upsetGames;
            if (week != null || year != null) {
                if (week == null || year == null) {
                    return ResponseEntity.badRequest().body("Week and Year must be supplied");
                }
                upsetGames = cfbService.getCfbUpsetMatches(week, year);
            } else {
                upsetGames = cfbService.getCfbUpsetMatches(Instant.now());
            }
            if (upsetGames == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(upsetGames);
            }
        } catch (JsonProcessingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/upsets/{timestamp}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the list of upset matches in the week based on given timestamp.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = {@Content(mediaType = "application/json")}
            ),
            @ApiResponse(
                    responseCode = "204",
                    description = "Timestamp is not a CFB season.",
                    content = {@Content(mediaType = "text/plain")}
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid timestamp format",
                    content = {@Content(mediaType = "text/plain")}
            )
    })
    public ResponseEntity<?> getPastUpsets(
            @Parameter(description = "in yyyy-MM-dd format") @PathVariable String timestamp) {
        log.info("GET /api/cfb/upsets/{}", timestamp);
        try {
            // Parse the date string as LocalDate
            final LocalDate localDate = LocalDate.parse(timestamp);
            if (localDate.isAfter(LocalDate.now())) {
                final String errorMessage = "Cannot take a future date";
                return ResponseEntity.badRequest().body(errorMessage);
            }
            // Convert LocalDate to Instant (assuming midnight as the time)
            final Instant convertedTimestamp = localDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
            final CfbUpsetMatchResponse upsetGames = cfbService.getCfbUpsetMatches(convertedTimestamp);
            if (upsetGames == null) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.ok(upsetGames);
            }
        } catch (final DateTimeParseException e) {
            final String errorMessage = String.format("Invalid timestamp: %s\n"
                    + "Make sure timestamp is in this format: yyyy-MM-dd", timestamp);
            log.info(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } catch (JsonProcessingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
