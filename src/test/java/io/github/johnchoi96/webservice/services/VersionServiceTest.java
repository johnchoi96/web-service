package io.github.johnchoi96.webservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

public class VersionServiceTest {

    @Autowired
    @Spy
    private VersionService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testGetLatestVersionWithValidBundleId() {
        final String versionString = "valid-version";
        doReturn(versionString).when(service).getAppVersionFromFile(any());
        final String appName = "voa";
        var appVersion = service.getLatestVersion(appName);
        assertEquals(versionString, appVersion.getVersion());
    }

    @Test
    void testGetLatestVersionWithInvalidBundleId() {
        final String versionString = "valid-version";
        doReturn(versionString).when(service).getAppVersionFromFile(any());
        final String appName = "invalid-bundle-id";
        var appVersion = service.getLatestVersion(appName);
        assertNull(appVersion);
    }
}
