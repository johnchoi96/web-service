package com.johnchoi96.webservice.factories;

import com.johnchoi96.webservice.models.AppVersion;
import lombok.experimental.UtilityClass;

@UtilityClass
public class AppVersionFactory {

    public AppVersion build(final String appVersion) {
        return AppVersion
                .builder()
                .version(appVersion)
                .build();
    }
}
