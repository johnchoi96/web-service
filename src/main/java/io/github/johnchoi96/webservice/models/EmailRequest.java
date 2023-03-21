package io.github.johnchoi96.webservice.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailRequest {

    @NonNull
    private String subject;

    @NonNull
    private String body;

    private String contactInfo;
}
