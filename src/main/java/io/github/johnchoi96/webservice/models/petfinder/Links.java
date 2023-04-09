package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class Links{
	private Organization organization;
	private Self self;
	private Type type;
}