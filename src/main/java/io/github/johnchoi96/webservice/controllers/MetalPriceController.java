package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.services.MetalPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/metal-price")
@RequiredArgsConstructor
@Slf4j
public class MetalPriceController {

    private final MetalPriceService metalPriceService;

    @GetMapping(value = "/trigger-report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> metalPrice() throws JsonProcessingException {
        metalPriceService.analyzeGoldPriceAndReport();
        return ResponseEntity.ok().build();
    }
}
