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

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GoogleCredentialsConfig {

    private final FirebaseProperties firebaseProperties;

    private final GoogleProperties googleProperties;

    @Bean
    @Qualifier("firebaseCredentials")
    public GoogleCredentials firebaseCredentials() throws IOException {
        try (var stream = firebaseProperties.getServiceAccountAsResource().getInputStream()) {
            return GoogleCredentials.fromStream(stream);
        }
    }

    @Bean
    @Qualifier("driveCredentials")
    public GoogleCredentials driveCredentials() throws IOException {
        try (var stream = googleProperties.getServiceAccountAsResource().getInputStream()) {
            return GoogleCredentials.fromStream(stream).createScoped(DriveScopes.DRIVE);
        }
    }
}
