package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.utils.InstantUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

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

    /**
     * Sundays, Mondays, Wednesdays, and Fridays at 9am in EST.
     *
     * @throws JsonProcessingException    if JSON parsing went wrong
     * @throws FirebaseMessagingException if sending push notification failed
     */
    @Scheduled(cron = "0 0 9 ? * SUN,MON,WED,FRI", zone = InstantUtil.TIMEZONE_US_EAST)
    public void findDogsNear43235() throws JsonProcessingException, FirebaseMessagingException {
        if (schedulerEnabled) {
            log.info("Starting job for findDogsNear43235()");
            final Integer limit = 100;
            petfinderService.findFilteredDogsAndNotify(limit);
            log.info("Finished job for findDogsNear43235()");
        }
    }

    /**
     * Mon-Fri at 10am in EST.
     *
     * @throws JsonProcessingException    if JSON parsing went wrong
     * @throws FirebaseMessagingException if sending push notification failed
     */
    @Scheduled(cron = "0 0 10 ? * MON,TUE,WED,THU,FRI", zone = InstantUtil.TIMEZONE_US_EAST)
    public void fetchGoldPriceInfo() throws JsonProcessingException, FirebaseMessagingException {
        if (schedulerEnabled) {
            log.info("Starting job for fetchGoldPriceInfo()");
            metalPriceService.analyzeGoldPriceAndNotify(LocalDate.now());
            log.info("Finished job for fetchGoldPriceInfo()");
        }
    }

    /**
     * 1st day of every month at 4am EST.
     *
     * @throws ExecutionException   if Firebase operation had issues
     * @throws InterruptedException if Firebase operation had issues
     */
    @Scheduled(cron = "0 0 4 1 1/1 ?", zone = InstantUtil.TIMEZONE_US_EAST)
    public void deleteOldNotificationsInCloudFirestore() throws ExecutionException, InterruptedException {
        if (schedulerEnabled) {
            final int DAYS = 60;
            log.info("Starting job for deleteOldNotificationsInCloudFirestore(). Days set for {}", DAYS);
            cloudFirestoreService.deleteNotificationsOlderThanDays(DAYS);
            log.info("Finished job for deleteOldNotificationsInCloudFirestore()");
        }
    }

    /**
     * At 11:00AM on Sundays.
     *
     * @throws JsonProcessingException    if JSON to DTO parsing went wrong
     * @throws FirebaseMessagingException if sending notification failed
     */
    @Scheduled(cron = "0 0 11 * * SUN", zone = InstantUtil.TIMEZONE_US_EAST)
    public void runCurrentWeeksCfbUpsetReport() throws JsonProcessingException, FirebaseMessagingException {
        if (schedulerEnabled) {
            log.info("Starting job for runCurrentWeeksCfbUpsetReport()");
            cfbService.runUpsetReport();
            log.info("Finished job for runCurrentWeeksCfbUpsetReport()");
        }
    }
}
