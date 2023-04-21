package io.github.johnchoi96.webservice;

import io.github.johnchoi96.webservice.properties.api.PetfinderApiProperties;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import io.github.johnchoi96.webservice.properties.metadata.ConcealThisVersionProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaVersionProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
        SendGridApiProperties.class,
        ConcealThisVersionProperties.class,
        VoaVersionProperties.class,
        PetfinderApiProperties.class,
        WebAppMetadataProperties.class
})
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }

}
