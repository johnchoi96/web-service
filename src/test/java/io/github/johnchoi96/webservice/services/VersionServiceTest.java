package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.properties.metadata.ConcealThisVersionProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaVersionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;

public class VersionServiceTest {

    @Mock
    @Autowired
    private ConcealThisVersionProperties concealThisVersionProperties;

    @Mock
    @Autowired
    private VoaVersionProperties voaVersionProperties;

    @Autowired
    @InjectMocks
    private VersionService versionService;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        doReturn("conceal-version").when(concealThisVersionProperties).getVersion();
        doReturn("voa-version").when(voaVersionProperties).getVersion();
        doReturn("conceal-id").when(concealThisVersionProperties).getAppId();
        doReturn("voa-id").when(voaVersionProperties).getAppId();
        final Field field = VersionService.class.getDeclaredField("knownAppNames");
        field.setAccessible(true);
        field.set(versionService, Map.of(
                concealThisVersionProperties.getAppId(), concealThisVersionProperties,
                voaVersionProperties.getAppId(), voaVersionProperties));
    }

    @Test
    void testGetLatestVersionWithValidAppId() {
        final String versionString = "voa-version";
        final String appId = "voa-id";
        var appVersion = versionService.getLatestVersion(appId);
        assertEquals(versionString, appVersion.getVersion());
    }

    @Test
    void testGetLatestVersionWithInvalidAppId() {
        final String appId = "invalid-bundle-id";
        var appVersion = versionService.getLatestVersion(appId);
        assertNull(appVersion);
    }
}
