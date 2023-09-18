package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.properties.api.MetalPriceApiProperties;
import io.github.johnchoi96.webservice.properties.api.PetfinderApiProperties;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class ApiPropertiesTest {

    @Autowired
    private PetfinderApiProperties petfinderApiProperties;

    @Autowired
    private SendGridApiProperties sendGridApiProperties;

    @Autowired
    private MetalPriceApiProperties metalPriceApiProperties;

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

    @Test
    void testMetalPriceApiProperty() {
        assertNotNull(metalPriceApiProperties);
        final String DUMMY_METAL_PRICE_API_KEY = "dummy-metal-price-apikey";
        assertEquals(DUMMY_METAL_PRICE_API_KEY, metalPriceApiProperties.getApiKey());
    }
}
