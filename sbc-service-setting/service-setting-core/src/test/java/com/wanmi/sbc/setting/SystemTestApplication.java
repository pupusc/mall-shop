package com.wanmi.sbc.setting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 系统模块测试
 *
 * @author liangck
 * @version 1.0
 * @since 16/5/13 09:42
 */

@SpringBootApplication(scanBasePackages = {"com.wanmi.sbc"})
public class SystemTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemTestApplication.class, args);
    }
}
