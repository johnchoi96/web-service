package com.johnchoi96.webservice.models.petfinder.filters;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ShibaInuFilter extends BreedFilter {

    @Override
    public boolean checkFilter() {
        return super.checkFilter()
                && super.getGender().equalsIgnoreCase("female")
                && !super.isMixed();
    }
}
