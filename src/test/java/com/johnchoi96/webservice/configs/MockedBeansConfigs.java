package com.johnchoi96.webservice.configs;

import com.github.javafaker.Faker;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.johnchoi96.webservice.configs.database.DataSourceConfig;
import com.johnchoi96.webservice.configs.jackson.JacksonConfig;
import com.johnchoi96.webservice.configs.swaggerui.SwaggerUiConfig;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MockedBeansConfigs {

    @Bean
    public GoogleCredentials googleCredentials() {
        return Mockito.mock(GoogleCredentials.class);
    }

    @Bean
    public FirebaseApp firebaseApp() {
        return Mockito.mock(FirebaseApp.class);
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() {
        return Mockito.mock(FirebaseMessaging.class);
    }

    @Bean
    public Firestore firestore() {
        return Mockito.mock(Firestore.class);
    }

    @Bean
    public JacksonConfig jacksonConfig() {
        return Mockito.mock(JacksonConfig.class);
    }

    @Bean
    public SwaggerUiConfig swaggerUiConfig() {
        return Mockito.mock(SwaggerUiConfig.class);
    }

    @Bean
    public Faker faker() {
        return Mockito.mock(Faker.class);
    }

    @Bean
    public DataSourceConfig dataSourceConfig() {
        return Mockito.mock(DataSourceConfig.class);
    }
}
