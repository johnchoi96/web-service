package io.github.johnchoi96.webservice.configs.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import io.github.johnchoi96.webservice.configs.google.GoogleCredentialsConfig;
import io.github.johnchoi96.webservice.properties.api.FirebaseProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class FirebaseAppConfig {

    private final GoogleCredentialsConfig googleCredentialsConfig;

    private final FirebaseProperties firebaseProperties;

    private static boolean firebaseInitialized = false;

    @Bean
    public Firestore firestore(@Qualifier("firebaseCredentials") GoogleCredentials googleCredentials) throws IOException {
        return FirestoreClient.getFirestore(firebaseApp(googleCredentialsConfig.firebaseCredentials()));
    }

    @Bean
    public FirebaseApp firebaseApp(@Qualifier("firebaseCredentials") GoogleCredentials credentials) {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        if (!firebaseInitialized) {
            FirebaseApp.initializeApp(options);
            firebaseInitialized = true;
        }
        return FirebaseApp.getInstance();
    }
}
