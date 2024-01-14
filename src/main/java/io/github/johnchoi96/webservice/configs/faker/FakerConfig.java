package io.github.johnchoi96.webservice.configs.faker;

import com.github.javafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

@Configuration
public class FakerConfig {

    @Bean
    public Faker initializeFaker() {
        return new Faker(new Locale("en-US"));
    }
}
