package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.properties.api.PetfinderApiProperties;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApiPropertiesTest {

    @Autowired
    private PetfinderApiProperties petfinderApiProperties;

    @Autowired
    private SendGridApiProperties sendGridApiProperties;

    @Test
    void testPetfinderApiProperty() {
        assertNotNull(petfinderApiProperties);
        assertNotNull(petfinderApiProperties.getClientId());
        assertNotNull(petfinderApiProperties.getClientSecret());
        assertNotNull(petfinderApiProperties.getBearerToken());
    }

    @Test
    void testSendGridApiProperty() {
        assertNotNull(sendGridApiProperties);
        assertNotNull(sendGridApiProperties.getApiKey());
    }
}
