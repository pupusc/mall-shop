package com.soybean.mall.wx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

//编译
@SpringBootApplication(scanBasePackages = {"com.soybean.mall", "com.wanmi.sbc"})
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.soybean.mall", "com.wanmi.sbc"})
public class WxServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxServiceApplication.class, args);
    }
}
