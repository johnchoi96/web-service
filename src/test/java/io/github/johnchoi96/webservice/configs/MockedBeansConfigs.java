package io.github.johnchoi96.webservice.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import io.github.johnchoi96.webservice.configs.jackson.JacksonConfig;
import io.github.johnchoi96.webservice.configs.swaggerui.SwaggerUiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;

@TestConfiguration
@RequiredArgsConstructor
public class MockedBeansConfigs {

    @MockBean
    private final GoogleCredentials googleCredentials;

    @MockBean
    private final FirebaseApp firebaseApp;

    @MockBean
    private final FirebaseMessaging firebaseMessaging;

    @MockBean
    private final Firestore firestore;

    @MockBean
    private final JacksonConfig jacksonConfig;

    @MockBean
    private final SwaggerUiConfig swaggerUiConfig;
}
