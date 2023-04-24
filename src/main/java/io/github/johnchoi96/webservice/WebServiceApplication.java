package io.github.johnchoi96.webservice;

import io.github.johnchoi96.webservice.properties.api.PetfinderApiProperties;
import io.github.johnchoi96.webservice.properties.api.SendGridApiProperties;
import io.github.johnchoi96.webservice.properties.metadata.ConcealThisVersionMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.VoaVersionMetadataProperties;
import io.github.johnchoi96.webservice.properties.metadata.WebAppMetadataMetadataProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({
        SendGridApiProperties.class,
        ConcealThisVersionMetadataProperties.class,
        VoaVersionMetadataProperties.class,
        PetfinderApiProperties.class,
        WebAppMetadataMetadataProperties.class
})
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }
}
