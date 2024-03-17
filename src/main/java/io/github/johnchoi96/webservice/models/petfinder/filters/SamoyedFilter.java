package io.github.johnchoi96.webservice.models.petfinder.filters;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SamoyedFilter extends BreedFilter {

    @Override
    public boolean checkFilter() {
        var result = super.checkFilter();
        result = result && !super.isMixed();
        return result;
    }
}
