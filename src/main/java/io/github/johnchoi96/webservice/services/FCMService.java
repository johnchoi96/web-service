package io.github.johnchoi96.webservice.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FCMService {

    private final FirebaseMessaging fcm;

    private final String NOTIFICATION_TOPIC = "jc-alerts";

    public void sendTestNotification() throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle("Notification title from rest server")
                .setBody("Notification body from rest server")
                .build();
        Message msg = Message.builder()
                .setTopic(NOTIFICATION_TOPIC)
                .setNotification(notification)
                .putData("body", "some data")
                .build();

        String id = fcm.send(msg);
        log.info("Notification sent with id: {}", id);
    }
}
