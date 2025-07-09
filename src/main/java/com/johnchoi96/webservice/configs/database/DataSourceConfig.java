package com.johnchoi96.webservice.configs.database;

import com.johnchoi96.webservice.properties.datasource.DataSourceProperties;
import lombok.RequiredArgsConstructor;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@RequiredArgsConstructor
public class DataSourceConfig {

    private final DataSourceProperties dataSourceProperties;

    @Value("${spring.datasource.schema}")
    public String schema;

    @Bean
    public DataSource dataSource() {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSourceProperties.getUsername());
        dataSource.setPassword(dataSourceProperties.getPassword());
        dataSource.setDriverClassName(dataSource.getDriverClassName());
        dataSource.setDefaultSchema(schema);
        return dataSource;
    }
}