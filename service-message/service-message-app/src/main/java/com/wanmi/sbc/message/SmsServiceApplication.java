package com.wanmi.sbc.message;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.wanmi.sbc.message.configuration.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.*;

/**
 * <p>Ares应用安装启动类</p>
 * Created by of628-wenzhi on 2017-09-18-上午11:18.
 */
@SpringBootApplication(exclude = {DruidDataSourceAutoConfigure.class, SpringBootConfiguration.class})
@ComponentScan(basePackages = {"com.wanmi.sbc.message","com.wanmi.sbc.common.util","com.wanmi.sbc.common.handler.exc"})
@EnableAsync
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wanmi.sbc.crm","com.wanmi.ares.provider","com.wanmi.sbc.setting"})
@PropertySource(value = {"api-application.properties"}, factory = CompositePropertySourceFactory.class)
@EnableJpaAuditing
public class SmsServiceApplication {
    public static void main(String[] args) throws UnknownHostException {
        Environment env = SpringApplication.run(com.wanmi.sbc.message.SmsServiceApplication.class, args).getEnvironment();
        String port = env.getProperty("server.port", "8580");
        String healthPort = env.getProperty("management.server.port", "9501");

        log.info("Access URLs:\n----------------------------------------------------------\n\t"
                        + "Local: \t\thttp://127.0.0.1:{}\n\t"
                        + "External: \thttp://{}:{}\n\t"
                        + "health: \thttp://{}:{}/act/health\n----------------------------------------------------------",
                port,
                InetAddress.getLocalHost().getHostAddress(),
                port,
                InetAddress.getLocalHost().getHostAddress(),
                healthPort
        );
    }
}
