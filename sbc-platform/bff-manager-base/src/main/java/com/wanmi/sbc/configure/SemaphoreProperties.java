package com.wanmi.sbc.configure;

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
@ConfigurationProperties(prefix = "semaphore")
public class SemaphoreProperties {

    private int permits;

    private boolean fair;

}
