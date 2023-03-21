package io.github.johnchoi96.webservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "concealthis")
public class ConcealThisVersionConfiguration {

    private String version;
}
