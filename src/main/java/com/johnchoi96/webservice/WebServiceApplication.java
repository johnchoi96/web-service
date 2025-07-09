package com.johnchoi96.webservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@ConfigurationPropertiesScan
@EnableCaching
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }
}
