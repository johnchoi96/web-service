package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.clients.PetfinderClient;
import io.github.johnchoi96.webservice.models.petfinder.filters.*;
import io.github.johnchoi96.webservice.models.petfinder.response.AnimalsItem;
import io.github.johnchoi96.webservice.models.petfinder.response.PetfinderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class PetfinderService {

    @Autowired
    private PetfinderClient petfinderClient;

    @Autowired
    private EmailService emailService;

    private final Set<String> breeds = Set.of("Shiba Inu",
            "Husky", "Siberian Husky", "German Shepherd Dog",
            "White German Shepherd", "Jindo");

    public List<PetfinderResponse> findAllDogsNear43235() throws JsonProcessingException {
        return petfinderClient.findAllDogsNear43235(breeds);
    }

    public void findFilteredDogsAndReport(final Integer limit) throws JsonProcessingException {
        var filteredList = findFilteredDogs(limit);
        emailService.sendEmailForPetfinder(filteredList);
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
