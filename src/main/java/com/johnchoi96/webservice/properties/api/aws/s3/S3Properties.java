package com.johnchoi96.webservice.properties.api.aws.s3;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "aws.s3")
@Slf4j
public class S3Properties {

    private ResumeProperty resume;

    private AppleIPAProperty appleIpa;

    private CredentialsProperty credentials;
}
