package com.wanmi.ms.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * Jwt 配置
 * Created by aqlu on 15/11/20.
 */
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtProperties {

    /**
     * 过滤器路径
     */
    private String[] urlPatterns = {};

    /**
     * 排除的url列表；被排除的url将不参与jwt验证；
     */
    private String[] excludedUrls = {};

    /**
     * 排除的restful url Map；被排除的url将不参与jwt验证；
     *   因为存在只开放GET的查询接口,不开放POST,PUT等操作接口的场景
     */
    private String excludedRestUrls;

    /**
     * 私钥信息
     */
    private String secretKey;

    /**
     * 过滤器加载顺序，数字越小优先级越高，可以是负数；此字段目前不生效
     */
    private int order = Ordered.LOWEST_PRECEDENCE;

    /**
     * 获取jwt信息的key
     */
    private String jwtHeaderKey = "Authorization";

    /**
     * jwt信息前缀
     */
    private String jwtHeaderPrefix = "Bearer ";
}
