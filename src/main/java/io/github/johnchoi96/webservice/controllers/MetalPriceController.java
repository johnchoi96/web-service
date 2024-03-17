package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
import io.github.johnchoi96.webservice.services.EmailService;
import io.github.johnchoi96.webservice.services.MetalPriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping(value = "/api/metal-price")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "MetalPrice")
public class MetalPriceController {

    private final MetalPriceService metalPriceService;

    private final AdminKeysProperties adminKeysProperties;

    private final EmailService emailService;

    @GetMapping(value = "/trigger-report", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Triggers a gold price report and sends notification.")
    public ResponseEntity<String> metalPrice(
            @Parameter(description = "admin key") @RequestParam final String key) {
        log.info("GET /api/metal-price/trigger-report");
        try {
            if (!adminKeysProperties.getAdminKey().equals(key)) {
                return ResponseEntity.badRequest().body("Invalid admin key");
            }
            metalPriceService.analyzeGoldPriceAndNotify(LocalDate.now());
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException | FirebaseMessagingException e) {
            log.error(e.getLocalizedMessage());
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
