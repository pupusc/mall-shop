package com.wanmi.sbc.common.config.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * redis 配置属性
 * Created by jinwei on 22/3/2017.
 */
@ConfigurationProperties(prefix = "redisson")
@Data
public class RedisProperties {

    private String url;

    private Integer database = 1;

    private String password;

}
