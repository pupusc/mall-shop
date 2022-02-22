package com.soybean.mall;

import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;

@PropertySource(value = {"web-base-application.properties","application.properties"}, factory = CompositePropertySourceFactory.class)
@SpringBootApplication
@EnableFeignClients
@Slf4j
public class MiniApplication {

    public static void main(String[] args) {
        System.setProperty("nacos.logging.default.config.enabled","false");
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(MiniApplication.class, args);
    }

}
