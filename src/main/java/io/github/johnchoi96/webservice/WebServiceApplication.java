package io.github.johnchoi96.webservice;

import io.github.johnchoi96.webservice.configs.ConcealThisVersionConfiguration;
import io.github.johnchoi96.webservice.configs.SendGridApiConfiguration;
import io.github.johnchoi96.webservice.configs.VoaVersionConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableConfigurationProperties({SendGridApiConfiguration.class, ConcealThisVersionConfiguration.class, VoaVersionConfiguration.class})
public class WebServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebServiceApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:3000", "https://johnchoi96.github.io/");
            }
        };
    }
}
