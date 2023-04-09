package io.github.johnchoi96.webservice.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "petfinder")
public class PetfinderApiConfiguration {

    private String clientId;

    private String clientSecret;

    private PetfinderBearerToken bearerToken;
}
