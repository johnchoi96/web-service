package io.github.johnchoi96.webservice;

import io.github.johnchoi96.webservice.configs.ConcealThisVersionConfiguration;
import io.github.johnchoi96.webservice.configs.PetfinderApiConfiguration;
import io.github.johnchoi96.webservice.configs.SendGridApiConfiguration;
import io.github.johnchoi96.webservice.configs.VoaVersionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        SendGridApiConfiguration.class,
        ConcealThisVersionConfiguration.class,
        VoaVersionConfiguration.class,
        PetfinderApiConfiguration.class
})
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }

}
