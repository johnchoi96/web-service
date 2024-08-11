package io.github.johnchoi96.webservice.entities.appmetadata;

import io.github.johnchoi96.webservice.models.AppVersion;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "APP_ID")
    private String appId;

    @Column(name = "VERSION")
    private String version;

    public AppVersion buildResponse() {
        return AppVersion.builder().version(this.getVersion()).build();
    }
}
