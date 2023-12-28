package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.CfbClient;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.models.cfb.calendar.CalendarResponseItem;
import io.github.johnchoi96.webservice.models.cfb.game_data.GameDataResponseItem;
import io.github.johnchoi96.webservice.models.cfb.rankings.PollsItem;
import io.github.johnchoi96.webservice.models.cfb.rankings.RankingResponseItem;
import io.github.johnchoi96.webservice.models.cfb.win_probability.WinProbabilityResponseItem;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.utils.InstantUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CfbService {

    private final CfbClient cfbClient;

    private final FCMService fcmService;

    private List<RankingResponseItem> completeRankingList;

    public void runUpsetReport() throws JsonProcessingException, FirebaseMessagingException {
        log.info("Starting CfbService.runUpsetReport()");
        // make sure current time is during the football season
        var currentDate = InstantUtil.getDate(Instant.now());
        List<CalendarResponseItem> calendar = cfbClient.getCurrentSeasonCalendar(currentDate.year());
        CalendarResponseItem currentWeek = findCurrentWeek(calendar, Instant.now());
        if (currentWeek == null) {
            calendar = cfbClient.getCurrentSeasonCalendar(currentDate.year() - 1);
            currentWeek = findCurrentWeek(calendar, Instant.now());
            if (currentWeek == null) {
                log.info("Current week not found. Stopping execution.");
                return;
            }
        }
        final List<WinProbabilityResponseItem> winProbabilities = cfbClient.getPregameWinProbabilityDataForWeek(currentWeek);
        final List<GameDataResponseItem> upsetGames = getUpsetGames(winProbabilities);
        if (upsetGames.isEmpty()) {
            log.info("No games were upset this week.");
            return;
        }
        final StringBuilder notificationContent = FCMBodyFactory.buildBodyForCfbUpset(currentWeek.getSeasonType(), currentWeek.getWeek(), upsetGames);
        final String notificationTitle = "This week's CFB upset report is ready.";
        final String notificationSubtitle = "Tap to see this week's CFB upsets.";
        fcmService.sendNotification(FCMTopic.CFB, notificationTitle, notificationSubtitle, notificationContent, true, false);
        log.info("Finished CfbService.runUpsetReport()");
    }

    private List<RankingResponseItem> getCompleteRankingList(final int year) throws JsonProcessingException {
        if (completeRankingList == null) {
            completeRankingList = cfbClient.getAllRankings(year);
        }
        if (!completeRankingList.isEmpty() && completeRankingList.get(0).getSeason() != year) {
            completeRankingList = cfbClient.getAllRankings(year);
        }
        return completeRankingList;
    }

    private List<GameDataResponseItem> getUpsetGames(final List<WinProbabilityResponseItem> list) {
        final List<GameDataResponseItem> upsets = new ArrayList<>();
        list.forEach(probability -> {
            log.info("Pulling game data for game ID: {}", probability.getGameId());
            var gameDetail = cfbClient.getGameData(probability.getSeasonType(), probability.getGameId()).get(0);
            if (gameDetail.isCompleted() && isUpset(probability, gameDetail)) {
                // get rank for home and away teams
                gameDetail.setAwayRank(getTeamRankForWeek(gameDetail.getAwayTeam(), gameDetail));
                gameDetail.setHomeRank(getTeamRankForWeek(gameDetail.getHomeTeam(), gameDetail));
                // save pregame win probabilities
                gameDetail.setPreGameHomeWinProbability(probability.getHomeWinProb());
                upsets.add(gameDetail);
            }
        });
        log.info("Finished pulling all upset game data");
        return upsets;
    }

    private boolean isUpset(final WinProbabilityResponseItem winProbabilityResponseItem, final GameDataResponseItem gameDataResponseItem) {
        return (winProbabilityResponseItem.getHomeWinProb() > 0.5 && gameDataResponseItem.getAwayPoints() > gameDataResponseItem.getHomePoints()) ||
                (winProbabilityResponseItem.getHomeWinProb() <= 0.5 && gameDataResponseItem.getAwayPoints() <= gameDataResponseItem.getHomePoints());
    }

    private CalendarResponseItem findCurrentWeek(@NonNull final List<CalendarResponseItem> entries, Instant current) {
        var week = entries.stream()
                .filter(entry -> currentIsInRange(entry.getFirstGameStart(), entry.getLastGameStart(), current))
                .findFirst();
        return week.orElse(null);
    }

    private Integer getTeamRankForWeek(final String team, final GameDataResponseItem gameDataResponseItem) {
        List<RankingResponseItem> response;
        try {
            response = cfbClient.getRankingForWeek(
                    gameDataResponseItem.getSeasonYear(),
                    gameDataResponseItem.getWeek(),
                    gameDataResponseItem.getSeasonType()
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        int responseIndex = 0;
        if (response.isEmpty()) {
            try {
                var allRankings = getCompleteRankingList(gameDataResponseItem.getSeasonYear());
                // find the latest ranking during regular season
                int weekNum = 1;
                for (int i = 0; i < allRankings.size(); i++) {
                    var ranking = allRankings.get(i);
                    if (ranking.getSeasonType().equals("regular") && ranking.getWeek() > weekNum) {
                        weekNum = ranking.getWeek();
                        responseIndex = i;
                    }
                }
                response = allRankings;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        List<PollsItem> polls = response.get(responseIndex).getPolls();
        var pollList = List.of("Playoff Committee Rankings", "AP Top 25", "Coaches Poll");
        final Map<String, Integer> pollIndices = new HashMap<>();
        pollList.forEach(poll -> pollIndices.put(poll, -1));
        for (int i = 0; i < polls.size(); i++) {
            if (pollIndices.containsKey(polls.get(i).getPoll())) {
                pollIndices.replace(polls.get(i).getPoll(), i);
            }
        }
        Integer index = null;
        for (final String pollName : pollList) {
            if (pollIndices.get(pollName) != -1) {
                index = pollIndices.get(pollName);
                break;
            }
        }
        if (index == null) {
            log.info("Acceptable poll list wasn't found in the response");
            return null;
        }
        var selectedPollList = polls.get(index);
        for (var rank : selectedPollList.getRanks()) {
            if (rank.getSchool().equals(team)) {
                return rank.getRank();
            }
        }
        log.info("Rank not found for team: {}", team);
        return null;
    }

    /**
     * Returns true if current time is between start and end.
     *
     * @param start   starting time
     * @param end     ending time
     * @param current current time. If null, Instant.now() is used.
     * @return true if between range
     */
    private boolean currentIsInRange(@NonNull final Instant start, @NonNull final Instant end, Instant current) {
        if (current == null) {
            current = Instant.now();
        }
        return current.isAfter(start) && current.isBefore(end);
    }
}
