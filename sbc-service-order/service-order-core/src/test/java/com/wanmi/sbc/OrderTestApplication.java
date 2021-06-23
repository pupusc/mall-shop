package com.wanmi.sbc;

import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@EnableAsync
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.wanmi.sbc"})
@SpringBootApplication
@EnableFeignClients(basePackages = {"com.wanmi.sbc"})
@PropertySource(value = {"api-application.properties"}, factory = CompositePropertySourceFactory.class)
public class OrderTestApplication {

    public static void main(String[] args) {
            SpringApplication.run(OrderTestApplication.class, args);
    }
}
