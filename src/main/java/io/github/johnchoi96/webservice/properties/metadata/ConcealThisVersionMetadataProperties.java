package io.github.johnchoi96.webservice.properties.metadata;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "concealthis")
public class ConcealThisVersionMetadataProperties extends MetadataProperties {
}
