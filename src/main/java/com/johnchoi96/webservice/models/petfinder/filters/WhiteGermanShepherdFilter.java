package com.johnchoi96.webservice.models.petfinder.filters;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class WhiteGermanShepherdFilter extends BreedFilter {

    @Override
    public boolean checkFilter() {
        var result = super.checkFilter();
        result = result && checkColor(this.getColor());
        return result;
    }
}
