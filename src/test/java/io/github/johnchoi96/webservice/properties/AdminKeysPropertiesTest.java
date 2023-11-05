package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.configs.MockedBeansConfigs;
import io.github.johnchoi96.webservice.properties.adminkeys.AdminKeysProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(MockedBeansConfigs.class)
public class AdminKeysPropertiesTest {

    @Autowired
    private AdminKeysProperties adminKeysProperties;

    @Test
    void testAdminKeysProperties() {
        assertNotNull(adminKeysProperties);
        final String DUMMY_ADMIN_KEY = "dummy-admin-key";

        assertEquals(DUMMY_ADMIN_KEY, adminKeysProperties.getAdminKey());
    }
}
