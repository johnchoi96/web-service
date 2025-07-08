package io.github.johnchoi96.webservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.services.CfbService;
import io.github.johnchoi96.webservice.services.EmailService;
import io.github.johnchoi96.webservice.services.FCMService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/fcm")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Firebase Cloud Messaging", description = "Sends push notifications to the mobile app.")
public class FCMController {

    private final FCMService fcmService;

    private final CfbService cfbService;

    private final EmailService emailService;

    /**
     * Sends a test notification. Requires admin authentication.
     *
     * @return response
     */
    @GetMapping(value = "/send-test-notification", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Sends a test notification using the test topic.", security = {@SecurityRequirement(name = "basicAuth")})
    public ResponseEntity<?> sendTestNotification() {
        log.info("GET /api/fcm/send-test-notification");
        try {
            final FCMTopic topicEnum = FCMTopic.TEST_NOTIFICATION;
            final String testMessage = fcmService.sendTestNotification(topicEnum);
            return ResponseEntity.ok(testMessage);
        } catch (FirebaseMessagingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    /**
     * Sends a test notification with a specified topic.
     * Requires admin authentication.
     *
     * @return response
     */
    @GetMapping(value = "/send-test-notification/{topic}", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Sends a test notification.", security = {@SecurityRequirement(name = "basicAuth")})
    public ResponseEntity<?> sendTestNotification(@PathVariable final String topic) {
        log.info("GET /api/fcm/send-test-notification");
        try {
            final FCMTopic topicEnum = switch (topic) {
                case "jc-alerts-all" -> FCMTopic.ALL;
                case "jc-alerts-petfinder" -> FCMTopic.PETFINDER;
                case "jc-alerts-metalprice" -> FCMTopic.METALPRICE;
                case "jc-alerts-cfb" -> FCMTopic.CFB;
                case "jc-alerts-test" -> FCMTopic.TEST_NOTIFICATION;
                default -> null;
            };
            if (topicEnum == null) {
                return ResponseEntity.badRequest().body(
                        "Topic should be one of: jc-alerts-all, jc-alerts-petfinder, jc-alerts-metalprice, jc-alerts-cfb, jc-alerts-test"
                );
            }
            final String testMessage = fcmService.sendTestNotification(topicEnum);
            return ResponseEntity.ok(testMessage);
        } catch (FirebaseMessagingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping(value = "/cfb/upsets", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Triggers a CFB upset report notification.", security = {@SecurityRequirement(name = "basicAuth")})
    public ResponseEntity<?> triggerCfbUpsetNotification() {
        log.info("GET /api/fcm/cfb/upsets");
        try {
            cfbService.triggerUpsetReport();
            return ResponseEntity.ok().build();
        } catch (JsonProcessingException | FirebaseMessagingException e) {
            emailService.notifyException(e);
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
