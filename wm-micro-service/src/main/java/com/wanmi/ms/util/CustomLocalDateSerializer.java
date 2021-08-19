package com.wanmi.ms.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Custom Jackson serializer for transforming a Joda LocalDate object to JSON.
 */
public class CustomLocalDateSerializer extends JsonSerializer<LocalDate> {


    @Override
    public void serialize(LocalDate value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        if(value == null) {
            jgen.writeNull();
        }else {
            jgen.writeString(DateTimeFormatter.ISO_DATE.format(value));
        }
    }
}
