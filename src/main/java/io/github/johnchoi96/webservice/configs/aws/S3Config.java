package io.github.johnchoi96.webservice.configs.aws;

import io.github.johnchoi96.webservice.properties.api.aws.s3.S3Properties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3Properties s3Properties;

    @Bean
    public S3Client configure() {
        return S3Client.builder()
                .region(Region.of(s3Properties.getResume().getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(
                                s3Properties.getCredentials().getAccessKey(),
                                s3Properties.getCredentials().getSecretKey()
                        )
                ))
                .build();
    }
}
