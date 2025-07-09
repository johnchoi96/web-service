package com.johnchoi96.webservice.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StringInstantDeserializerTest {

    @Test
    void deserialize_ValidInstantString_ReturnsInstant() throws IOException {
        // Arrange
        StringInstantDeserializer deserializer = new StringInstantDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        String validInstantString = "2023-12-27T12:34:56Z";
        when(jsonParser.getValueAsString()).thenReturn(validInstantString);

        // Act
        Instant result = deserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertEquals(Instant.parse(validInstantString), result);
    }

    @Test
    void deserialize_InvalidInstantString_ReturnsNull() throws IOException {
        // Arrange
        StringInstantDeserializer deserializer = new StringInstantDeserializer();
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        String invalidInstantString = "invalid_instant_format";
        when(jsonParser.getValueAsString()).thenReturn(invalidInstantString);

        // Act
        Instant result = deserializer.deserialize(jsonParser, deserializationContext);

        // Assert
        assertNull(result);
    }
}