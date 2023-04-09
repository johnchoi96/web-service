package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class Contact{
	private Address address;
	private String phone;
	private String email;
}