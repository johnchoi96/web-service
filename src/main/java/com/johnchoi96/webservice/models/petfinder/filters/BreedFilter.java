package com.johnchoi96.webservice.models.petfinder.filters;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public abstract class BreedFilter {

    private String gender;

    private boolean mixed;

    private String color;

    private String status;

    private String age;

    protected boolean checkColor(final String color) {
        return color != null && (color.toLowerCase().contains("white")
                || color.toLowerCase().contains("cream")
                || color.toLowerCase().contains("tan")
                || color.toLowerCase().contains("yellow")
                || color.toLowerCase().contains("blond")
                || color.toLowerCase().contains("gray")
                || color.toLowerCase().contains("silver"));
    }

    /**
     * By default, filter out by adoptable status and not senior
     *
     * @return true if adoptable and not senior
     */
    public boolean checkFilter() {
        return status.equalsIgnoreCase("adoptable")
                && !age.equalsIgnoreCase("senior");
    }
}
