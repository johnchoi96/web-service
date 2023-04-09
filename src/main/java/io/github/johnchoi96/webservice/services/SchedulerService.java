package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.clients.PetfinderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SchedulerService {

    @Autowired
    private PetfinderClient petfinderClient;

    @Scheduled(cron = "* * 9 ? * SUN,WED,FRI *") // Sundays, Wednesdays, and Fridays at 9am
    public void findShibaInuIn43235() throws JsonProcessingException {
        petfinderClient.findShibaNear43235();
    }
}
