package io.github.johnchoi96.webservice.models.petfinder.filters;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class HuskyFilter extends BreedFilter {

    @Override
    public boolean checkFilter() {
        var result = super.checkFilter();
        result = result && checkColor(this.getColor());
        result = result && super.getGender().equalsIgnoreCase("female");
        return result;
    }
}
