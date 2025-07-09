package com.johnchoi96.webservice.properties.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "datasource")
public class DataSourceProperties {

    private String url;

    private String username;

    private String password;

    private String driverClassName;
}
