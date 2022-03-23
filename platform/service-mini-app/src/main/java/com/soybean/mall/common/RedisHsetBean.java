package com.soybean.mall.common;

import lombok.Data;

@Data
public class RedisHsetBean {

    private String key;

    private String field;

    private String value;

}
