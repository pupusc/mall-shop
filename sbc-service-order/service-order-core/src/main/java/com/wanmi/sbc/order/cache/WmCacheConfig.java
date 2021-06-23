package com.wanmi.sbc.order.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WmCacheConfig {

    // 在 CacheConfig 中定义
    @Bean
    KeyGenerator keyGenerator(){
        return new EhCacheKeyGenerator();
    }

    // 该值是 keyGenerator 方法的方法名称，如果Bean 指定了名称，则使用指定的名称
    public static final String DEFAULT_KEY_GENERATOR = "keyGenerator";

    // 定义缓存区，缓存区可以在配置时指定不同的过期时间，作为防止缓存雪崩的一个保护措施
    public static final String ORDER = "ORDER";
}
