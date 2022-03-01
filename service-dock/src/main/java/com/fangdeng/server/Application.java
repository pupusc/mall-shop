package com.fangdeng.server;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.*;



@ComponentScan(basePackages = {"com.wanmi.sbc.common.handler.exc"})
@SpringBootApplication
@EnableAsync
@EnableDiscoveryClient
@EnableCaching
@EnableScheduling
@EnableSwagger2
@Configuration
public class Application {
    public static void main(String[] args) throws UnknownHostException {
          SpringApplication.run(Application.class,args);
    }
}