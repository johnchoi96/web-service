package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.cfb.UpsetGame;
import io.github.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import io.github.johnchoi96.webservice.models.petfinder.response.AnimalsItem;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class FCMBodyFactory {

    public StringBuilder buildBodyForPetfinder(final List<AnimalsItem> response) {
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
        return sb;
    }

    public StringBuilder buildBodyForMetalPrice(
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
        var priceDifference = today.getRates().getUsd() - prev.getRates().getUsd();
        var body = String.format(message,
                prevDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                prev.getRates().getUsd(),
                todayDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
                today.getRates().getUsd(),
                priceDifference,
                googleLink);
        return new StringBuilder(body);
    }

    public StringBuilder buildBodyForCfbUpset(final String seasonType, final Integer week, final List<UpsetGame> upsetGames) {
        final StringBuilder body = new StringBuilder("<html><body>");
        final String headerText = seasonType.equals("regular") ? String.format("CFB Week %d Upset Report", week) : "CFB Postseason Upset Report";
        body.append("<h1>");
        body.append(headerText);
        body.append("</h1>");
        body.append("<p>Upsets:</p>");
        body.append("<table border='1'><tr>");
        body.append("<th>Match Desc</th>");
        body.append("<th>Pre-Match Win Chance</th>");
        body.append("<th>Score</th>");
        body.append("</tr>");
        final String rowText = "<tr><td>%s</td><td>%s</td><td>%s</td>";
        for (final UpsetGame gameData : upsetGames) {
            final String matchDesc = getMatchDesc(gameData);
            final int homeWinChancePercentage = (int) (gameData.getPreGameHomeWinProbability() * 100);
            final int awayWinChancePercentage = 100 - homeWinChancePercentage;
            final String preMatchChanceText = String.format("%d%%, %d%%", awayWinChancePercentage, homeWinChancePercentage);
            final String scoreText = String.format("%d - %d", gameData.getAwayPoints(), gameData.getHomePoints());
            body.append(String.format(rowText, matchDesc, preMatchChanceText, scoreText));
        }
        body.append("</tr>");
        body.append("</table></body></html>");
        return body;
    }

    private static String getMatchDesc(final UpsetGame gameData) {
        String awayTeamDesc = gameData.getAwayTeamName();
        String homeTeamDesc = gameData.getHomeTeamName();
        if (gameData.getAwayRank() != null) {
            awayTeamDesc = String.format("#%d %s", gameData.getAwayRank(), gameData.getAwayTeamName());
        }
        if (gameData.getHomeRank() != null) {
            homeTeamDesc = String.format("#%d %s", gameData.getHomeRank(), gameData.getHomeTeamName());
        }
        return String.format("%s @ %s", awayTeamDesc, homeTeamDesc);
    }
}
