package com.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.johnchoi96.webservice.services.EmailService;
import com.johnchoi96.webservice.services.PetfinderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/petfinder")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Petfinder")
public class PetfinderController {

    private final PetfinderService petfinderService;

    private final EmailService emailService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a complete list of adoptable pets in 78727 area.")
    public ResponseEntity<?> findDogsNear78727() {
        log.info("GET /api/petfinder/all");
        try {
            return ResponseEntity.ok(petfinderService.findAllDogsNear78727());
        } catch (JsonProcessingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns a list of filtered list of adoptable pets in 78727 area.")
    public ResponseEntity<?> findFilteredDogsNear78727(
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) Boolean ignoreKnownPets) {
        log.info("GET /api/petfinder/filtered");
        try {
            if (ignoreKnownPets == null) {
                ignoreKnownPets = false;
            }
            return ResponseEntity.ok(petfinderService.findFilteredDogs(limit, ignoreKnownPets));
        } catch (JsonProcessingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
