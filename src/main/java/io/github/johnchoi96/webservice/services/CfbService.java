package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.CfbClient;
import io.github.johnchoi96.webservice.entities.cfb.CfbSeasonTypeEntity;
import io.github.johnchoi96.webservice.entities.cfb.CfbTeamEntity;
import io.github.johnchoi96.webservice.entities.cfb.CfbUpsetEntity;
import io.github.johnchoi96.webservice.entities.cfb.CfbUpsetTypeEntity;
import io.github.johnchoi96.webservice.entities.cfb.CfbWeekSummaryEntity;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.factories.cfb.UpsetGameFactory;
import io.github.johnchoi96.webservice.models.cfb.api_response.CfbUpsetMatchResponse;
import io.github.johnchoi96.webservice.models.cfb.calendar.CalendarResponseItem;
import io.github.johnchoi96.webservice.models.cfb.game_data.GameDataResponseItem;
import io.github.johnchoi96.webservice.models.cfb.rankings.PollsItem;
import io.github.johnchoi96.webservice.models.cfb.rankings.RankingResponseItem;
import io.github.johnchoi96.webservice.models.cfb.upset_game.UpsetGame;
import io.github.johnchoi96.webservice.models.cfb.upset_game.UpsetGameResponse;
import io.github.johnchoi96.webservice.models.cfb.win_probability.WinProbabilityResponseItem;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.repos.cfb.CfbSeasonTypeRepo;
import io.github.johnchoi96.webservice.repos.cfb.CfbTeamRepo;
import io.github.johnchoi96.webservice.repos.cfb.CfbUpsetRepo;
import io.github.johnchoi96.webservice.repos.cfb.CfbUpsetTypeRepo;
import io.github.johnchoi96.webservice.repos.cfb.CfbWeekSummaryRepo;
import io.github.johnchoi96.webservice.utils.InstantUtil;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CfbService {

    private final CfbClient cfbClient;

    private final FCMService fcmService;

    private final CfbWeekSummaryRepo cfbWeekSummaryRepo;

    private final CfbUpsetRepo cfbUpsetRepo;

    private final CfbTeamRepo cfbTeamRepo;

    private final CfbUpsetTypeRepo cfbUpsetTypeRepo;

    private final CfbSeasonTypeRepo cfbSeasonTypeRepo;

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
        final CfbUpsetMatchResponse response = getCfbUpsetMatches(Instant.now());
        if (response == null) {
            log.error("Could not fetch the upset match response.");
            return;
        }
        final StringBuilder notificationContent = FCMBodyFactory.buildBodyForCfbUpset(response.getWeekSummary().getSeasonType(), response.getWeekSummary().getWeek(), response);
        final String notificationTitle = "This week's CFB upset report is ready.";
        final String notificationSubtitle = "Tap to see this week's CFB upsets.";
        fcmService.sendNotification(FCMTopic.CFB, notificationTitle, notificationSubtitle, notificationContent, true, false);
        log.info("Finished CfbService.triggerUpsetReport()");
    }

    /**
     * Looks up CFB upset matches by Instant, if not found, calls API.
     *
     * @param time of CFB week
     * @return response
     * @throws JsonProcessingException if API call went wrong
     */
    public CfbUpsetMatchResponse getCfbUpsetMatches(final Instant time) throws JsonProcessingException {
        return getCfbUpsetMatches(time, 1);
    }

    private CfbUpsetMatchResponse getCfbUpsetMatches(final Instant time, final int tries) throws JsonProcessingException {
        // get week summary
        Optional<CfbWeekSummaryEntity> weekSummaryEntity = cfbWeekSummaryRepo.getCfbWeekSummary(time);
        // check the DB for the past 5 days
        if (weekSummaryEntity.isEmpty()) {
            for (int i = 1; i < 5; i++) {
                weekSummaryEntity = cfbWeekSummaryRepo.getCfbWeekSummary(time.minus(Duration.ofDays(i)));
                if (weekSummaryEntity.isPresent()) {
                    break;
                }
            }
        }

        if (weekSummaryEntity.isEmpty()) {
            // we never queried for this week so call the API and insert to DB
            final CalendarResponseItem currentWeek = getCurrentWeek(time);
            if (currentWeek == null) {
                log.info("Current week is not a CFB week. Stopping execution.");
                return null;
            }
            if (tries > 5) {
                return null;
            }
            collectUpsetGames(currentWeek.getLastGameStart());
            return getCfbUpsetMatches(time, tries + 1);
        }
        if (!weekSummaryEntity.get().isFinalized()) {
            final CalendarResponseItem currentWeek = getCurrentWeek(time);
            if (currentWeek == null) {
                log.info("Current week is not finalized and current week is not a CFB week. Stopping execution.");
                return null;
            }
            collectUpsetGames(time);
        }

        final List<CfbUpsetEntity> upsetGames = cfbUpsetRepo.getUpsetMatches(weekSummaryEntity.get());
        return CfbUpsetMatchResponse.builder().upsetGames(upsetGames).weekSummary(weekSummaryEntity.get()).build();
    }

    /**
     * Looks up CFB upset matches by week and year. If not found, calls API.
     *
     * @param week of the CFB season
     * @param year of the CFB season
     * @return response
     */
    public CfbUpsetMatchResponse getCfbUpsetMatches(final int week, final int year) throws JsonProcessingException {
        if (year > Year.now().getValue()) {
            return null;
        }
        // get week summary
        Optional<CfbWeekSummaryEntity> weekSummaryEntity = cfbWeekSummaryRepo.getCfbWeekSummary(week, year);
        if (weekSummaryEntity.isEmpty()) {
            final List<CalendarResponseItem> response = cfbClient.getCurrentSeasonCalendar(year);
            int left = 0, right = response.size();
            while (left < right) {
                final int midpoint = left + (right - left) / 2;
                if (response.get(midpoint).getWeek() == week) {
                    collectUpsetGames(response.get(midpoint).getLastGameStart());
                    break;
                }
                if (response.get(midpoint).getWeek() < week) {
                    left = midpoint + 1;
                } else {
                    right = midpoint;
                }
            }
            if (left >= right) {
                return null;
            }
            weekSummaryEntity = cfbWeekSummaryRepo.getCfbWeekSummary(week, year);
            if (weekSummaryEntity.isEmpty()) {
                log.info("No upset game for this week in DB.");
                return null;
            }
        }
        if (!weekSummaryEntity.get().isFinalized()) {
            collectUpsetGames(weekSummaryEntity.get().getEnd());
        }
        final List<CfbUpsetEntity> upsetGames = cfbUpsetRepo.getUpsetMatches(week, year);
        return CfbUpsetMatchResponse.builder().upsetGames(upsetGames).weekSummary(weekSummaryEntity.orElse(null)).build();
    }

    /**
     * Calls the CFB API and inserts data to the DB
     *
     * @param currentTime date to look for
     * @throws JsonProcessingException if JSON parsing went bad
     */
    @Transactional
    public void collectUpsetGames(final Instant currentTime) throws JsonProcessingException {
        // make sure current time is during the football season
        final CalendarResponseItem currentWeek = getCurrentWeek(currentTime);
        if (currentWeek == null) {
            log.info("Current week not found. Stopping execution.");
            return;
        }
        // insert to the CFB_WEEK_SUMMARY table
        final Optional<CfbWeekSummaryEntity> optionalCfbWeek = cfbWeekSummaryRepo.getCfbWeekSummary(currentWeek.getWeek(), currentWeek.getSeasonYear());
        if (optionalCfbWeek.isPresent() && optionalCfbWeek.get().isFinalized()) {
            log.info("Current week already in the DB. Stopping execution.");
            return;
        }
        final List<WinProbabilityResponseItem> winProbabilities = cfbClient.getPregameWinProbabilityDataForWeek(currentWeek);
        if (winProbabilities == null) return;
        final UpsetGameResponse upsetGames = getUpsetGames(winProbabilities);
        if (upsetGames.getUpsetGamesCount() == 0) {
            log.info("No games were upset this week.");
            return;
        }

        final Optional<CfbSeasonTypeEntity> seasonTypeEntity = cfbSeasonTypeRepo.getSeasonType(currentWeek.getSeasonType());
        final CfbWeekSummaryEntity cfbWeek = optionalCfbWeek.orElseGet(() -> cfbWeekSummaryRepo.save(CfbWeekSummaryEntity.builder()
                .week(currentWeek.getWeek())
                .year(currentWeek.getSeasonYear())
                .upsetMatchCount(upsetGames.getUpsetGamesCount())
                .totalMatchCount(upsetGames.getTotalGamesCount())
                .rankUpsetCount(upsetGames.getRankUpsetCount())
                .predictionUpsetCount(upsetGames.getPredictionUpsetCount())
                .seasonType(seasonTypeEntity.orElseGet(() -> cfbSeasonTypeRepo.save(CfbSeasonTypeEntity.builder()
                        .seasonType(currentWeek.getSeasonType())
                        .build())))
                .start(currentWeek.getFirstGameStart())
                .end(currentWeek.getLastGameStart())
                .build()));
        // insert matches
        final List<CfbUpsetEntity> upsetMatchesToInsert = new ArrayList<>();
        upsetGames.getUpsetGames().forEach(match -> {
            // check if home team exists
            final Optional<CfbTeamEntity> optionalHomeTeam = cfbTeamRepo.getTeamWithName(match.getHomeTeamName());
            final CfbTeamEntity homeTeam = optionalHomeTeam.orElseGet(() -> cfbTeamRepo.save(CfbTeamEntity.builder()
                    .teamName(match.getHomeTeamName()).build()));
            // check if away team exists
            final Optional<CfbTeamEntity> optionalAwayTeam = cfbTeamRepo.getTeamWithName(match.getAwayTeamName());
            final CfbTeamEntity awayTeam = optionalAwayTeam.orElseGet(() -> cfbTeamRepo.save(CfbTeamEntity.builder()
                    .teamName(match.getAwayTeamName()).build()));
            CfbUpsetEntity cfbUpsetEntity = cfbUpsetRepo.getUpsetMatch(homeTeam, awayTeam, cfbWeek);
            if (cfbUpsetEntity == null) {
                cfbUpsetEntity = new CfbUpsetEntity();
                cfbUpsetEntity.setHomeTeam(homeTeam);
                cfbUpsetEntity.setAwayTeam(awayTeam);
                cfbUpsetEntity.setCfbWeek(cfbWeek);
            }
            cfbUpsetEntity.setLocation(match.getLocation());
            cfbUpsetEntity.setBowlName(match.getBowlName());
            final Optional<CfbTeamEntity> winningTeam = cfbTeamRepo.getTeamWithName(match.getWinningTeamName());
            winningTeam.ifPresent(cfbUpsetEntity::setWinningTeam);
            cfbUpsetEntity.setHomeRank(match.getHomeRank());
            cfbUpsetEntity.setAwayRank(match.getAwayRank());
            cfbUpsetEntity.setPreMatchHomeWinChance(match.getPreGameHomeWinProbability());
            cfbUpsetEntity.setPreMatchAwayWinChance(match.getPreGameAwayWinProbability());
            final Optional<CfbUpsetTypeEntity> upsetTypeEntity = cfbUpsetTypeRepo.getUpsetType(match.getUpsetType());
            upsetTypeEntity.ifPresent(cfbUpsetEntity::setUpsetType);
            cfbUpsetEntity.setHomeScore(match.getHomePoints());
            cfbUpsetEntity.setAwayScore(match.getAwayPoints());
            cfbUpsetEntity.setMatchTimestamp(match.getTimestamp());
            cfbUpsetEntity.setNeutralSite(match.getNeutralSite());
            cfbUpsetEntity.setConferenceGame(match.getConferenceGame());
            upsetMatchesToInsert.add(cfbUpsetEntity);
        });
        cfbUpsetRepo.saveAll(upsetMatchesToInsert);

        // NOTE: add 6 hours to the last game start of the week because game usually takes less than 6 hours to complete
        if (Instant.now().isAfter(cfbWeek.getEnd().plus(Duration.ofHours(6)))) {
            cfbWeek.setFinalized(true);
            cfbWeekSummaryRepo.save(cfbWeek);
        }
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
        if (calendar == null) return null;
        CalendarResponseItem currentWeek = findCurrentWeek(calendar, currentTime);
        if (currentWeek == null) {
            calendar = cfbClient.getCurrentSeasonCalendar(currentDate.year() - 1);
            if (calendar == null) return null;
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
        if (!completeRankingList.isEmpty() && completeRankingList.getFirst().getSeason() != year) {
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

    private UpsetGameResponse getUpsetGames(final List<WinProbabilityResponseItem> list) {
        final List<UpsetGame> upsets = new ArrayList<>();
        list.forEach(probability -> {
            log.info("Collecting game data for game ID: {}", probability.getGameId());
            var gameDetail = cfbClient.getGameData(probability.getSeasonType(), probability.getSeasonYear(), probability.getGameId()).getFirst();
            if (gameDetail == null) return;
            var homeRank = getTeamRankForWeek(gameDetail.getHomeTeam(), gameDetail);
            var awayRank = getTeamRankForWeek(gameDetail.getAwayTeam(), gameDetail);
            var upsetType = isUpset(probability, gameDetail, homeRank, awayRank);
            if (gameDetail.isCompleted() && upsetType != null) {
                final UpsetGame game = UpsetGameFactory.build(homeRank, awayRank, upsetType, gameDetail, probability);
                upsets.add(game);
            }
        });
        log.info("Finished collecting all upset game data");
        return UpsetGameFactory.buildUpsetGameResponse(upsets, list.size());
    }

    /**
     * Determines if the game is considered an upset.
     * Returns one of "rank", "prediction", "both", or null if any of the following conditions is met:
     * 1. Home team had lower chance of winning but won the game.
     * 2. Home team had higher chance of winning but lost the game.
     * 3. Home team's rank was higher than away's rank but lost the game.
     * 4. Home team's rank was lower than away's but won the game.
     * 5. Home team was unranked, away team was ranked but home team won.
     * 6. Home team was ranked, away team was unranked but away team won.
     *
     * @param winProbabilityResponseItem pre game win probability information
     * @param gameDataResponseItem       match information
     * @param homeRank                   rank of home team
     * @param awayRank                   rank of away team
     * @return "rank" if the game is considered a rank upset, "prediction" if the game was a prediction upset,
     * "both" if both rank and prediction upset occurred, null if no upset
     */
    private String isUpset(
            final WinProbabilityResponseItem winProbabilityResponseItem,
            final GameDataResponseItem gameDataResponseItem,
            final Integer homeRank,
            final Integer awayRank
    ) {
        final Float homeWinProbability = winProbabilityResponseItem.getHomeWinProb();
        final int homePoints = gameDataResponseItem.getHomePoints();
        final int awayPoints = gameDataResponseItem.getAwayPoints();
        final String RANK_TYPE = "rank";
        final String PREDICTION_TYPE = "prediction";
        final String BOTH_TYPE = "both";
        String result;
        final boolean isRankUpset = isRankUpset(homeRank, awayRank, homePoints, awayPoints);
        final boolean isPredictionUpset = isPredictionUpset(homeWinProbability, homePoints, awayPoints);
        if (isRankUpset && isPredictionUpset) result = BOTH_TYPE;
        else if (isRankUpset) result = RANK_TYPE;
        else if (isPredictionUpset) result = PREDICTION_TYPE;
        else result = null;
        return result;
    }

    private boolean isRankUpset(final Integer homeRank, final Integer awayRank, final Integer homePoints, final Integer awayPoints) {
        // determine if there was a rank upset
        if (homeRank == null && awayRank != null && homePoints > awayPoints) {
            return true;
        }
        if (homeRank != null && awayRank == null && homePoints < awayPoints) {
            return true;
        }
        if (homeRank != null && awayRank != null) {
            // NOTE: higher the homeRank/awayRank value is, lower the actual rank
            return (homeRank < awayRank && homePoints < awayPoints) || (homeRank > awayRank && homePoints > awayPoints);
        }
        return false;
    }

    private boolean isPredictionUpset(final Float homeWinProbability, final Integer homePoints, final Integer awayPoints) {
        // determine if there was a prediction upset
        return (homeWinProbability > 0.5 && awayPoints > homePoints) || (homeWinProbability <= 0.5 && awayPoints <= homePoints);
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
        if (response == null) return null;
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
        if (current.isAfter(start) && current.isBefore(end)) {
            return true;
        }
        final int daysError = 3;
        return Math.min(InstantUtil.getDifferenceInDays(start, current), InstantUtil.getDifferenceInDays(end, current)) <= daysError;
    }
}
