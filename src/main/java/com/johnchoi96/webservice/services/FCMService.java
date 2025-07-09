package com.johnchoi96.webservice.services;

import com.github.javafaker.Faker;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging fcm;

    private final CloudFirestoreService cloudFirestoreService;

    private final Faker faker;

    public String sendTestNotification(final FCMTopic topic) throws FirebaseMessagingException {
        final String testMessage = faker.lorem().sentence();
        final StringBuilder message = new StringBuilder("Test message: ");
        message.append(testMessage);
        final String testNotificationTitle = "Test Notification title from rest server";
        final String testNotificationSubtitle = message.toString();
        sendNotification(
                topic,
                testNotificationTitle,
                testNotificationSubtitle,
                message,
                false,
                true
        );
        return message.toString();
    }

    public void sendNotification(
            final FCMTopic topic,
            final String notificationTitle,
            final String notificationBody,
            final StringBuilder message,
            final boolean isHtml,
            final boolean testNotification
    ) throws FirebaseMessagingException {
        // First, try to add notification data to Cloud Firestore
        String notificationId;
        final Map<String, Object> notificationMetadata = Map.of(
                "notification-title", notificationTitle,
                "notification-body", notificationBody,
                "message", message.toString(),
                "isHtml", isHtml,
                "timestamp", Instant.now().toString(),
                "topic", topic.getValue(),
                "test-notification", testNotification
        );
        try {
            notificationId = cloudFirestoreService.addNotificationPayload(notificationMetadata);
        } catch (final ExecutionException e) {
            log.error("Error occurred during computation. Notification will not be sent.", e);
            return;
        } catch (final InterruptedException e) {
            log.error("Current thread interrupted. Notification will not be sent.", e);
            return;
        }
        final Map<String, String> messageData = Map.of(
                "notification-id", notificationId,
                "test-notification", Boolean.toString(testNotification),
                "notification-topic", topic.getValue()
        );
        final Notification notification = Notification
                .builder()
                .setTitle(notificationTitle)
                .setBody(notificationBody)
                .build();
        final Message msg = Message.builder()
                .setTopic(topic.getValue())
                .setNotification(notification)
                .putAllData(messageData)
                .build();

        final String id = fcm.send(msg);
        log.info("Notification sent with id: {}", id);
    }
}
