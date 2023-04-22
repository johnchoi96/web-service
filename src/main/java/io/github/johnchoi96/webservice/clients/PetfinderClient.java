package io.github.johnchoi96.webservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.johnchoi96.webservice.configs.auth.BearerToken;
import io.github.johnchoi96.webservice.models.petfinder.PetfinderResponse;
import io.github.johnchoi96.webservice.properties.api.PetfinderApiProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class PetfinderClient {

    @Autowired
    private PetfinderApiProperties petfinderApiProperties;

    private HttpHeaders createHttpHeadersWithBearerToken(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    private String concatenateBreeds(final Set<String> breeds) {
        final StringBuilder sb = new StringBuilder();

        breeds.forEach(breed -> sb.append(breed).append(","));
        return sb.substring(0, sb.length() - 1);
    }

    public List<PetfinderResponse> findAllDogsNear43235(final Set<String> breeds) throws JsonProcessingException {
        final String clientId = petfinderApiProperties.getClientId();
        final String clientSecret = petfinderApiProperties.getClientSecret();
        final String tokenUri = petfinderApiProperties.getBearerToken().getUri();

        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = BearerToken.getPetfinderBearerToken(tokenUri, clientId, clientSecret);
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final String uri = "https://api.petfinder.com/v2/animals";
        int currentPage = 1;
        int finalPage;
        List<PetfinderResponse> responseList = new ArrayList<>();
        do {
            final ResponseEntity<String> result = restTemplate.exchange(String.format("%s?type=dog&location=43235&breed=%s&page=%d", uri, concatenateBreeds(breeds), currentPage), HttpMethod.GET, entity, String.class);

            final ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            final PetfinderResponse response = mapper.readValue(result.getBody(), PetfinderResponse.class);
            finalPage = response.getPagination().getTotalPages();

            log.info("Querying page {} out of {}", currentPage++, finalPage);
            responseList.add(response);
        } while (currentPage <= finalPage);
        return responseList;
    }
}
