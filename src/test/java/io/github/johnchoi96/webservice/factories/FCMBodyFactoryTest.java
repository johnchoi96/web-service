package io.github.johnchoi96.webservice.factories;

import io.github.johnchoi96.webservice.models.cfb.upset_game.UpsetGameResponse;
import io.github.johnchoi96.webservice.models.metalprice.MetalPriceResponse;
import io.github.johnchoi96.webservice.models.metalprice.Rates;
import io.github.johnchoi96.webservice.models.petfinder.response.Address;
import io.github.johnchoi96.webservice.models.petfinder.response.AnimalsItem;
import io.github.johnchoi96.webservice.models.petfinder.response.Breeds;
import io.github.johnchoi96.webservice.models.petfinder.response.Colors;
import io.github.johnchoi96.webservice.models.petfinder.response.Contact;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FCMBodyFactoryTest {

    @Test
    void testBuildBodyForPetfinder() {
        final List<AnimalsItem> inputItems = getDummyAnimalsItems();
        final String expected = "<html><body><h3>Breed: dummy-primary-value-0" +
                "</h3><ol><li>Name: test-name-0<br/>Breed: dummy-primary-" +
                "value-0<br/>Gender: test-gender-0<br/>Distance: 1.23 miles " +
                "from Columbus OH<br/>Address: test-city-0, test-state-0<br/>" +
                "Mixed: yes<br/>Color: test-primary-0<br/>URL: <a href=" +
                "'https://www.example.com'>Click Here</a><br/>Published at: " +
                "test-published-at-0<br/>Last updated at: test-status-changed-at-0" +
                "<br/></li></ol><h3>Breed: dummy-primary-value-1</h3><ol><li>" +
                "Name: test-name-1<br/>Breed: dummy-primary-value-1<br/>Gender: " +
                "test-gender-1<br/>Distance: 2.34 miles from Columbus OH<br/>" +
                "Address: test-city-1, test-state-1<br/>Mixed: no<br/>Color: " +
                "test-primary-1<br/>URL: <a href='https://www.example.com'>" +
                "Click Here</a><br/>Published at: test-published-at-1<br/>" +
                "Last updated at: test-status-changed-at-1<br/></li></ol><h3>" +
                "Breed: dummy-primary-value-2</h3><ol><li>Name: test-name-2<br/>" +
                "Breed: dummy-primary-value-2<br/>Gender: test-gender-2<br/>" +
                "Distance: 4.56 miles from Columbus OH<br/>Address: test-city-2, " +
                "test-state-2<br/>Mixed: yes<br/>Color: test-primary-2<br/>URL: " +
                "<a href='https://www.example.com'>Click Here</a><br/>Published " +
                "at: test-published-at-2<br/>Last updated at: test-status-changed" +
                "-at-2<br/></li></ol></body></html>";
        final String actual = FCMBodyFactory.buildBodyForPetfinder(inputItems).toString();
        assertEquals(expected, actual);
    }

    @Test
    void testBuildBodyForCfbUpset() {
        final String inputSeasonType = "";
        final Integer inputWeek = 1;
        final UpsetGameResponse inputResponse = getDummyUpsetGameResponse();
        final String expected = """
                """;
        final String actual = FCMBodyFactory.buildBodyForCfbUpset(inputSeasonType, inputWeek, inputResponse).toString();
        assertEquals(expected, actual);
    }

    @Test
    void buildForMetalPriceIncreased() {
        final LocalDate prevDate = LocalDate.of(2023, 9, 14);
        final LocalDate todayDate = LocalDate.of(2023, 9, 15);
        final MetalPriceResponse prevRate = getDummyPrevRate();
        final MetalPriceResponse todayRate = getDummyTodayRate();
        final String expectedBody = """
                <html><body>
                <h3>Gold Price is lower today!</h3>
                <p>
                    Previous Gold Rate on 09/14/2023: $2.34
                    <br />
                    Today's Gold Rate on 09/15/2023: $3.45
                    <br />
                    Difference: $1.11
                    <br />
                    For more info, <a href='https://www.google.com/search?q=gold+price+right+now'>Click Here</a>
                    <br />
                </p>
                </body></html>
                """;
        final StringBuilder actual = FCMBodyFactory.buildBodyForMetalPrice(prevDate, todayDate, prevRate, todayRate);
        assertEquals(expectedBody, actual.toString());
    }

    @Test
    void buildForMetalPriceDecreased() {
        final LocalDate prevDate = LocalDate.of(2023, 9, 14);
        final LocalDate todayDate = LocalDate.of(2023, 9, 15);
        final MetalPriceResponse prevRate = getDummyTodayRate();
        final MetalPriceResponse todayRate = getDummyPrevRate();
        final String expectedBody = """
                <html><body>
                <h3>Gold Price is lower today!</h3>
                <p>
                    Previous Gold Rate on 09/14/2023: $3.45
                    <br />
                    Today's Gold Rate on 09/15/2023: $2.34
                    <br />
                    Difference: $-1.11
                    <br />
                    For more info, <a href='https://www.google.com/search?q=gold+price+right+now'>Click Here</a>
                    <br />
                </p>
                </body></html>
                """;
        final StringBuilder actual = FCMBodyFactory.buildBodyForMetalPrice(prevDate, todayDate, prevRate, todayRate);
        assertEquals(expectedBody, actual.toString());
    }

    private MetalPriceResponse getDummyPrevRate() {
        final MetalPriceResponse response = new MetalPriceResponse();
        final Rates rate = new Rates();
        rate.setUsd(2.34);
        response.setRates(rate);
        return response;
    }

    private MetalPriceResponse getDummyTodayRate() {
        final MetalPriceResponse response = new MetalPriceResponse();
        final Rates rate = new Rates();
        rate.setUsd(3.45);
        response.setRates(rate);
        return response;
    }

    private List<AnimalsItem> getDummyAnimalsItems() {
        final AnimalsItem item0 = new AnimalsItem() {
            {
                setBreeds(new Breeds() {
                    {
                        setPrimary("dummy-primary-value-0");
                        setMixed(true);
                    }
                });
                setName("test-name-0");
                setGender("test-gender-0");
                setDistance(1.23);
                setContact(new Contact() {
                    {
                        setAddress(new Address() {
                            {
                                setCity("test-city-0");
                                setState("test-state-0");
                            }
                        });
                    }
                });
                setColors(new Colors() {
                    {
                        setPrimary("test-primary-0");
                    }
                });
                setUrl("https://www.example.com");
                setPublishedAt("test-published-at-0");
                setStatusChangedAt("test-status-changed-at-0");
            }
        };
        final AnimalsItem item1 = new AnimalsItem() {
            {
                setBreeds(new Breeds() {
                    {
                        setPrimary("dummy-primary-value-1");
                        setMixed(false);
                    }
                });
                setName("test-name-1");
                setGender("test-gender-1");
                setDistance(2.34);
                setContact(new Contact() {
                    {
                        setAddress(new Address() {
                            {
                                setCity("test-city-1");
                                setState("test-state-1");
                            }
                        });
                    }
                });
                setColors(new Colors() {
                    {
                        setPrimary("test-primary-1");
                    }
                });
                setUrl("https://www.example.com");
                setPublishedAt("test-published-at-1");
                setStatusChangedAt("test-status-changed-at-1");
            }
        };
        final AnimalsItem item2 = new AnimalsItem() {
            {
                setBreeds(new Breeds() {
                    {
                        setPrimary("dummy-primary-value-2");
                        setMixed(true);
                    }
                });
                setName("test-name-2");
                setGender("test-gender-2");
                setDistance(4.56);
                setContact(new Contact() {
                    {
                        setAddress(new Address() {
                            {
                                setCity("test-city-2");
                                setState("test-state-2");
                            }
                        });
                    }
                });
                setColors(new Colors() {
                    {
                        setPrimary("test-primary-2");
                    }
                });
                setUrl("https://www.example.com");
                setPublishedAt("test-published-at-2");
                setStatusChangedAt("test-status-changed-at-2");
            }
        };

        return List.of(item0, item1, item2);
    }

    private UpsetGameResponse getDummyUpsetGameResponse() {
        return UpsetGameResponse.builder().build();
    }
}
