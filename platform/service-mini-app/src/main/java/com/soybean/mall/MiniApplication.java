package com.soybean.mall;

import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"web-base-application.properties","application.properties","api-application.properties"}, factory = CompositePropertySourceFactory.class)
@SpringBootApplication(scanBasePackages = {"com.soybean.mall", "com.wanmi.sbc"})
@ComponentScan(basePackages = {"com.soybean.mall", "com.wanmi.sbc"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wanmi.sbc", "com.soybean.mall"})
@Slf4j
public class MiniApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(MiniApplication.class, args);
    }

}
