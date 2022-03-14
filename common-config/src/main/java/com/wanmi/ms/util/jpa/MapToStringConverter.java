package com.wanmi.ms.util.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.io.IOException;
import java.util.*;


/**
 * Map <--> String Converter
 * Created by angus on 15/12/6.
 */
@Converter
public class MapToStringConverter implements AttributeConverter<Map<String, Object>, String> {
    private static Logger logger = LoggerFactory.getLogger(MapToStringConverter.class);

    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        try {
            return new ObjectMapper().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            logger.warn("convert to string failed. param:", attribute);
            return null;
        }
    }

    public Map<String, Object> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().length() == 0) {
            return new HashMap<>();
        }

        try {
            return new ObjectMapper().readValue(dbData, new TypeReference<Map<String, Object>>(){});
        } catch (IOException e) {
            logger.warn("convert to map failed. param:", dbData);
            return null;
        }
    }
}