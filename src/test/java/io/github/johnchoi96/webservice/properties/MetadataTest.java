package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.properties.metadata.ConcealThisMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataProperties;
import liquibase.integration.spring.SpringLiquibase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MetadataTest {

    @MockBean
    private SpringLiquibase liquibase;

    @Autowired
    private ConcealThisMetadataProperties concealThisVersionProperties;

    @Autowired
    private VoaMetadataProperties voaVersionProperties;

    @Autowired
    private WebAppMetadataProperties webAppMetadataProperties;

    @Test
    public void testConcealThisProperty() {
        assertNotNull(concealThisVersionProperties);
        final String DUMMY_APP_ID = "concealthis-app-id";
        final String DUMMY_VERSION = "1.2.3";
        assertEquals(DUMMY_APP_ID, concealThisVersionProperties.getAppId());
        assertEquals(DUMMY_VERSION, concealThisVersionProperties.getVersion());
    }

    @Test
    public void testVoaVersionProperty() {
        assertNotNull(voaVersionProperties);
        final String DUMMY_APP_ID = "voa-app-id";
        final String DUMMY_VERSION = "a.b";
        assertEquals(DUMMY_APP_ID, voaVersionProperties.getAppId());
        assertEquals(DUMMY_VERSION, voaVersionProperties.getVersion());
    }

    @Test
    public void testWebAppMetadataProperty() {
        assertNotNull(webAppMetadataProperties);
        final String DUMMY_APP_ID = "webapp-id";
        final String DUMMY_VERSION = "6.7.8-a";
        assertEquals(DUMMY_APP_ID, webAppMetadataProperties.getAppId());
        assertEquals(DUMMY_VERSION, webAppMetadataProperties.getVersion());
    }
}
