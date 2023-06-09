package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/email")
@Slf4j
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/contactme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmailForContactMe(
            @RequestParam("appId") final String appId,
            @RequestBody final EmailRequest request) {
        log.info("POST /api/email/contactme");
        if (emailService.sendEmailForContactMe(request, appId)) {
            return ResponseEntity.ok("Email sent to johnchoi1003@icloud.com");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/contactme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmailForContactMe(
            @RequestParam("appId") final String appId,
            @RequestParam("subject") final String subject,
            @RequestParam("body") final String body,
            @RequestParam(value = "email", required = false) final String email) {
        log.info("GET /api/email/contactme");
        final EmailRequest request = EmailRequest.builder().subject(subject).body(body).contactInfo(email).build();
        if (emailService.sendEmailForContactMe(request, appId)) {
            return ResponseEntity.ok("Email sent to johnchoi1003@icloud.com");
        }
        return ResponseEntity.badRequest().build();
    }
}
