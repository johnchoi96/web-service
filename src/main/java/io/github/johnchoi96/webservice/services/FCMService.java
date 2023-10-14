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

    public void sendNotification() throws FirebaseMessagingException {

        Notification notification = Notification
                .builder()
                .setTitle("notification title from rest serv")
                .setBody("notification body from rest serv")
                .build();
        Message msg = Message.builder()
                .setTopic("jc-alert")
                .setNotification(notification)
                .putData("body", "some data")
                .build();

        String id = fcm.send(msg);
        log.info(id);
    }
}
