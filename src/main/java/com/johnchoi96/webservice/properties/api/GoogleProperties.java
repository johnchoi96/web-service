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
@ConfigurationProperties(prefix = "gcp.drive")
@Slf4j
public class GoogleProperties {

    private String serviceAccount;

    private String resumeDocId = "10jSUdhbt0Qv9lN3pvWG_B-_nUGZEOtl-iDfzMHjUNW4";

    public Resource getServiceAccountAsResource() {
        log.info("Loading resource from path: {}", new File(serviceAccount).getAbsolutePath());

        return new FileSystemResource(serviceAccount);
    }
}
