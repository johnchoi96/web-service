package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.firebase.messaging.FirebaseMessagingException;
import io.github.johnchoi96.webservice.clients.PetfinderClient;
import io.github.johnchoi96.webservice.entities.petfinder.PetBreedEntity;
import io.github.johnchoi96.webservice.entities.petfinder.PetLogEntity;
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
import io.github.johnchoi96.webservice.repos.petfinder.PetBreedRepo;
import io.github.johnchoi96.webservice.repos.petfinder.PetLogRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PetfinderService {

    private final PetfinderClient petfinderClient;

    private final FCMService fcmService;

    private final PetBreedRepo petBreedRepo;

    private final PetLogRepo petLogRepo;

    public List<PetfinderResponse> findAllDogsNear43235() throws JsonProcessingException {
        final Set<String> breeds = petBreedRepo.getBreedList();
        return petfinderClient.findAllDogsNear43235(breeds);
    }

    public void findFilteredDogsAndNotify(final Integer limit) throws JsonProcessingException, FirebaseMessagingException {
        var filteredList = findFilteredDogs(limit, true);
        final StringBuilder petfinderBody = FCMBodyFactory.buildBodyForPetfinder(filteredList);
        final String notificationTitle = "Message from Web Service for Petfinder";
        final String notificationBody = "Tap to see the filtered list of 43235 dogs!";
        fcmService.sendNotification(FCMTopic.PETFINDER, notificationTitle, notificationBody, petfinderBody, true, false);
    }

    public List<AnimalsItem> findFilteredDogs(final Integer limit, final boolean ignoreKnownPets) throws JsonProcessingException {
        final List<PetfinderResponse> petfinderResponses = findAllDogsNear43235();
        List<AnimalsItem> result = new ArrayList<>();
        petfinderResponses.forEach(response -> result.addAll(response.getAnimals()));
        List<AnimalsItem> list = result.stream().filter(animal -> {
            var filter = buildFilter(animal);
            if (filter == null) return false;
            return filter.checkFilter();
        }).toList();
        list = filterKnownPets(list, ignoreKnownPets);
        if (limit != null) {
            list = list.stream().limit(limit).toList();
        }
        return list;
    }

    public void deleteInactivePetfinderLogs(final int months) {
        final LocalDateTime date = LocalDateTime.now().minusMonths(months);
        petLogRepo.deleteLogOlderThanMonths(date);
    }

    @Transactional
    private List<AnimalsItem> filterKnownPets(final List<AnimalsItem> list, final boolean ignoreKnownPets) {
        final List<PetLogEntity> entitiesToSave = new ArrayList<>();
        final List<AnimalsItem> result = new ArrayList<>();
        list.forEach(animal -> {
            // check if animal is in DB
            final Optional<PetLogEntity> petLogEntityOptional = petLogRepo.getPetLogWithPetfinderId(animal.getId());
            if (petLogEntityOptional.isEmpty()) {
                final PetBreedEntity breed = petBreedRepo.getBreedWithName(animal.getBreeds().getPrimary());
                entitiesToSave.add(AnimalsItem.buildPetLogEntity(animal, breed));
                result.add(animal);
            } else {
                final PetLogEntity entry = petLogEntityOptional.get();
                entry.setLastAccessed(Instant.now());
                entitiesToSave.add(entry);
                if (!ignoreKnownPets) {
                    result.add(animal);
                }
            }
        });
        petLogRepo.saveAll(entitiesToSave);
        return result;
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
