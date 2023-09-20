package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {

    @Value("${scheduler.enabled}")
    private boolean schedulerEnabled;

    private final PetfinderService petfinderService;

    private final MetalPriceService metalPriceService;

    // Sundays, Mondays, Wednesdays, and Fridays at 9am in EST
    @Scheduled(cron = "0 0 9 ? * SUN,MON,WED,FRI", zone = "America/New_York")
    public void findDogsNear43235() throws JsonProcessingException {
        if (schedulerEnabled) {
            log.info("Starting job for findDogsNear43235()");
            final Integer limit = 100;
            petfinderService.findFilteredDogsAndReport(limit);
            log.info("Finished job for findDogsNear43235()");
        }
    }

    @Scheduled(cron = "0 0 10 ? * MON,TUE,WED,THU,FRI", zone = "America/New_York")
    public void fetchGoldPriceInfo() throws JsonProcessingException {
        if (schedulerEnabled) {
            log.info("Starting job for fetchGoldPriceInfo()");
            metalPriceService.analyzeGoldPriceAndReport(LocalDate.now());
            log.info("Finished job for fetchGoldPriceInfo()");
        }
    }
}
