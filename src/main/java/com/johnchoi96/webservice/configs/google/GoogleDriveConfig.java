package com.johnchoi96.webservice.configs.google;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.johnchoi96.webservice.properties.api.GoogleProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Configuration
@RequiredArgsConstructor
public class GoogleDriveConfig {

    private final GoogleProperties googleProperties;

    @Bean
    public Drive createDrive(@Qualifier("driveCredentials") GoogleCredentials credentials) throws IOException {
        try (final InputStream is = googleProperties.getServiceAccountAsResource().getInputStream()) {
            final HttpRequestInitializer requestInitializer = new HttpCredentialsAdapter(
                    credentials);

            // Build a new authorized API client service.
            return new Drive.Builder(new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    requestInitializer)
                    .setApplicationName("johnchoi96_webservice")
                    .build();
        }
    }
}
