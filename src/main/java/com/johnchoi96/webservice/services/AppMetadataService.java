package com.johnchoi96.webservice.services;

import com.johnchoi96.webservice.entities.appmetadata.AppMetadataEntity;
import com.johnchoi96.webservice.models.AppVersion;
import com.johnchoi96.webservice.repos.appmetadata.AppMetadataRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AppMetadataService {

    private final AppMetadataRepo appMetadataRepo;

    public AppVersion getLatestVersion(@NonNull final String appId) {
        final Optional<AppMetadataEntity> entity = appMetadataRepo.getAppMetadataWithAppId(appId);
        return entity.map(AppMetadataEntity::buildResponse).orElse(null);
    }
}
