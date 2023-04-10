package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.configs.SendGridApiConfiguration;
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
    private SendGridApiConfiguration sendGridApiConfiguration;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/contactme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmailForContactMe(@RequestBody final EmailRequest request) {
        if (emailService.sendEmailForContactMe(request)) {
            return ResponseEntity.ok("Email sent to johnchoi1003@icloud.com");
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/contactme", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmailForContactMe(@RequestParam("subject") final String subject, @RequestParam("body") final String body, @RequestParam(value = "email", required = false) final String email) {
        final EmailRequest request = EmailRequest.builder().subject(subject).body(body).contactInfo(email).build();
        if (emailService.sendEmailForContactMe(request)) {
            return ResponseEntity.ok("Email sent to johnchoi1003@icloud.com");
        }
        return ResponseEntity.badRequest().build();
    }
}
