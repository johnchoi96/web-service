package com.johnchoi96.webservice.services;

import com.johnchoi96.webservice.entities.appmetadata.AppMetadataEntity;
import com.johnchoi96.webservice.entities.user.UserRole;
import com.johnchoi96.webservice.models.AppVersion;
import com.johnchoi96.webservice.repos.appmetadata.AppMetadataRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppMetadataService {

    private final AppMetadataRepo appMetadataRepo;

    public AppVersion getLatestVersion(@NonNull final String appId) {
        final Optional<AppMetadataEntity> entity = appMetadataRepo.getAppMetadataWithAppId(appId);
        return entity.map(AppMetadataEntity::buildResponse).orElse(null);
    }

    public List<AppMetadataEntity> getDistributableAppsForRole(@NonNull final UserRole userRole) {
        final List<AppMetadataEntity> apps = appMetadataRepo.getAllApps();
        return apps.stream().filter(app -> app.getAppName() != null && userRole.canAccess(app.getRequiredPermission())).toList();
    }

    public Optional<AppMetadataEntity> getAppMetadataByBundleId(@NonNull final String appId) {
        return appMetadataRepo.getAppMetadataWithAppId(appId);
    }

    @Transactional
    public void updateVersion(@NonNull final AppMetadataEntity appMetadata, @NonNull final String newVersion) {
        appMetadata.setVersion(newVersion);

        appMetadataRepo.save(appMetadata);
    }
}
