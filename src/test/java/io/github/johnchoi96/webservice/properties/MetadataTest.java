package io.github.johnchoi96.webservice.properties;

import io.github.johnchoi96.webservice.properties.metadata.ConcealThisMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class MetadataTest {

    @Autowired
    private ConcealThisMetadataProperties concealThisVersionProperties;

    @Autowired
    private VoaMetadataProperties voaVersionProperties;

    @Autowired
    private WebAppMetadataProperties webAppMetadataProperties;

    @Test
    public void testConcealThisProperty() {
        assertNotNull(concealThisVersionProperties);
        assertNotNull(concealThisVersionProperties.getAppId());
        assertNotNull(concealThisVersionProperties.getVersion());
    }

    @Test
    public void testVoaVersionProperty() {
        assertNotNull(voaVersionProperties);
        assertNotNull(voaVersionProperties.getAppId());
        assertNotNull(voaVersionProperties.getVersion());
    }

    @Test
    public void testWebAppMetadataProperty() {
        assertNotNull(webAppMetadataProperties);
        assertNotNull(webAppMetadataProperties.getAppId());
        assertNotNull(webAppMetadataProperties.getVersion());
    }
}
