package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.EmailRequest;
import io.github.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import io.github.johnchoi96.webservice.models.petfinder.response.AnimalsItem;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        // categorize breeds
        Map<String, List<AnimalsItem>> breedsMap = new HashMap<>();
        for (final AnimalsItem item : response) {
            var breedName = item.getBreeds().getPrimary().toLowerCase();
            if (!breedsMap.containsKey(breedName)) {
                final List<AnimalsItem> array = new ArrayList<>();
                array.add(item);
                breedsMap.put(breedName, array);
            } else {
                breedsMap.get(breedName).add(item);
            }
        }
        StringBuilder sb = new StringBuilder("<html><body>");
        breedsMap.forEach((breedName, animalList) -> {
            sb.append(String.format("<h3>Breed: %s</h3>", breedName));
            sb.append("<ol>");
            animalList.forEach(animal -> {
                sb.append("<li>");
                sb.append(String.format("Name: %s<br/>", animal.getName()));
                sb.append(String.format("Breed: %s<br/>", animal.getBreeds().getPrimary()));
                sb.append(String.format("Gender: %s<br/>", animal.getGender()));
                sb.append(String.format("Distance: %.2f miles from Columbus OH<br/>", animal.getDistance()));
                sb.append(String.format("Address: %s, %s<br/>", animal.getContact().getAddress().getCity(), animal.getContact().getAddress().getState()));
                sb.append(String.format("Mixed: %s<br/>", animal.getBreeds().getMixed() ? "yes" : "no"));
                sb.append(String.format("Color: %s<br/>", animal.getColors().getPrimary()));
                sb.append(String.format("URL: <a href='%s'>Click Here</a><br/>", animal.getUrl()));
                sb.append(String.format("Published at: %s<br/>", animal.getPublishedAt()));
                sb.append(String.format("Last updated at: %s<br/>", animal.getStatusChangedAt()));
                sb.append("</li>");
            });
            sb.append("</ol>");
        });
        sb.append("</body></html>");
        return sb.toString();
    }

    public String buildBodyForMetalPrice(
            final LocalDate prevDate, final LocalDate todayDate,
            final MetalPriceResponse prev, final MetalPriceResponse today) {
        final String googleLink = "https://www.google.com/search?q=gold+price+right+now";
        final String message = """
                <html><body>
                <h3>Gold Price is lower today!</h3>
                <p>
                    Previous Gold Rate on %s: $%.2f
                    <br />
                    Today's Gold Rate on %s: $%.2f
                    <br />
                    Difference: $%.2f
                    <br />
                    For more info, <a href='%s'>Click Here</a>
                    <br />
                </p>
                </body></html>
                """;
        var priceDifference = prev.getRates().getUsd() - today.getRates().getUsd();
        return String.format(message,
                prevDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                prev.getRates().getUsd(),
                todayDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                today.getRates().getUsd(),
                priceDifference,
                googleLink);
    }
}
