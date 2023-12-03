package io.github.johnchoi96.webservice.configs.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.github.johnchoi96.webservice.properties.api.FirebaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseAppConfig {

    private final FirebaseProperties firebaseProperties;

    private static boolean firebaseInitialized = false;

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore(firebaseApp(googleCredentials()));
    }

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (!firebaseInitialized) {
            FirebaseApp.initializeApp(options);
            firebaseInitialized = true;
        }
        return FirebaseApp.getInstance();

//        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public GoogleCredentials googleCredentials() {
        try {
            if (firebaseProperties.getServiceAccount() != null) {
                try (InputStream is = firebaseProperties.getServiceAccountAsResource().getInputStream()) {
                    return GoogleCredentials.fromStream(is);
                }
            } else {
                // Use standard credentials chain. Useful when running inside GKE
                return GoogleCredentials.getApplicationDefault();
            }
        } catch (IOException e) {
            log.error("Error loading service account file: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to load GoogleCredentials from service account file", e);
        }
    }
}
