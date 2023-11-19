package io.github.johnchoi96.webservice.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
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

    public String addNotificationPayload(
            final StringBuilder message,
            final FCMTopic topic,
            final boolean isHtml,
            final boolean test
    ) throws ExecutionException, InterruptedException {
        final String notificationId = generateUuid();
        final String timestamp = Instant.now().toString();
        final DocumentReference docRef = collectionRef.document(notificationId);
        final Map<String, Object> data = Map.of(
                "message", message.toString(),
                "isHtml", isHtml,
                "timestamp", timestamp,
                "topic", topic.getValue(),
                "test-notification", test
        );
        // asynchronously write data
        final ApiFuture<WriteResult> result = docRef.set(data);
        log.info("Update time: {}", result.get().getUpdateTime());
        return notificationId;
    }

    private String generateUuid() {
        final int UUID_LENGTH = 15;
        // all possible alphanumeric characters
        return RandomStringUtils.randomAlphanumeric(UUID_LENGTH);
    }
}
