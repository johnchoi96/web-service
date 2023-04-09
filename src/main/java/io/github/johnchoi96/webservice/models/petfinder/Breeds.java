package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class Breeds{
	private Object secondary;
	private boolean mixed;
	private String primary;
	private boolean unknown;
}