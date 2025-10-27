package com.johnchoi96.webservice.models.app_distribution.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AppMetadataResponse {

    private List<AppMetadata> apps;
}
