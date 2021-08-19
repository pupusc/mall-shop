package com.wanmi.ms.util.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 针对String字段进行处理; 如果是String类型字段且值等于null, 这返回空串""; 如果是Object字段且值为null, 则按Jackson默认处理并返回
 * Created by aqlu on 16/6/29.
 */
public class EmptyStringObjectMapper extends ObjectMapper {
    public EmptyStringObjectMapper(){
        EmptyStringSerializerProvider serializerProvider = new EmptyStringSerializerProvider();
        this.setSerializerProvider(serializerProvider);
    }
}
