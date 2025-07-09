package com.johnchoi96.webservice.properties.api;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;

@Getter
@Setter
@ConfigurationProperties(prefix = "gcp.firebase")
@Slf4j
public class FirebaseProperties {
    private String serviceAccount;

    public Resource getServiceAccountAsResource() {
        log.info("Loading resource from path: {}", new File(serviceAccount).getAbsolutePath());

        return new FileSystemResource(serviceAccount);
    }
}