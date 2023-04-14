package io.github.johnchoi96.webservice.models.uptime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UptimeResponse {
    private String startTime;

    private String timeZone;

    private UptimeObject uptime;
}