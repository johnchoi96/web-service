package io.github.johnchoi96.webservice.properties.api.aws.s3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeProperty {

    private String bucketName;

    private String region;

    private String objectName;
}
