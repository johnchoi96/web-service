package com.johnchoi96.webservice.models.app_distribution.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AppMetadata {

    private String appName;

    private String version;

    private String otaDownloadLink;
}
