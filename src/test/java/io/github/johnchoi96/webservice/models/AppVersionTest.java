package io.github.johnchoi96.webservice.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppVersionTest {

    @Test
    void testAppVersion() {
        final String version = "0.0.1/3";
        final AppVersion appVersion = AppVersion.builder().version(version).build();
        assertEquals(version, appVersion.getVersion());
    }
}
