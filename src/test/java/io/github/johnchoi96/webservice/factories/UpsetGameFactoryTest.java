package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.factories.cfb.UpsetGameFactory;
import io.github.johnchoi96.webservice.models.cfb.upset_game.UpsetGame;
import io.github.johnchoi96.webservice.models.cfb.upset_game.UpsetGameResponse;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UpsetGameFactoryTest {

    @Test
    void testBuildUpsetGameResponse() {
        final UpsetGame game1 = getDummyUpsetGame("123", "rank", "regular");
        final UpsetGame game2 = getDummyUpsetGame("345", "prediction", "postseason");
        final UpsetGame game3 = getDummyUpsetGame("5122", "both", "postseason");
        final UpsetGame game4 = getDummyUpsetGame("5191", "rank", "postseason");
        final List<UpsetGame> upsetGames = Arrays.asList(game1, game2, game3, game4);
        final int totalGamesCount = 10;

        final UpsetGameResponse upsetGameResponse = UpsetGameFactory.buildUpsetGameResponse(upsetGames, totalGamesCount);

        assertNotNull(upsetGameResponse);
        assertEquals(upsetGames, upsetGameResponse.getUpsetGames());
        assertEquals(upsetGames.size(), upsetGameResponse.getUpsetGamesCount());
        assertEquals(totalGamesCount, upsetGameResponse.getTotalGamesCount());
        assertEquals(3, upsetGameResponse.getRankUpsetCount());
        assertEquals(2, upsetGameResponse.getPredictionUpsetCount());
    }

    @Test
    void testBuildUpsetGameResponseInvalidUpsetType() {
        final UpsetGame game1 = getDummyUpsetGame("123", "rank", "regular");
        final UpsetGame game2 = getDummyUpsetGame("345", "prediction", "postseason");
        final UpsetGame game3 = getDummyUpsetGame("5122", "invalid-invalid", "postseason");
        final UpsetGame game4 = getDummyUpsetGame("5191", "rank", "postseason");
        final List<UpsetGame> upsetGames = Arrays.asList(game1, game2, game3, game4);
        final int totalGamesCount = 10;

        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> UpsetGameFactory.buildUpsetGameResponse(upsetGames, totalGamesCount)
        );
        assertTrue(exception.getMessage().contains("Unknown upset type: invalid-invalid"));
    }

    private UpsetGame getDummyUpsetGame(final String randomWord, final String upsetType, final String seasonType) {
        return UpsetGame.builder()
                .location("dummy-location" + randomWord)
                .bowlName("dummy-bowl-name" + randomWord)
                .winningTeamName("dummy-winning-team" + randomWord)
                .homeTeamName("dummy-home-team-name" + randomWord)
                .awayTeamName("dummy-away-team-name" + randomWord)
                .homeRank(1)
                .awayRank(2)
                .preGameHomeWinProbability(0.81F)
                .preGameAwayWinProbability(0.41F)
                .upsetType(upsetType)
                .homePoints(41)
                .awayPoints(42)
                .week(1)
                .year(2023)
                .seasonType(seasonType)
                .timestamp(Instant.now())
                .neutralSite(true)
                .conferenceGame(false)
                .build();
    }
}
