package io.github.johnchoi96.webservice.models.firebase.fcm;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendNotificationPayload {

    @NotNull(message = "Title must not be blank")
    private String title;

    @NotNull(message = "Subtitle must not be blank")
    private String subtitle;

    @NotNull(message = "Message must not be blank")
    private String message;

    @JsonProperty(value = "html")
    private boolean isHtml = false;
}
