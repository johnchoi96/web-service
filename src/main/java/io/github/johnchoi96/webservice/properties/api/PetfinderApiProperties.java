package io.github.johnchoi96.webservice.properties.api;

import io.github.johnchoi96.webservice.configs.PetfinderBearerToken;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "petfinder")
public class PetfinderApiProperties {

    private String clientId;

    private String clientSecret;

    private PetfinderBearerToken bearerToken;
}
