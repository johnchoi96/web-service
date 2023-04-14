package io.github.johnchoi96.webservice.models.uptime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UptimeObject {
    private int hours;

    private int seconds;

    private int minutes;

    private int days;
}