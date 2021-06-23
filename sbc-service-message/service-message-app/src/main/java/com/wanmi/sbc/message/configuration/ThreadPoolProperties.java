package com.wanmi.sbc.message.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * \* Created with IntelliJ IDEA.
 * \* User: zgl
 * \* Date: 2019-12-5
 * \* Time: 17:30
 * \* To change this template use File | Settings | File Templates.
 * \* Description:
 * \
 */
@Data
@ConfigurationProperties(prefix = "thread.pool")
public class ThreadPoolProperties {

    private String threadNamePrefix;

    private int corePoolSize;

    private int maxPoolSize;

    private int keepAliveSeconds;

    private int queueCapacity;
}
