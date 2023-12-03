package io.github.johnchoi96.webservice.configs.firebase;

import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirestoreConfig {

    @Bean
    public Firestore getFirestore() {
        return FirestoreClient.getFirestore();
    }
}
