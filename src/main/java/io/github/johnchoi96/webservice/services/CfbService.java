package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.CfbClient;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.models.cfb.UpsetGame;
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

    private Instant cachedTimestamp;

    private List<RankingResponseItem> completeRankingList;

    /**
     * Collects upset games for the current time and sends a push notification with the constructed report.
     *
     * @throws JsonProcessingException    if JSON parsing went bad
     * @throws FirebaseMessagingException if push notification failed
     */
    public void triggerUpsetReport() throws JsonProcessingException, FirebaseMessagingException {
        log.info("Starting CfbService.triggerUpsetReport()");
        final CalendarResponseItem currentWeek = getCurrentWeek(Instant.now());
        if (currentWeek == null) {
            log.info("Current week not found. Stopping execution.");
            return;
        }
        final List<UpsetGame> upsetGames = collectUpsetGames(Instant.now());
        final StringBuilder notificationContent = FCMBodyFactory.buildBodyForCfbUpset(currentWeek.getSeasonType(), currentWeek.getWeek(), upsetGames);
        final String notificationTitle = "This week's CFB upset report is ready.";
        final String notificationSubtitle = "Tap to see this week's CFB upsets.";
        fcmService.sendNotification(FCMTopic.CFB, notificationTitle, notificationSubtitle, notificationContent, true, false);
        log.info("Finished CfbService.triggerUpsetReport()");
    }

    /**
     * Returns the list of upset games for the currentTime passed in.
     *
     * @param currentTime date to look for
     * @return list of upset games and their metadata
     * @throws JsonProcessingException if JSON parsing went bad
     */
    public List<UpsetGame> collectUpsetGames(final Instant currentTime) throws JsonProcessingException {
        // make sure current time is during the football season
        final CalendarResponseItem currentWeek = getCurrentWeek(currentTime);
        if (currentWeek == null) {
            log.info("Current week not found. Stopping execution.");
            return null;
        }
        final List<WinProbabilityResponseItem> winProbabilities = cfbClient.getPregameWinProbabilityDataForWeek(currentWeek);
        final List<UpsetGame> upsetGames = getUpsetGames(winProbabilities);
        if (upsetGames.isEmpty()) {
            log.info("No games were upset this week.");
            return null;
        }
        return upsetGames;
    }

    /**
     * Searches the list of current or last year's season calendar and returns the CalendarResponseItem that matches
     * the current time. Returns null if current time is not within any season.
     *
     * @param currentTime to look for
     * @return calendar response item that corresponds to the current time, null if not found
     * @throws JsonProcessingException if JSON parsing went bad
     */
    private CalendarResponseItem getCurrentWeek(final Instant currentTime) throws JsonProcessingException {
        var currentDate = InstantUtil.getDateObject(currentTime);
        List<CalendarResponseItem> calendar = cfbClient.getCurrentSeasonCalendar(currentDate.year());
        CalendarResponseItem currentWeek = findCurrentWeek(calendar, currentTime);
        if (currentWeek == null) {
            calendar = cfbClient.getCurrentSeasonCalendar(currentDate.year() - 1);
            currentWeek = findCurrentWeek(calendar, currentTime);
        }
        return currentWeek;
    }

    /**
     * Returns the cached complete ranking list for the entire year. If any of the following conditions is true, the
     * list gets updated.
     * 1. First time attempting to retrieve the complete list.
     * 2. The first element's season year doesn't match the passed in year.
     * 3. The cached list is more than a day old.
     *
     * @param year for the complete ranking list
     * @return the complete rank list for the entire season. Can be null.
     * @throws JsonProcessingException if JSON parsing went bad
     */
    private List<RankingResponseItem> getCompleteRankingList(final int year) throws JsonProcessingException {
        if (completeRankingList == null) {
            completeRankingList = cfbClient.getAllRankings(year);
            cachedTimestamp = Instant.now();
        }
        if (!completeRankingList.isEmpty() && completeRankingList.get(0).getSeason() != year) {
            completeRankingList = cfbClient.getAllRankings(year);
            cachedTimestamp = Instant.now();
        }
        // check if completeRankingList is more than a day old
        final Instant current = Instant.now();
        if (InstantUtil.getDifferenceInDays(current, cachedTimestamp) >= 1) {
            completeRankingList = cfbClient.getAllRankings(year);
            cachedTimestamp = Instant.now();
        }
        return completeRankingList;
    }

    private List<UpsetGame> getUpsetGames(final List<WinProbabilityResponseItem> list) {
        final List<UpsetGame> upsets = new ArrayList<>();
        list.forEach(probability -> {
            log.info("Collecting game data for game ID: {}", probability.getGameId());
            var gameDetail = cfbClient.getGameData(probability.getSeasonType(), probability.getGameId()).get(0);
            if (gameDetail.isCompleted() && isUpset(probability, gameDetail)) {
                final UpsetGame game = UpsetGame.builder()
                        .homeRank(getTeamRankForWeek(gameDetail.getHomeTeam(), gameDetail))
                        .awayRank(getTeamRankForWeek(gameDetail.getAwayTeam(), gameDetail))
                        .preGameHomeWinProbability(probability.getHomeWinProb())
                        .homeTeamName(gameDetail.getHomeTeam())
                        .awayTeamName(gameDetail.getAwayTeam())
                        .homePoints(gameDetail.getHomePoints())
                        .awayPoints(gameDetail.getAwayPoints())
                        .week(gameDetail.getWeek())
                        .year(gameDetail.getSeasonYear())
                        .seasonType(gameDetail.getSeasonType())
                        .location(gameDetail.getVenue())
                        .bowlName(gameDetail.getNotes())
                        .timestamp(gameDetail.getStartDate())
                        .neutralSite(gameDetail.isNeutralSite())
                        .conferenceGame(gameDetail.isConferenceGame())
                        .build();
                upsets.add(game);
            }
        });
        log.info("Finished collecting all upset game data");
        return upsets;
    }

    /**
     * Determines if the game is considered an upset.
     * Returns true if any of the following conditions is met:
     * 1. Home team had lower chance of winning but won the game.
     * 2. Home team had higher chance of winning but lost the game.
     *
     * @param winProbabilityResponseItem pre game win probability information
     * @param gameDataResponseItem       match information
     * @return true if the game is considered an upset
     */
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

    /**
     * Returns a historical rank at the time of the game for the team, if exists.
     * Checks 3 different polls in this specific order: "Playoff Committee Rankings", "AP Top 25", "Coaches Poll".
     * For example, if Ohio State is #3 in Playoff Committee ranking and #1 in AP Poll, 3 would get returned.
     * If a poll is not available, it would look in the next available poll in the order.
     *
     * @param team                 to retrieve rank for
     * @param gameDataResponseItem match metadata
     * @return rank or null if team is unranked
     */
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
                if (allRankings == null || allRankings.isEmpty()) return null;
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
        final List<PollsItem> polls = response.get(responseIndex).getPolls();
        final List<String> pollList = List.of("Playoff Committee Rankings", "AP Top 25", "Coaches Poll");
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
                log.info("Rank found: #{} {}", rank.getRank(), team);
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