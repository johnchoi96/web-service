package com.johnchoi96.webservice.factories;

import com.johnchoi96.webservice.models.AppVersion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppVersionFactoryTest {

    @Test
    void testBuild() {
        final String appVersion = "1.0.0/1";
        final AppVersion version = AppVersionFactory.build(appVersion);
        assertEquals(appVersion, version.getVersion());
    }
}
