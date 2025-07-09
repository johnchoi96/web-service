package com.johnchoi96.webservice.models.cfb.upset_game;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UpsetGameResponse {
    private List<UpsetGame> upsetGames;

    private int upsetGamesCount;

    private int totalGamesCount;

    private int rankUpsetCount;

    private int predictionUpsetCount;
}
