package io.github.johnchoi96.webservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.johnchoi96.webservice.clients.PetfinderClient;
import io.github.johnchoi96.webservice.models.petfinder.AnimalsItem;
import io.github.johnchoi96.webservice.models.petfinder.PetfinderResponse;
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
//            "Husky", "Siberian Husky", "German Shepherd Dog", // removed because they're common
            "White German Shepherd", "Jindo");

    public List<PetfinderResponse> findAllDogsNear43235() throws JsonProcessingException {
        return petfinderClient.findAllDogsNear43235(breeds);
    }

    public void findFilteredDogsAndReport() throws JsonProcessingException {
        var filteredList = findFilteredDogs();
        emailService.sendEmailForPetfinder(filteredList);
    }

    public List<AnimalsItem> findFilteredDogs() throws JsonProcessingException {
        var petfinderResponses = findAllDogsNear43235();
        List<AnimalsItem> result = new ArrayList<>();
        petfinderResponses.forEach(response -> result.addAll(response.getAnimals()));
        return result.stream().filter(animal -> breeds.contains(animal.getBreeds().getPrimary()) &&
                !animal.getBreeds().getMixed() &&
                animal.getStatus().equalsIgnoreCase("adoptable")).toList();
    }
}
