package io.github.johnchoi96.webservice.configs.google;

import com.google.api.services.drive.DriveScopes;
import com.google.auth.oauth2.GoogleCredentials;
import io.github.johnchoi96.webservice.properties.api.FirebaseProperties;
import io.github.johnchoi96.webservice.properties.api.GoogleProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GoogleCredentialsConfig {

    private final FirebaseProperties firebaseProperties;

    private final GoogleProperties googleProperties;

    @Bean
    @Qualifier("firebaseCredentials")
    public GoogleCredentials firebaseCredentials() {
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

    @Bean
    @Qualifier("googleDriveCredentials")
    public GoogleCredentials googleDriveCredentials() {
        try {
            if (googleProperties.getServiceAccount() != null) {
                try (InputStream is = googleProperties.getServiceAccountAsResource().getInputStream()) {
                    return GoogleCredentials.fromStream(is).createScoped(List.of(DriveScopes.DRIVE_FILE));
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
