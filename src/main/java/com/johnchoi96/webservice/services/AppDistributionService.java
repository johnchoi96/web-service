package com.johnchoi96.webservice.services;

import com.johnchoi96.webservice.entities.appmetadata.AppMetadataEntity;
import com.johnchoi96.webservice.entities.user.UserEntity;
import com.johnchoi96.webservice.models.app_distribution.response.AppMetadata;
import com.johnchoi96.webservice.models.app_distribution.response.AppMetadataResponse;
import com.johnchoi96.webservice.properties.api.aws.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
                .appName(entity.getBucketName())
                .version(entity.getVersion())
                .otaDownloadLink(buildOtaDownloadLink(entity.getBucketName()))
                .build();
    }

    private String buildOtaDownloadLink(final String appName) {
        final String otaLink = "itms-services://?action=download-manifest&url=%s/%s/%s";
        final String cdnUrl = s3Properties.getAppleIpa().getCdnUrl();
        final String manifestFileName = s3Properties.getAppleIpa().getManifestFile();
        return String.format(otaLink, cdnUrl, appName, manifestFileName);
    }
}
