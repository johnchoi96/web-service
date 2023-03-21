package io.github.johnchoi96.webservice.controllers;

import io.github.johnchoi96.webservice.configs.SendGridApiConfiguration;
import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/email")
@Slf4j
public class EmailController {

    @Autowired
    private SendGridApiConfiguration sendGridApiConfiguration;

    @Autowired
    private EmailService emailService;

    @PostMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendEmail(@RequestBody final EmailRequest request) {
        if (emailService.sendEmail(request, sendGridApiConfiguration.getApiKey())) {
            return ResponseEntity.ok("Email sent to johnchoi1003@icloud.com");
        }
        return ResponseEntity.badRequest().build();
    }
}
