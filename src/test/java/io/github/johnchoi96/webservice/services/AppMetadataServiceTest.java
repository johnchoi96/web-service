package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.entities.AppMetadataEntity;
import io.github.johnchoi96.webservice.repos.AppMetadataRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class AppMetadataServiceTest {

    @Autowired
    @Mock
    private AppMetadataRepo appMetadataRepo;

    @Autowired
    @InjectMocks
    private AppMetadataService appMetadataService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetLatestVersionWithValidAppId() {
        final String versionString = "expected-version";
        final String appId = "expected-app-id";
        when(appMetadataRepo.getAppMetadataWithAppId(anyString())).thenReturn(getDummyEntity("expected-version"));
        var appVersion = appMetadataService.getLatestVersion(appId);
        assertEquals(versionString, appVersion.getVersion());
    }

    @Test
    void testGetLatestVersionWithInvalidAppId() {
        final String appId = "invalid-bundle-id";
        when(appMetadataRepo.getAppMetadataWithAppId(anyString())).thenReturn(getDummyEntity(null));
        var appVersion = appMetadataService.getLatestVersion(appId);
        assertNull(appVersion);
    }

    private Optional<AppMetadataEntity> getDummyEntity(final String expectedVersion) {
        if (expectedVersion == null) {
            return Optional.empty();
        }
        return Optional.of(AppMetadataEntity.builder()
                .id(1L)
                .appId("expected-app-id")
                .version(expectedVersion)
                .build());
    }
}
