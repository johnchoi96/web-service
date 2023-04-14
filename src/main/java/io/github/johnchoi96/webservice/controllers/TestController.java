package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.components.Uptime;
import io.github.johnchoi96.webservice.factories.UptimeFactory;
import io.github.johnchoi96.webservice.models.uptime.UptimeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping(value = "/api/test")
@Slf4j
public class TestController {

    @Autowired
    private Uptime uptime;

    @GetMapping(value = "/helloWorld", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> helloWorld() {
        return ResponseEntity.ok(String.format("johnchoi96/web-service returned Hello World at %s", Instant.now().toString()));
    }

    @GetMapping(value = "/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> ping() {
        log.info(Instant.now().toString());
        return ResponseEntity.ok("pong");
    }

    @GetMapping(value = "/uptime", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UptimeResponse> uptime() {
        return ResponseEntity.ok(UptimeFactory.build(uptime.getStartTime(), uptime.getUptime()));
    }
}
