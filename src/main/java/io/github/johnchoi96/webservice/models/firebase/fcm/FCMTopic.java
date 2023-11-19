package io.github.johnchoi96.webservice.models.firebase.fcm;

import lombok.Getter;

@Getter
public enum FCMTopic {

    ALL("jc-alerts-all"),
    PETFINDER("jc-alerts-petfinder"),
    METALPRICE("jc-alerts-metalprice");

    private final String value;

    FCMTopic(final String value) {
        this.value = value;
    }
}
