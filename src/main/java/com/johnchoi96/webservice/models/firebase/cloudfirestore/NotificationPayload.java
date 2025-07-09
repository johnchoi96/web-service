package com.johnchoi96.webservice.models.firebase.cloudfirestore;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class NotificationPayload {
    private String id;
    
    private Boolean isHtml;

    private String message;

    private String notificationBody;

    private String notificationTitle;

    private Boolean isTestNotification;

    private Instant timestamp;

    private String topic;
}
