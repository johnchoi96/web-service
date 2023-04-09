package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class PhotosItem{
	private String small;
	private String large;
	private String medium;
	private String full;
}