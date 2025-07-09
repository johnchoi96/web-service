package com.johnchoi96.webservice.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteBatch;
import com.google.cloud.firestore.WriteResult;
import com.johnchoi96.webservice.models.firebase.cloudfirestore.NotificationPayload;
import com.johnchoi96.webservice.utils.InstantUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudFirestoreService {

    private final Firestore db;

    private CollectionReference collectionRef;

    private final String DB_COLLECTION = "notifications";

    @PostConstruct
    private void init() {
        collectionRef = db.collection(DB_COLLECTION);
    }

    public String addNotificationPayload(final Map<String, Object> notificationMetadata) throws ExecutionException, InterruptedException {
        final String notificationId = generateUuid();
        final DocumentReference docRef = collectionRef.document(notificationId);

        // asynchronously write data
        final ApiFuture<WriteResult> result = docRef.set(notificationMetadata);
        log.info("Update time: {}", result.get().getUpdateTime());
        return notificationId;
    }

    private String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public List<NotificationPayload> fetchAllNotifications() throws ExecutionException, InterruptedException {
        final ApiFuture<QuerySnapshot> future = collectionRef.get();
        final List<QueryDocumentSnapshot> documents = future.get().getDocuments();
        return documents.stream().map(document -> deserializePayload(document.getId(), document.getData())).toList();
    }

    public void deleteNotificationsOlderThanDays(final int days) throws ExecutionException, InterruptedException {
        if (days <= 0) return;
        final List<NotificationPayload> payloads = fetchAllNotifications();
        final Instant current = Instant.now();
        final List<String> targetNotificationIds = payloads.stream()
                .filter(payload -> InstantUtil.getDifferenceInDays(current, payload.getTimestamp()) > days)
                .map(NotificationPayload::getId)
                .toList();
        // use batch job to delete
        final WriteBatch batch = db.batch();
        targetNotificationIds.forEach(id -> {
            final DocumentReference payloadRef = collectionRef.document(id);
            batch.delete(payloadRef);
        });
        final ApiFuture<List<WriteResult>> future = batch.commit();
        future.get().forEach(result -> log.info("Update time: {}", result.getUpdateTime()));
    }

    private NotificationPayload deserializePayload(final String id, final Map<String, Object> payload) {
        final String IS_HTML = "isHtml";
        final String MESSAGE = "message";
        final String NOTIFICATION_BODY = "notification-body";
        final String NOTIFICATION_TITLE = "notification-title";
        final String TEST_NOTIFICATION = "test-notification";
        final String TIMESTAMP = "timestamp";
        final String TOPIC = "topic";

        var builder = NotificationPayload.builder()
                .id(id)
                .isHtml((Boolean) payload.get(IS_HTML))
                .message((String) payload.get(MESSAGE))
                .notificationBody((String) payload.get(NOTIFICATION_BODY))
                .notificationTitle((String) payload.get(NOTIFICATION_TITLE))
                .isTestNotification((Boolean) payload.get(TEST_NOTIFICATION))
                .topic((String) payload.get(TOPIC));
        final Instant deserializedTimestamp = Instant.parse((String) payload.get(TIMESTAMP));
        builder.timestamp(deserializedTimestamp);
        return builder.build();
    }
}
