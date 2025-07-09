package com.johnchoi96.webservice.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.format.DateTimeParseException;

public class StringInstantDeserializer extends JsonDeserializer<Instant> {
    @Override
    public Instant deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            return Instant.parse(jsonParser.getValueAsString());
        } catch (IOException | DateTimeParseException e) {
            return null;
        }
    }
}
