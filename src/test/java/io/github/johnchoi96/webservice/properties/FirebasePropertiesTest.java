package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.configs.MockedBeansConfigs;
import io.github.johnchoi96.webservice.properties.api.FirebaseProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(MockedBeansConfigs.class)
public class FirebasePropertiesTest {
    @Autowired
    private FirebaseProperties firebaseProperties;

    @Test
    void testAdminKeysProperties() {
        assertNotNull(firebaseProperties);
        final String DUMMY_SERVICE_ACCOUNT_PATH = "dummy-service-account-path";

        assertEquals(DUMMY_SERVICE_ACCOUNT_PATH, firebaseProperties.getServiceAccountPath());
    }
}
