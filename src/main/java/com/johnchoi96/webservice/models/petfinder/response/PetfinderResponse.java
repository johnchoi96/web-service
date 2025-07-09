package com.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PetfinderResponse {
    private Pagination pagination;

    private List<AnimalsItem> animals;
}