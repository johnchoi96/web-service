package io.github.johnchoi96.webservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import io.github.johnchoi96.webservice.models.cfb.calendar.CalendarResponseItem;
import io.github.johnchoi96.webservice.models.cfb.game_data.GameDataResponseItem;
import io.github.johnchoi96.webservice.models.cfb.win_probability.WinProbabilityResponseItem;
import io.github.johnchoi96.webservice.properties.api.CollegeFootballDataProperties;
import io.github.johnchoi96.webservice.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CfbClient {

    private final CollegeFootballDataProperties collegeFootballDataProperties;

    private final String CALENDAR_URL = "https://api.collegefootballdata.com/calendar?year=%d";

    private final String WIN_PROBABILITY_URL = "https://api.collegefootballdata.com/metrics/wp/pregame?year=%d&week=%d&seasonType=%s";

    private final String GAME_DATA_URL = "https://api.collegefootballdata.com/games?year=%d&seasonType=%s&id=%d";

    private HttpHeaders createHttpHeadersWithBearerToken(final String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    public List<CalendarResponseItem> getCurrentSeasonCalendar() throws JsonProcessingException {
        var date = InstantUtil.getDate(Instant.now());
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result = restTemplate.exchange(
                String.format(CALENDAR_URL, date.year()),
                HttpMethod.GET,
                entity,
                String.class
        );
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }

    public List<WinProbabilityResponseItem> getPregameWinProbabilityDataForWeek(final CalendarResponseItem calendar) throws JsonProcessingException {
        var date = InstantUtil.getDate(Instant.now());
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result = restTemplate.exchange(
                String.format(
                        WIN_PROBABILITY_URL,
                        date.year(),
                        calendar.getWeek(),
                        calendar.getSeasonType()
                ),
                HttpMethod.GET,
                entity,
                String.class
        );
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }

    public List<GameDataResponseItem> getGameData(final String seasonType, final int gameId) throws JsonProcessingException {
        var date = InstantUtil.getDate(Instant.now());
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result = restTemplate.exchange(
                String.format(GAME_DATA_URL,
                        date.year(),
                        seasonType,
                        gameId
                ),
                HttpMethod.GET,
                entity,
                String.class
        );
        final ObjectMapper mapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }
}
