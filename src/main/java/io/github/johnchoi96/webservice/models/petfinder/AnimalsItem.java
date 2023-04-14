package io.github.johnchoi96.webservice.models.petfinder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AnimalsItem {
    private String gender;

    private Double distance;

    @JsonProperty(value = "_links")
    private Links links;

    private String statusChangedAt;

    private String description;

    private Object organizationAnimalId;

    private List<Object> videos;

    private String type;

    private List<PhotosItem> photos;

    private Colors colors;

    private Breeds breeds;

    private Contact contact;

    private int id;

    private String publishedAt;

    private PrimaryPhotoCropped primaryPhotoCropped;

    private String url;

    private List<Object> tags;

    private String coat;

    private Environment environment;

    private String size;

    private String species;

    private String organizationId;

    private String name;

    private Attributes attributes;

    private String age;

    private String status;
}