package io.github.johnchoi96.webservice.properties.metadata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MetadataProperties {

    private String appId;

    private String version;
}
