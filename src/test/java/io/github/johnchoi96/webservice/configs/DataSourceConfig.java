package io.github.johnchoi96.webservice.configs;

import liquibase.integration.spring.SpringLiquibase;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@TestConfiguration
public class DataSourceConfig {

    @Bean
    public SpringLiquibase liquibase() {
        return new SpringLiquibase() {
            @Override
            public void afterPropertiesSet() {
                // no-op
            }
        };
    }

    @Bean
    @Primary
    public DataSource testDataSource() {
        return Mockito.mock(DataSource.class);
    }
}