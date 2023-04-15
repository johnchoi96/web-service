package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.models.petfinder.AnimalsItem;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class EmailBodyFactory {

    public String buildBodyFromWebApp(final EmailRequest emailRequest) {
        return String.format("Subject: %s\n\nBody:\n%s\n\nContact Info: %s\n",
                emailRequest.getSubject(),
                emailRequest.getBody(),
                Optional.ofNullable(emailRequest.getContactInfo()).orElse("Not Provided"));
    }

    public String buildBodyForPetfinder(final List<AnimalsItem> response) {
        StringBuilder sb = new StringBuilder();
        response.forEach(animal -> {
            sb.append("Item: [\n");
            sb.append(String.format("\tName: %s\n", animal.getName()));
            sb.append(String.format("\tGender: %s\n", animal.getGender()));
            sb.append(String.format("\tDistance: %.2f miles from Columbus OH\n", animal.getDistance()));
            sb.append(String.format("\tAddress: %s, %s\n", animal.getContact().getAddress().getCity(), animal.getContact().getAddress().getState()));
            sb.append(String.format("\tMixed: %s\n", animal.getBreeds().getMixed() ? "yes" : "no"));
            sb.append(String.format("\tColor: %s\n", animal.getColors().getPrimary()));
            sb.append(String.format("\tURL: %s\n", animal.getUrl()));
            sb.append("]\n\n");
        });
        return sb.toString();
    }
}
