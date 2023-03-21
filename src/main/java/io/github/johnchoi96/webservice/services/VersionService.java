package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.configs.ConcealThisVersionConfiguration;
import io.github.johnchoi96.webservice.configs.VoaVersionConfiguration;
import io.github.johnchoi96.webservice.factories.AppVersionFactory;
import io.github.johnchoi96.webservice.models.AppVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
public class VersionService {

    protected enum AppNames {
        ConcealThis,
        VOA
    }

    @Autowired
    private ConcealThisVersionConfiguration concealThisVersionConfiguration;

    @Autowired
    private VoaVersionConfiguration voaVersionConfiguration;

    private final Map<String, AppNames> knownAppNames = Map.of(
            "concealthis", AppNames.ConcealThis,
            "voa", AppNames.VOA
    );

    public AppVersion getLatestVersion(@NonNull final String appName) {
        if (!knownAppNames.containsKey(appName.toLowerCase(Locale.US))) {
            return null;
        }
        final String latestVersion = getAppVersionFromFile(knownAppNames.get(appName.toLowerCase(Locale.US)));
        return AppVersionFactory.build(latestVersion);
    }

    protected String getAppVersionFromFile(final AppNames appName) {
        return switch (appName) {
            case ConcealThis -> concealThisVersionConfiguration.getVersion();
            case VOA -> voaVersionConfiguration.getVersion();
        };
    }
}
