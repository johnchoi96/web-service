package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.services.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/email")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Email Controller")
public class EmailController {

    private final EmailService emailService;

    @PostMapping(value = "/contactme", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sends an email to @johnchoi96 using the JSON body passed.")
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
    @Operation(summary = "Sends an email to @johnchoi96 using the request parameters.")
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
