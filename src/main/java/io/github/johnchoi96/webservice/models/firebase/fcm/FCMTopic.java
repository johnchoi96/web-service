package io.github.johnchoi96.webservice.models.firebase.fcm;

import lombok.Getter;

@Getter
public enum FCMTopic {

    ALL("jc-alerts-all"),
    PETFINDER("jc-alerts-petfinder"),
    METALPRICE("jc-alerts-metalprice"),
    CFB("jc-alerts-cfb"),
    TEST_NOTIFICATION("jc-alerts-test");

    private final String value;

    FCMTopic(final String value) {
        this.value = value;
    }
}
