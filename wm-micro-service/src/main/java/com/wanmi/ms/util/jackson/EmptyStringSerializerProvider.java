package com.wanmi.ms.util.jackson;

import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
import com.fasterxml.jackson.databind.ser.SerializerFactory;

/**
 * 自定义序列化实现, 针对String字段进行处理; 如果是String类型字段且值等于null, 这返回空串""; 如果是Object字段且值为null, 则按Jackson默认处理并返回
 * Created by aqlu on 16/6/29.
 */
public class EmptyStringSerializerProvider extends DefaultSerializerProvider {

    public EmptyStringSerializerProvider() {
        super();
    }

    public EmptyStringSerializerProvider(EmptyStringSerializerProvider provider, SerializationConfig config,
                                         SerializerFactory jsf) {
        super(provider, config, jsf);
    }

    @Override
    public EmptyStringSerializerProvider createInstance(SerializationConfig config, SerializerFactory jsf) {
        return new EmptyStringSerializerProvider(this, config, jsf);
    }

    @Override
    public JsonSerializer<Object> findNullValueSerializer(BeanProperty property) throws JsonMappingException {
        if (property.getType().getRawClass().equals(String.class)) {
            return EmptyStringSerializer.INSTANCE;
        } else {
            return super.findNullValueSerializer(property);
        }
    }
}