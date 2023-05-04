package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.models.petfinder.response.AnimalsItem;
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
        StringBuilder sb = new StringBuilder("<html><body><ol>");
        response.forEach(animal -> {
            sb.append("<li>");
            sb.append(String.format("Name: %s<br/>", animal.getName()));
            sb.append(String.format("Breed: %s<br/>", animal.getBreeds().getPrimary()));
            sb.append(String.format("Gender: %s<br/>", animal.getGender()));
            sb.append(String.format("Distance: %.2f miles from Columbus OH<br/>", animal.getDistance()));
            sb.append(String.format("Address: %s, %s<br/>", animal.getContact().getAddress().getCity(), animal.getContact().getAddress().getState()));
            sb.append(String.format("Mixed: %s<br/>", animal.getBreeds().getMixed() ? "yes" : "no"));
            sb.append(String.format("Color: %s<br/>", animal.getColors().getPrimary()));
            sb.append(String.format("URL: <a href='%s'>Click Here</a><br/>", animal.getUrl()));
            sb.append("</li>");
        });
        sb.append("</ol></body></html>");
        return sb.toString();
    }
}
