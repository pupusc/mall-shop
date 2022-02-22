package com.soybean.mall.wx;

import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = {"api-application.properties"}, factory = CompositePropertySourceFactory.class)
public class WxServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxServiceApplication.class, args);
    }
}
