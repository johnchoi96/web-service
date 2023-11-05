package io.github.johnchoi96.webservice.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging fcm;

    public void sendTestNotification() throws FirebaseMessagingException {

        final Notification notification = Notification
                .builder()
                .setTitle("Test Notification title from rest server")
                .setBody("Test Notification body from rest server")
                .build();
        final Message msg = Message.builder()
                .setTopic(FCMTopic.METALPRICE.getValue())
                .setNotification(notification)
                .putData("body", "Test data")
                .build();

        final String id = fcm.send(msg);
        log.info("Notification sent with id: {}", id);
    }

    public void sendNotification(
            final FCMTopic topic,
            final String notificationTitle,
            final String notificationBody,
            final Map<String, String> data
    ) throws FirebaseMessagingException {
        final Notification notification = Notification
                .builder()
                .setTitle(notificationTitle)
                .setBody(notificationBody)
                .build();
        final Message msg = Message.builder()
                .setTopic(topic.getValue())
                .setNotification(notification)
                .putAllData(data)
                .build();

        final String id = fcm.send(msg);
        log.info("Notification sent with id: {}", id);
    }
}
