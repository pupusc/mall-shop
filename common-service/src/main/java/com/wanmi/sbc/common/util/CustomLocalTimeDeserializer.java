package com.wanmi.sbc.common.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom Jackson deserializer for transforming a JSON object (using the ISO 8601 time format)
 * to a Joda LocalTime object.
 */
public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    private static final String TIME_FORMATTER_PATTERN = "HH:mm:ss";

    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMATTER_PATTERN);

    @SuppressWarnings("deprecation")
    @Override
    public LocalTime deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonToken t = jp.getCurrentToken();
        if (t == JsonToken.VALUE_STRING) {
            String str = jp.getText().trim();
            return LocalTime.parse(str, TIME_FORMATTER);
        }
        throw ctxt.mappingException(handledType());
    }
}
