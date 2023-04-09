package io.github.johnchoi96.webservice.models.petfinder;

import lombok.Data;

@Data
public class Attributes{
	private boolean shotsCurrent;
	private boolean specialNeeds;
	private Object declawed;
	private boolean spayedNeutered;
	private boolean houseTrained;
}