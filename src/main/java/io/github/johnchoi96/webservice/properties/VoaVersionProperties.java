package io.github.johnchoi96.webservice.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "voa")
public class VoaVersionProperties {

    private String version;
}