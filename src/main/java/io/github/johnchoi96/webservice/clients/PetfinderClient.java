package io.github.johnchoi96.webservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.johnchoi96.webservice.configs.auth.BearerToken;
import io.github.johnchoi96.webservice.models.petfinder.PetfinderResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PetfinderClient {

    private final String uri = "https://api.petfinder.com/v2/animals";


    @Value("${petfinder.clientId}")
    private String clientId;

    @Value("${petfinder.clientSecret}")
    private String clientSecret;

    @Value("${petfinder.bearerToken.uri}")
    private String tokenUri;

    private HttpHeaders createHttpHeadersWithBearerToken(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }
    
    public PetfinderResponse sendRequest() throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = BearerToken.getPetfinderBearerToken(tokenUri, clientId, clientSecret);
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result = restTemplate.exchange(String.format("%s?breed=shiba inu&location=43235&distance=100", uri, ""), HttpMethod.GET, entity, String.class);

        ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper.readValue(result.getBody(), PetfinderResponse.class);
    }
}
