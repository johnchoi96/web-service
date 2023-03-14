package io.github.johnchoi96.webservice.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
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
    void testGetLatestVersionWithValidBundleId() throws IOException {
        final String versionString = "valid-version";
        doReturn(versionString).when(service).getAppVersionFromFile(anyString());
        final String bundleId = "io.github.johnchoi96.voa";
        var appVersion = service.getLatestVersion(bundleId);
        assertEquals(versionString, appVersion.getVersion());
    }

    @Test
    void testGetLatestVersionWithInvalidBundleId() throws IOException {
        final String versionString = "valid-version";
        doReturn(versionString).when(service).getAppVersionFromFile(anyString());
        final String bundleId = "invalid-bundle-id";
        var appVersion = service.getLatestVersion(bundleId);
        assertNull(appVersion);
    }
}
