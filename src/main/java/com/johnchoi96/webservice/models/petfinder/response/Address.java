package com.johnchoi96.webservice.models.petfinder.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Address {
    private String country;

    private String address2;

    private String city;

    private String address1;

    private String postcode;

    private String state;
}