package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.factories.AppVersionFactory;
import io.github.johnchoi96.webservice.models.AppVersion;
import io.github.johnchoi96.webservice.properties.metadata.ConcealThisVersionProperties;
import io.github.johnchoi96.webservice.properties.metadata.Properties;
import io.github.johnchoi96.webservice.properties.metadata.VoaVersionProperties;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
public class VersionService {

    @Autowired
    private ConcealThisVersionProperties concealThisVersionProperties;

    @Autowired
    private VoaVersionProperties voaVersionProperties;

    private Map<String, Properties> knownAppNames;

    @PostConstruct
    private void init() {
        knownAppNames = Map.of(
                concealThisVersionProperties.getAppId().toLowerCase(), concealThisVersionProperties,
                voaVersionProperties.getAppId().toLowerCase(), voaVersionProperties
        );
    }

    public AppVersion getLatestVersion(@NonNull final String appId) {
        if (!knownAppNames.containsKey(appId.toLowerCase(Locale.US))) {
            return null;
        }
        final String latestVersion = knownAppNames.get(appId.toLowerCase()).getVersion();
        return AppVersionFactory.build(latestVersion);
    }
}
