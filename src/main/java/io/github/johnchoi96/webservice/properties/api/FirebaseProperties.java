package io.github.johnchoi96.webservice.properties.api;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

@Getter
@Setter
@ConfigurationProperties(prefix = "gcp.firebase")
public class FirebaseProperties {
    private Resource serviceAccount;
}