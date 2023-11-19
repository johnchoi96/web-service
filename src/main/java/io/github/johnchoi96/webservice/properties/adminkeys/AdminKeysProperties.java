package io.github.johnchoi96.webservice.properties.adminkeys;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "endpoint")
public class AdminKeysProperties {

    private String adminKey;
}