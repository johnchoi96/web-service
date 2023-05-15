package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.properties.api.PetfinderApiProperties;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApiPropertiesTest {

    @MockBean
    private SpringLiquibase liquibase;

    @Autowired
    private PetfinderApiProperties petfinderApiProperties;

    @Autowired
    private SendGridApiProperties sendGridApiProperties;

    @Test
    void testPetfinderApiProperty() {
        assertNotNull(petfinderApiProperties);
        final String DUMMY_CLIENT_ID = "dummy-petfinder-client-id";
        final String DUMMY_CLIENT_SECRET = "dummy-petfinder-client-secret";
        final String DUMMY_BEARER_TOKEN_URI = "dummy-petfinder-bearer-token-uri";
        assertEquals(DUMMY_CLIENT_ID, petfinderApiProperties.getClientId());
        assertEquals(DUMMY_CLIENT_SECRET, petfinderApiProperties.getClientSecret());
        assertEquals(DUMMY_BEARER_TOKEN_URI, petfinderApiProperties.getBearerToken().getUri());
    }

    @Test
    void testSendGridApiProperty() {
        assertNotNull(sendGridApiProperties);
        final String DUMMY_SENDGRID_API_KEY = "dummy-sendgrid-apikey";
        assertEquals(DUMMY_SENDGRID_API_KEY, sendGridApiProperties.getApiKey());
    }
}
