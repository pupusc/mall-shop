package com.wanmi.ms.util;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Custom Jackson serializer for transforming a Date object to JSON.
 */
public class CustomDateSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date value, JsonGenerator generator,
                          SerializerProvider serializerProvider)
            throws IOException {
        if(value == null) {
            generator.writeNull();
        }else{
            generator.writeString(new SimpleDateFormat("yyyy-MM-dd").format(value));
        }
    }
}
