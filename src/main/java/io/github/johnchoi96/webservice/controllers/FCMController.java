package io.github.johnchoi96.webservice.controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.services.FCMService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/fcm")
@RequiredArgsConstructor
@Slf4j
public class FCMController {

    private final FCMService fcmService;

    @GetMapping(value = "/send-test-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendTestNotification() throws FirebaseMessagingException {
        log.info("GET /api/v1/fcm/send-test-notification");
        fcmService.sendTestNotification();
        return ResponseEntity.ok().build();
    }
}
