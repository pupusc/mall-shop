package com.wanmi.sbc.common.config.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

;

/**
 * <p>redis client 自动化配置</p>
 * Created by of628-wenzhi on 2017-07-05-上午11:31.
 */

@Configuration
@ConditionalOnProperty(prefix="redisson",name = "injection", havingValue = "true")
@EnableConfigurationProperties(RedisProperties.class)
public class RedisAutoConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress(redisProperties.getUrl());
        config.useSingleServer().setDatabase(redisProperties.getDatabase());
        if (!StringUtils.isEmpty(redisProperties.getPassword())) {
            config.useSingleServer().setPassword(redisProperties.getPassword());
        }
        config.useSingleServer().setConnectionMinimumIdleSize(0);
        return Redisson.create(config);
    }

}
