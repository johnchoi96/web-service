package io.github.johnchoi96.webservice.configs.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.johnchoi96.webservice.models.auth.BearerTokenResponse;
import lombok.*;
import lombok.experimental.UtilityClass;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@UtilityClass
public class BearerToken {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    private class PetfinderPost {

        @JsonProperty(value = "grant_type")
        private String grantType;

        @JsonProperty(value = "client_id")
        private String clientId;

        @JsonProperty(value = "client_secret")
        private String clientSecret;
    }

    @Bean
    public String getPetfinderBearerToken(final String uri, final String clientId, final String clientSecret) throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final PetfinderPost postObj = PetfinderPost
                .builder()
                .grantType("client_credentials")
                .clientId(clientId)
                .clientSecret(clientSecret)
                .build();
        final String result = restTemplate.postForObject(uri, postObj, String.class);

        final ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        final BearerTokenResponse response = mapper.readValue(result, BearerTokenResponse.class);
        return response.getAccessToken();
    }
}
