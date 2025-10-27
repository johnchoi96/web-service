package com.johnchoi96.webservice.entities.appmetadata;

import com.johnchoi96.webservice.entities.user.UserRole;
import com.johnchoi96.webservice.models.AppVersion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "WEBSERVICE_APP_METADATA")
@Getter
@Setter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class AppMetadataEntity {

    @Id
    @Column(name = "ID")
    private Long id;

    @Column(name = "APP_ID", unique = true)
    private String appId;

    @Column(name = "VERSION")
    private String version;

    /**
     * This has to match the folder name in S3, if applicable.
     * If appName is null, not distributable
     */
    @Column(name = "APP_NAME")
    private String appName;

    @Column(name = "VIEW_PERMISSION")
    @Enumerated(EnumType.STRING)
    private UserRole requiredPermission;

    public AppVersion buildResponse() {
        return AppVersion.builder().version(this.getVersion()).build();
    }
}
