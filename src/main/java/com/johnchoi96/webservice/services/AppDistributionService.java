package com.johnchoi96.webservice.services;

import com.johnchoi96.webservice.entities.appmetadata.AppMetadataEntity;
import com.johnchoi96.webservice.entities.user.UserEntity;
import com.johnchoi96.webservice.models.app_distribution.response.AppMetadata;
import com.johnchoi96.webservice.models.app_distribution.response.AppMetadataResponse;
import com.johnchoi96.webservice.properties.api.aws.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppDistributionService {

    private final UserService userService;

    private final AppMetadataService appMetadataService;

    private final S3Properties s3Properties;

    private final AWSS3Service awss3Service;

    public AppMetadataResponse getAppsForUser(final String email) {
        final Optional<UserEntity> optionalUserEntity = userService.getUserByEmail(email);
        if (optionalUserEntity.isEmpty() || !optionalUserEntity.get().isActive()) {
            throw new IllegalStateException("No user found.");
        }
        final UserEntity userEntity = optionalUserEntity.get();
        // get all apps that this user can access
        final List<AppMetadataEntity> apps = appMetadataService.getDistributableAppsForRole(userEntity.getRole());
        final List<AppMetadata> appMetadata = new ArrayList<>();
        apps.forEach(app -> appMetadata.add(createAppMetadataFromEntity(app)));

        return AppMetadataResponse.builder()
                .apps(appMetadata)
                .build();
    }

    private AppMetadata createAppMetadataFromEntity(final AppMetadataEntity entity) {
        return AppMetadata.builder()
                .appName(entity.getAppName())
                .version(entity.getVersion())
                .otaDownloadLink(buildOtaDownloadLink(entity.getAppName()))
                .build();
    }

    private String buildOtaDownloadLink(final String appName) {
        final String otaLink = "itms-services://?action=download-manifest&url=%s";
        final String manifestFileName = s3Properties.getAppleIpa().getManifestFile();
        return String.format(otaLink, buildIPADownloadUrl(appName, manifestFileName));
    }

    private String buildIPADownloadUrl(final String appName, final String fileName) {
        final String cdnUrl = s3Properties.getAppleIpa().getCdnUrl();
        return String.format("%s/%s/%s", cdnUrl, appName, fileName);
    }

    public void uploadIpa(final String appId, final String newVersion, final MultipartFile file) throws IOException {
        // get app name with appId
        final Optional<AppMetadataEntity> optionalAppMetadataEntity = appMetadataService.getAppMetadataByBundleId(appId);
        if (optionalAppMetadataEntity.isEmpty()) {
            throw new IllegalArgumentException("Invalid app ID.");
        }
        final AppMetadataEntity appMetadata = optionalAppMetadataEntity.get();
        // upload IPA file to S3
        awss3Service.uploadIpa(file.getBytes(), appMetadata.getAppName());
        // create manifest.plist
        final byte[] manifestPlistFile = buildManifestPlist(appMetadata, newVersion);
        // upload manifest.plist file to S3
        awss3Service.uploadManifestPlist(manifestPlistFile, appMetadata.getAppName());

        // upload successful, update our own database with the new version
        appMetadataService.updateVersion(appMetadata, newVersion);
    }

    private byte[] buildManifestPlist(final AppMetadataEntity appMetadata, final String newVersion) {
        final String appFileName = s3Properties.getAppleIpa().getIpaFile();
        final String url = buildIPADownloadUrl(appMetadata.getAppName(), appFileName);
        final String bundleId = appMetadata.getAppId();
        final String appTitle = appMetadata.getAppName();
        return """
                <?xml version="1.0" encoding="UTF-8"?>
                <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN"
                  "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
                <plist version="1.0">
                    <dict>
                      <key>items</key>
                      <array>
                        <dict>
                          <key>assets</key>
                          <array>
                            <dict>
                              <key>kind</key>
                              <string>software-package</string>
                              <key>url</key>
                              <string>%s</string>
                            </dict>
                          </array>
                          <key>metadata</key>
                          <dict>
                            <key>bundle-identifier</key>
                            <string>%s</string>
                            <key>bundle-version</key>
                            <string>%s</string>
                            <key>kind</key>
                            <string>software</string>
                            <key>title</key>
                            <string>%s</string>
                          </dict>
                        </dict>
                      </array>
                    </dict>
                </plist>
                """.formatted(url, bundleId, newVersion, appTitle).getBytes();
    }
}
