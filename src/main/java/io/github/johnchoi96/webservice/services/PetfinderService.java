package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.PetfinderClient;
import io.github.johnchoi96.webservice.factories.FCMBodyFactory;
import io.github.johnchoi96.webservice.models.firebase.fcm.FCMTopic;
import io.github.johnchoi96.webservice.models.petfinder.filters.BreedFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.GermanShepherdDogFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.HuskyFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.JindoFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.SamoyedFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.ShibaInuFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.SiberianHuskyFilter;
import io.github.johnchoi96.webservice.models.petfinder.filters.WhiteGermanShepherdFilter;
import io.github.johnchoi96.webservice.models.petfinder.response.AnimalsItem;
import io.github.johnchoi96.webservice.models.petfinder.response.PetfinderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PetfinderService {

    private final PetfinderClient petfinderClient;

    private final EmailService emailService;

    private final FCMService fcmService;

    private final Set<String> breeds = Set.of(
            "Shiba Inu",
            "Husky",
            "Siberian Husky",
            "German Shepherd Dog",
            "White German Shepherd",
            "Jindo",
            "Samoyed"
    );

    public List<PetfinderResponse> findAllDogsNear43235() throws JsonProcessingException {
        return petfinderClient.findAllDogsNear43235(breeds);
    }

    public void findFilteredDogsAndNotify(final Integer limit) throws JsonProcessingException, FirebaseMessagingException {
        var filteredList = findFilteredDogs(limit);
        emailService.sendEmailForPetfinder(filteredList);
        final StringBuilder petfinderBody = FCMBodyFactory.buildBodyForPetfinder(filteredList);
        final String notificationTitle = "Message from Web Service for Petfinder";
        final String notificationBody = "Tap to see the filtered list of 43235 dogs!";
        fcmService.sendNotification(FCMTopic.PETFINDER, notificationTitle, notificationBody, petfinderBody, true, false);
    }

    public List<AnimalsItem> findFilteredDogs(final Integer limit) throws JsonProcessingException {
        var petfinderResponses = findAllDogsNear43235();
        List<AnimalsItem> result = new ArrayList<>();
        petfinderResponses.forEach(response -> result.addAll(response.getAnimals()));
        var list = result.stream().filter(animal -> {
            var filter = buildFilter(animal);
            if (filter == null) return false;
            return filter.checkFilter();
        }).toList();
        if (limit != null) {
            list = list.stream().limit(limit).toList();
        }
        return list;
    }

    private BreedFilter buildFilter(final AnimalsItem item) {
        var filter = switch (item.getBreeds().getPrimary().toLowerCase()) {
            case "shiba inu" -> ShibaInuFilter.builder();
            case "husky" -> HuskyFilter.builder();
            case "siberian husky" -> SiberianHuskyFilter.builder();
            case "german shepherd dog" -> GermanShepherdDogFilter.builder();
            case "white german shepherd" -> WhiteGermanShepherdFilter.builder();
            case "jindo" -> JindoFilter.builder();
            case "samoyed" -> SamoyedFilter.builder();
            default -> null;
        };
        if (filter == null) return null;
        return filter
                .gender(item.getGender())
                .color(item.getColors().getPrimary())
                .mixed(item.getBreeds().getMixed())
                .status(item.getStatus())
                .age(item.getAge())
                .build();
    }
}
