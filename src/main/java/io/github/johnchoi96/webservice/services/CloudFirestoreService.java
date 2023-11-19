package io.github.johnchoi96.webservice.services;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

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

    public String addNotificationPayload(final Map<String, Object> notificationMetadata) throws ExecutionException, InterruptedException {
        final String notificationId = generateUuid();
        final DocumentReference docRef = collectionRef.document(notificationId);

        // asynchronously write data
        final ApiFuture<WriteResult> result = docRef.set(notificationMetadata);
        log.info("Update time: {}", result.get().getUpdateTime());
        return notificationId;
    }

    private String generateUuid() {
        final int UUID_LENGTH = 15;
        // all possible alphanumeric characters
        return RandomStringUtils.randomAlphanumeric(UUID_LENGTH);
    }
}
