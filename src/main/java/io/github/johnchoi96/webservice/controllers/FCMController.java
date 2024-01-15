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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/fcm")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Firebase Cloud Messaging", description = "Sends push notifications to the mobile app.")
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
    @GetMapping(value = "/send-test-notification", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Sends a test notification using the test topic.")
    public ResponseEntity<?> sendTestNotification(
            @Parameter(description = "admin key") @RequestParam final String key) throws FirebaseMessagingException {
        log.info("GET /api/fcm/send-test-notification");
        if (!adminKeysProperties.getAdminKey().equals(key)) {
            return ResponseEntity.badRequest().body("Invalid admin key");
        }
        final FCMTopic topicEnum = FCMTopic.TEST_NOTIFICATION;
        final String testMessage = fcmService.sendTestNotification(topicEnum);
        return ResponseEntity.ok(testMessage);
    }

    /**
     * I didn't spend too much time on security but at least it's something...
     *
     * @param key admin key
     * @return response
     * @throws FirebaseMessagingException if notification failed to be sent
     */
    @GetMapping(value = "/send-test-notification/{topic}", produces = MediaType.TEXT_PLAIN_VALUE)
    @Operation(summary = "Sends a test notification.")
    public ResponseEntity<?> sendTestNotification(
            @PathVariable final String topic,
            @Parameter(description = "admin key") @RequestParam final String key) throws FirebaseMessagingException {
        log.info("GET /api/fcm/send-test-notification");
        if (!adminKeysProperties.getAdminKey().equals(key)) {
            return ResponseEntity.badRequest().body("Invalid admin key");
        }
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
