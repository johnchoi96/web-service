package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.services.PetfinderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/petfinder")
@Slf4j
public class PetfinderController {

    @Autowired
    private PetfinderService petfinderService;

    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findDogsNear43235() throws JsonProcessingException {
        log.info("GET /api/petfinder/all");
        return ResponseEntity.ok(petfinderService.findAllDogsNear43235());
    }

    @GetMapping(value = "/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findFilteredDogsNear43235(@RequestParam(required = false) Integer limit) throws JsonProcessingException {
        log.info("GET /api/petfinder/filtered");
        return ResponseEntity.ok(petfinderService.findFilteredDogs(limit));
    }
}
