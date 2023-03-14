package io.github.johnchoi96.webservice.services;

import io.github.johnchoi96.webservice.factories.AppVersionFactory;
import io.github.johnchoi96.webservice.models.AppVersion;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

@Service
public class VersionService {

    private final Set<String> knownBundleIds = Set.of(
            "io.github.johnchoi96.concealthis",
            "io.github.johnchoi96.voa"
    );

    public AppVersion getLatestVersion(@NonNull final String bundleId) throws IOException {
        if (!knownBundleIds.contains(bundleId.toLowerCase(Locale.US))) {
            return null;
        }
        final String latestVersion = getAppVersionFromFile(bundleId.toLowerCase(Locale.US));
        return AppVersionFactory.build(latestVersion);
    }

    protected String getAppVersionFromFile(final String bundleId) throws IOException {
        String path = String.format("./version-properties/%s.properties", bundleId);
        Properties mainProperties = new Properties();
        FileInputStream file = new FileInputStream(path);
        mainProperties.load(file);
        file.close();
        return mainProperties.getProperty("app.version");
    }
}
