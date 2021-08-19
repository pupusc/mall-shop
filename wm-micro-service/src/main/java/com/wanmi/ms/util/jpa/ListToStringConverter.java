package com.wanmi.ms.util.jpa;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * List <--> String Converter
 * Created by angus on 15/6/16.
 */
@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {

    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "";
        }
        return join(attribute, ",");
    }

    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().length() == 0) {
            return new ArrayList<>();
        }

        String[] data = dbData.split(",");
        return Arrays.asList(data);
    }

    public static String join(final Iterable<?> target, final String separator) {

        final StringBuilder sb = new StringBuilder();
        final Iterator<?> it = target.iterator();
        if (it.hasNext()) {
            sb.append(it.next());
            while (it.hasNext()) {
                sb.append(separator);
                sb.append(it.next());
            }
        }
        return sb.toString();

    }
}