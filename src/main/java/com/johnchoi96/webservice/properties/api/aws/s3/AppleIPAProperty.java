package com.johnchoi96.webservice.properties.api.aws.s3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppleIPAProperty {

    private String bucketName;

    private String region;

    private String manifestFile;

    private String ipaFile;

    private String cdnUrl;
}
