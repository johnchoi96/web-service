package io.github.johnchoi96.webservice.controllers;

import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
import io.github.johnchoi96.webservice.services.FCMService;
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
@RequestMapping(path = "/api/v1/fcm")
@RequiredArgsConstructor
@Slf4j
public class FCMController {

    private final FCMService fcmService;

    private final AdminKeysProperties adminKeysProperties;

    /**
     * I didn't spend too much time on security but at least it's something...
     *
     * @param topic topic to send the notification to
     * @param key   admin key
     * @return response
     * @throws FirebaseMessagingException if notification failed to be sent
     */
    @GetMapping(value = "/send-test-notification/{topic}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> sendTestNotification(@PathVariable final String topic, @RequestParam final String key) throws FirebaseMessagingException {
        log.info("GET /api/v1/fcm/send-test-notification");
        if (!adminKeysProperties.getAdminKey().equals(key)) {
            return ResponseEntity.badRequest().body("Invalid admin key");
        }
        final FCMTopic topicEnum = switch (topic) {
            case "jc-alerts-all" -> FCMTopic.ALL;
            case "jc-alerts-petfinder" -> FCMTopic.PETFINDER;
            case "jc-alerts-metalprice" -> FCMTopic.METALPRICE;
            default -> null;
        };
        if (topicEnum == null) {
            return ResponseEntity.badRequest().body(
                    "Topic should be either jc-alerts-all, jc-alerts-petfinder, or jc-alerts-metalprice"
            );
        }
        fcmService.sendTestNotification(topicEnum);
        return ResponseEntity.ok().build();
    }
}
