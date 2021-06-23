package com.wanmi.sbc.common.util;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom Jackson serializer for transforming a Joda LocalTime object to JSON.
 */
public class CustomLocalTimeSerializer extends JsonSerializer<LocalTime> {
    private static final String TIME_FORMATTER_PATTERN = "HH:mm:ss";

    private static DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern(TIME_FORMATTER_PATTERN);

    @Override
    public void serialize(LocalTime value, JsonGenerator jGen, SerializerProvider provider) throws IOException {
        if (value == null) {
            jGen.writeNull();
        } else {
            jGen.writeString(TIME_FORMATTER.format(value));
        }
    }
}
