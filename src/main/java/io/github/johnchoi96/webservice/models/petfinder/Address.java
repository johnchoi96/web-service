package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class Address{
	private String country;
	private Object address2;
	private String city;
	private Object address1;
	private String postcode;
	private String state;
}