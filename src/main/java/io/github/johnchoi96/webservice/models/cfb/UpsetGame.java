package io.github.johnchoi96.webservice.models.cfb;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
public class UpsetGame {

    private String location;

    private String bowlName;

    private String winningTeamName;

    private Float preGameHomeWinProbability;

    private Float preGameAwayWinProbability;

    private String homeTeamName;

    private String awayTeamName;

    private Integer homeRank;

    private Integer awayRank;

    private int homePoints;

    private int awayPoints;

    private int week;

    private int year;

    private String seasonType;

    private Instant timestamp;

    private Boolean neutralSite;

    private Boolean conferenceGame;
}
