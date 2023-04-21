package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.properties.metadata.ConcealThisVersionProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaVersionProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

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
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doReturn("conceal-version").when(concealThisVersionProperties).getVersion();
        doReturn("voa-version").when(voaVersionProperties).getVersion();
    }
    
    @Test
    void testGetLatestVersionWithValidBundleId() {
        final String versionString = "voa-version";
        final String appName = "voa";
        var appVersion = versionService.getLatestVersion(appName);
        assertEquals(versionString, appVersion.getVersion());
    }

    @Test
    void testGetLatestVersionWithInvalidBundleId() {
        final String appName = "invalid-bundle-id";
        var appVersion = versionService.getLatestVersion(appName);
        assertNull(appVersion);
    }

    @Test
    void testGetConcealAppVersionFromFile() {
        final String expected = "conceal-version";
        assertEquals(expected, versionService.getAppVersionFromFile(VersionService.AppNames.ConcealThis));
    }

    @Test
    void testGetVoaAppVersionFromFile() {
        final String expected = "voa-version";
        assertEquals(expected, versionService.getAppVersionFromFile(VersionService.AppNames.VOA));
    }
}
