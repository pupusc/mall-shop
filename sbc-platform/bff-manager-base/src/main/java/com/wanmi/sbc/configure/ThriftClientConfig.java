package com.wanmi.sbc.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by zhangjin on 2017/9/25.
 */
@ConfigurationProperties(prefix = "thrift")
@Data
public class ThriftClientConfig {

    /**
     * 服务器端口号
     */
    private Integer port;

    /**
     * 服务端的ip
     */
    private String ip;

    /**
     * 最大线程数
     */
    private Integer maxTotal;

    /**
     * 最小空闲数
     */
    private Integer minIdle;

    /**
     * 授权地址
     */
    private String authorizationSocket;

}
