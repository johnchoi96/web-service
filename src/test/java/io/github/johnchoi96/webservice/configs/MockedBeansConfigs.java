package io.github.johnchoi96.webservice.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
public class MockedBeansConfigs {

    @MockBean
    private GoogleCredentials googleCredentials;

    @MockBean
    private FirebaseApp firebaseApp;

    @MockBean
    private FirebaseMessaging firebaseMessaging;
}
