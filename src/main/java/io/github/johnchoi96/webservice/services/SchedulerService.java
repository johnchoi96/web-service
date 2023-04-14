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


    // Sundays, Wednesdays, and Fridays at 9am
    @Scheduled(cron = "0 0 9 ? * SUN,WED,FRI")
    public void findDogsNear43235() throws JsonProcessingException {
        log.info("Starting job for findDogsNear43235()");
        petfinderService.findFilteredDogsAndReport();
        log.info("Finished job for findDogsNear43235()");
    }
}
