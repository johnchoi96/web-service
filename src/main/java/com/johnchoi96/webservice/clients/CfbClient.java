package com.johnchoi96.webservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.johnchoi96.webservice.models.cfb.calendar.CalendarResponseItem;
import com.johnchoi96.webservice.models.cfb.game_data.GameDataResponseItem;
import com.johnchoi96.webservice.models.cfb.rankings.RankingResponseItem;
import com.johnchoi96.webservice.models.cfb.win_probability.WinProbabilityResponseItem;
import com.johnchoi96.webservice.properties.api.CollegeFootballDataProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CfbClient {

    private final CollegeFootballDataProperties collegeFootballDataProperties;

    private final String CALENDAR_URL = "https://api.collegefootballdata.com/calendar?year=%d";

    private final String WIN_PROBABILITY_URL = "https://api.collegefootballdata.com/metrics/wp/pregame?year=%d&week=%d&seasonType=%s";

    private final String GAME_DATA_URL = "https://api.collegefootballdata.com/games?year=%d&seasonType=%s&id=%d";

    private final String RANKING_URL = "https://api.collegefootballdata.com/rankings?year=%d&week=%d&seasonType=%s";

    private final String RANKING_ALL_URL = "https://api.collegefootballdata.com/rankings?year=%d";

    private final int ATTEMPTS = 3;

    private HttpHeaders createHttpHeadersWithBearerToken(final String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        return headers;
    }

    public List<RankingResponseItem> getAllRankings(final int year) throws JsonProcessingException {
        return getAllRankings(year, ATTEMPTS);
    }

    @Cacheable(value = "getRankingForWeek", key = "#year + '-' + #week + '-' + #seasonType")
    public List<RankingResponseItem> getRankingForWeek(final int year, final int week, final String seasonType) throws JsonProcessingException {
        return getRankingForWeek(year, week, seasonType, ATTEMPTS);
    }

    public List<CalendarResponseItem> getCurrentSeasonCalendar(final int year) throws JsonProcessingException {
        return getCurrentSeasonCalendar(year, ATTEMPTS);
    }

    public List<WinProbabilityResponseItem> getPregameWinProbabilityDataForWeek(final CalendarResponseItem calendar) throws JsonProcessingException {
        return getPregameWinProbabilityDataForWeek(calendar, ATTEMPTS);
    }

    public List<GameDataResponseItem> getGameData(final String seasonType, final int year, final int gameId) {
        return getGameData(seasonType, year, gameId, ATTEMPTS);
    }

    private List<RankingResponseItem> getAllRankings(final int year, final int attempts) throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result;
        try {
            result = restTemplate.exchange(
                    String.format(RANKING_ALL_URL, year),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (final HttpServerErrorException e) {
            if (attempts <= 0) {
                log.error("502 error never got resolved.", e);
                return null;
            }
            log.debug("502 error occurred. Attempts left: {}", attempts);
            try {
                log.debug("Sleeping for 10 seconds");
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return getAllRankings(year, attempts - 1);
        }
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }

    private List<RankingResponseItem> getRankingForWeek(final int year, final int week, final String seasonType, final int attempts) throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result;
        try {
            result = restTemplate.exchange(
                    String.format(RANKING_URL,
                            year,
                            week,
                            seasonType
                    ),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (final HttpServerErrorException e) {
            if (attempts <= 0) {
                log.error("502 error never got resolved.", e);
                return null;
            }
            log.debug("502 error occurred. Attempts left: {}", attempts);
            try {
                log.debug("Sleeping for 10 seconds");
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return getRankingForWeek(year, week, seasonType, attempts - 1);
        }
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }

    private List<CalendarResponseItem> getCurrentSeasonCalendar(final int year, final int attempts) throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result;
        try {
            result = restTemplate.exchange(
                    String.format(CALENDAR_URL, year),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (final HttpServerErrorException e) {
            if (attempts <= 0) {
                log.error("502 error never got resolved.", e);
                return null;
            }
            log.debug("502 error occurred. Attempts left: {}", attempts);
            try {
                log.debug("Sleeping for 10 seconds");
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return getCurrentSeasonCalendar(year, attempts - 1);
        }
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }

    private List<WinProbabilityResponseItem> getPregameWinProbabilityDataForWeek(final CalendarResponseItem calendar, final int attempts) throws JsonProcessingException {
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result;
        try {
            result = restTemplate.exchange(
                    String.format(
                            WIN_PROBABILITY_URL,
                            calendar.getSeasonYear(),
                            calendar.getWeek(),
                            calendar.getSeasonType()
                    ),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (final HttpServerErrorException e) {
            if (attempts <= 0) {
                log.error("502 error never got resolved.", e);
                return null;
            }
            log.debug("502 error occurred. Attempts left: {}", attempts);
            try {
                log.debug("Sleeping for 10 seconds");
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return getPregameWinProbabilityDataForWeek(calendar, attempts - 1);
        }
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(result.getBody(), new TypeReference<>() {
        });
    }

    private List<GameDataResponseItem> getGameData(final String seasonType, final int year, final int gameId, final int attempts) {
        final RestTemplate restTemplate = new RestTemplate();
        final String bearerToken = collegeFootballDataProperties.getApiKey();
        final HttpHeaders headers = createHttpHeadersWithBearerToken(bearerToken);
        final HttpEntity<String> entity = new HttpEntity<>(headers);
        final ResponseEntity<String> result;
        try {
            result = restTemplate.exchange(
                    String.format(GAME_DATA_URL,
                            year,
                            seasonType,
                            gameId
                    ),
                    HttpMethod.GET,
                    entity,
                    String.class
            );
        } catch (final HttpServerErrorException e) {
            // NOTE: handles server errors like 502. Retries after 10 seconds
            // until attempts value reaches 0.
            if (attempts <= 0) {
                log.error("502 error never got resolved.", e);
                return null;
            }
            log.debug("502 error occurred. Attempts left: {}", attempts);
            try {
                log.debug("Sleeping for 10 seconds");
                Thread.sleep(1000 * 10);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return getGameData(seasonType, year, gameId, attempts - 1);
        } catch (final HttpClientErrorException.TooManyRequests e) {
            /* NOTE: if getting rate-limited by the client (429 status code), do:
            1. Get the retry-after value in seconds
            2. Sleep for the seconds specified. If Retry-After value not available, does not sleep
            3. Try to get the game data again
            4. If no more attempts available, throw exception
             */
            log.debug("Retry-After: {} seconds", e.getResponseHeaders() != null ? e.getResponseHeaders().getFirst("Retry-After") : "");
            int retryAfterSeconds = 0;
            if (e.getResponseHeaders() != null && e.getResponseHeaders().getFirst("Retry-After") != null) {
                try {
                    retryAfterSeconds = Integer.parseUnsignedInt(Objects.requireNonNull(e.getResponseHeaders().getFirst("Retry-After")));
                } catch (final NumberFormatException numberFormatException) {
                    log.debug("No retry-after seconds provided");
                }
            }
            if (attempts <= 0) {
                log.error("429 error never got resolved.", e);
                return null;
            }
            log.debug("429 error occurred. Attempts left: {}", attempts);
            try {
                log.debug("Sleeping for {} seconds", retryAfterSeconds);
                Thread.sleep(1000L * retryAfterSeconds);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            return getGameData(seasonType, year, gameId, attempts - 1);
        }
        final ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(result.getBody(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
