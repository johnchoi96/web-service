package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.clients.PetfinderClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/petfinder")
@Slf4j
public class PetfinderController {

    @Autowired
    private PetfinderClient petfinderClient;

    @GetMapping(value = "/shiba/43235", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findShibaNear43235() throws JsonProcessingException {
        return ResponseEntity.ok(petfinderClient.findShibaNear43235());
    }
}
