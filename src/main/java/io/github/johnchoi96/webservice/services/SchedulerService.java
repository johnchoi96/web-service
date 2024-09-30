package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    @Value("${scheduler.enabled}")
    private boolean schedulerEnabled;

    private final PetfinderService petfinderService;

    private final MetalPriceService metalPriceService;

    private final CloudFirestoreService cloudFirestoreService;

    private final CfbService cfbService;

    private final EmailService emailService;

    /**
     * Sundays, Mondays, Wednesdays, and Fridays at 9am in EST.
     */
    @Scheduled(cron = "0 0 9 ? * SUN,MON,WED,FRI", zone = InstantUtil.TIMEZONE_US_EAST)
    public void findDogsNear43235() {
        if (schedulerEnabled) {
            log.info("Starting job for findDogsNear43235()");
            final Integer limit = 100;
            try {
                petfinderService.findFilteredDogsAndNotify(limit);
            } catch (JsonProcessingException | FirebaseMessagingException e) {
                emailService.notifyException(e);
            }
            log.info("Finished job for findDogsNear43235()");
        }
    }

    /**
     * Mon-Fri at 10am in EST.
     */
    @Scheduled(cron = "0 0 10 ? * MON,TUE,WED,THU,FRI", zone = InstantUtil.TIMEZONE_US_EAST)
    public void fetchGoldPriceInfo() {
        if (schedulerEnabled) {
            log.info("Starting job for fetchGoldPriceInfo()");
            try {
                metalPriceService.analyzeGoldPriceAndNotify(LocalDate.now());
            } catch (JsonProcessingException | FirebaseMessagingException e) {
                emailService.notifyException(e);
            }
            log.info("Finished job for fetchGoldPriceInfo()");
        }
    }

    /**
     * 1st day of every month at 4am EST.
     */
    @Scheduled(cron = "0 0 4 1 1/1 ?", zone = InstantUtil.TIMEZONE_US_EAST)
    public void deleteOldNotificationsInCloudFirestore() {
        if (schedulerEnabled) {
            final int DAYS = 60;
            log.info("Starting job for deleteOldNotificationsInCloudFirestore(). Days set for {}", DAYS);
            try {
                cloudFirestoreService.deleteNotificationsOlderThanDays(DAYS);
            } catch (ExecutionException | InterruptedException e) {
                emailService.notifyException(e);
            }
            log.info("Finished job for deleteOldNotificationsInCloudFirestore()");
        }
    }

    /**
     * At 11:00AM on Sundays.
     */
    @Scheduled(cron = "0 0 11 * * SUN", zone = InstantUtil.TIMEZONE_US_EAST)
    public void runCurrentWeeksCfbUpsetReport() {
        if (schedulerEnabled) {
            log.info("Starting job for runCurrentWeeksCfbUpsetReport()");
            try {
                cfbService.triggerUpsetReport();
            } catch (JsonProcessingException | FirebaseMessagingException e) {
                emailService.notifyException(e);
            }
            log.info("Finished job for runCurrentWeeksCfbUpsetReport()");
        }
    }

    @Scheduled(cron = "0 30 9 * * SUN", zone = InstantUtil.TIMEZONE_US_EAST)
    public void collectCfbUpsetMatches() {
        if (schedulerEnabled) {
            log.info("Starting job for collectCfbUpsetMatches");
            try {
                cfbService.collectUpsetGames(Instant.now());
            } catch (JsonProcessingException e) {
                emailService.notifyException(e);
            }
            log.info("Finished job for collectCfbUpsetMatches");
        }
    }

    /**
     * 1st day of every month at 3am EST.
     */
    @Scheduled(cron = "0 0 3 1 1/1 ?", zone = InstantUtil.TIMEZONE_US_EAST)
    public void deleteOldPetfinderLogs() {
        if (schedulerEnabled) {
            log.info("Starting job for deleteOldPetfinderLogs()");
            try {
                final int MONTHS = 2;
                petfinderService.deleteInactivePetfinderLogs(MONTHS);
            } catch (Exception e) {
                log.error("Error occurred while trying to delete petfinder logs", e);
                emailService.notifyException(e);
            }
            log.info("Finished job for deleteOldPetfinderLogs()");
        }
    }
}
