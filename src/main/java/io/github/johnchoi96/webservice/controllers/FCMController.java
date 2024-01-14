package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
import io.github.johnchoi96.webservice.services.CfbService;
import io.github.johnchoi96.webservice.services.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/fcm")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "FCM Controller")
public class FCMController {

    private final FCMService fcmService;

    private final CfbService cfbService;

    private final AdminKeysProperties adminKeysProperties;

    /**
     * I didn't spend too much time on security but at least it's something...
     *
     * @param key admin key
     * @return response
     * @throws FirebaseMessagingException if notification failed to be sent
     */
    @GetMapping(value = "/send-test-notification", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Sends a test notification.")
    public ResponseEntity<?> sendTestNotification(
            @Parameter(description = "admin key") @RequestParam final String key) throws FirebaseMessagingException {
        log.info("GET /api/fcm/send-test-notification");
        if (!adminKeysProperties.getAdminKey().equals(key)) {
            return ResponseEntity.badRequest().body("Invalid admin key");
        }
        final FCMTopic topicEnum = FCMTopic.TEST_NOTIFICATION;
        fcmService.sendTestNotification(topicEnum);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/cfb/upsets", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Triggers a CFB upset report notification.")
    public ResponseEntity<?> triggerCfbUpsetNotification(@Parameter(description = "admin key") @RequestParam final String key)
            throws JsonProcessingException, FirebaseMessagingException {
        log.info("GET /api/fcm/cfb/upsets");
        if (!adminKeysProperties.getAdminKey().equals(key)) {
            return ResponseEntity.badRequest().body("Invalid admin key");
        }
        cfbService.triggerUpsetReport();
        return ResponseEntity.ok().build();
    }
}
