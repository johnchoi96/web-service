package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SchedulerService {

    @Autowired
    private PetfinderService petfinderService;


    // Sundays, Wednesdays, and Fridays at 3pm in EST
    @Scheduled(cron = "0 0 15 ? * SUN,WED,FRI", zone = "America/New_York")
    public void findDogsNear43235() throws JsonProcessingException {
        log.info("Starting job for findDogsNear43235()");
        petfinderService.findFilteredDogsAndReport();
        log.info("Finished job for findDogsNear43235()");
    }
}
