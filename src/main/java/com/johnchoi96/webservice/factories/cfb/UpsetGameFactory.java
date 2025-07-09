package com.johnchoi96.webservice.factories.cfb;

import com.johnchoi96.webservice.models.cfb.game_data.GameDataResponseItem;
import com.johnchoi96.webservice.models.cfb.upset_game.UpsetGame;
import com.johnchoi96.webservice.models.cfb.upset_game.UpsetGameResponse;
import com.johnchoi96.webservice.models.cfb.win_probability.WinProbabilityResponseItem;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class UpsetGameFactory {

    public UpsetGame build(final Integer homeRank, final Integer awayRank, final String upsetType,
                           final GameDataResponseItem gameDetail, final WinProbabilityResponseItem probability) {
        final UpsetGame.UpsetGameBuilder builder = UpsetGame.builder()
                .homeRank(homeRank)
                .awayRank(awayRank)
                .upsetType(upsetType)
                .preGameHomeWinProbability(probability.getHomeWinProb())
                .preGameAwayWinProbability(1 - probability.getHomeWinProb())
                .homeTeamName(gameDetail.getHomeTeam())
                .awayTeamName(gameDetail.getAwayTeam())
                .homePoints(gameDetail.getHomePoints())
                .awayPoints(gameDetail.getAwayPoints())
                .year(gameDetail.getSeasonYear())
                .seasonType(gameDetail.getSeasonType())
                .location(gameDetail.getVenue())
                .bowlName(gameDetail.getNotes())
                .timestamp(gameDetail.getStartDate())
                .neutralSite(gameDetail.getIsNeutralSite())
                .conferenceGame(gameDetail.getIsConferenceGame());
        if (gameDetail.getHomePoints() < gameDetail.getAwayPoints()) {
            builder.winningTeamName(gameDetail.getAwayTeam());
        } else {
            builder.winningTeamName(gameDetail.getHomeTeam());
        }
        if (!gameDetail.getSeasonType().equals("regular")) {
            builder.week(null);
        } else {
            builder.week(gameDetail.getWeek());
        }
        return builder.build();
    }

    public UpsetGameResponse buildUpsetGameResponse(final List<UpsetGame> upsetGames, final int totalGamesCount) {
        int rankUpsets = 0, predictionUpsets = 0;
        for (final UpsetGame game : upsetGames) {
            switch (game.getUpsetType()) {
                case "both" -> {
                    rankUpsets++;
                    predictionUpsets++;
                }
                case "rank" -> rankUpsets++;
                case "prediction" -> predictionUpsets++;
                default ->
                        throw new IllegalStateException("Unknown upset type: " + game.getUpsetType());
            }
        }
        return UpsetGameResponse.builder()
                .upsetGames(upsetGames)
                .upsetGamesCount(upsetGames.size())
                .totalGamesCount(totalGamesCount)
                .rankUpsetCount(rankUpsets)
                .predictionUpsetCount(predictionUpsets)
                .build();
    }
}
