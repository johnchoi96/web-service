package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.factories.AppVersionFactory;
import io.github.johnchoi96.webservice.models.AppVersion;
import io.github.johnchoi96.webservice.properties.metadata.ConcealThisMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.MetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VersionService {

    private final ConcealThisMetadataProperties concealThisVersionProperties;

    private final VoaMetadataProperties voaVersionProperties;

    private final WebAppMetadataProperties webAppMetadataProperties;

    private Map<String, MetadataProperties> knownAppNames;

    @PostConstruct
    private void init() {
        knownAppNames = Map.of(
                concealThisVersionProperties.getAppId().toLowerCase(), concealThisVersionProperties,
                voaVersionProperties.getAppId().toLowerCase(), voaVersionProperties,
                webAppMetadataProperties.getAppId().toLowerCase(), webAppMetadataProperties
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
