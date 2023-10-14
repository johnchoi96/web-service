package io.github.johnchoi96.webservice.controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.services.FCMService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/fcm")
@RequiredArgsConstructor
public class FCMController {

    private final FCMService fcmService;

    @GetMapping(value = "/send", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> send() throws FirebaseMessagingException {
        fcmService.sendNotification();
        return ResponseEntity.ok().build();
    }
}
