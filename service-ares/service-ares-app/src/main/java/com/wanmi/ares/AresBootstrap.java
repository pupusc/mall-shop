package com.wanmi.ares;

//import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.wanmi.ares.configuration.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Ares应用安装启动类</p>
 * Created by of628-wenzhi on 2017-09-18-上午11:18.
 */
@SpringBootApplication
@EnableAsync
@MapperScan(basePackages = {"com.wanmi.ares.report.*.dao", "com.wanmi.ares.export.dao"})
@ComponentScan(basePackages = {"com.wanmi.ares", "com.wanmi.ares.provider"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = com.wanmi.sbc.common.handler.aop.TomcatDataSourceAspect.class)
})
@Slf4j
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wanmi.ares.provider", "com.wanmi.sbc.setting.api"})
@PropertySource(value = {"api-application.properties"}, factory = CompositePropertySourceFactory.class)
public class AresBootstrap {
    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("nacos.logging.default.config.enabled","false");
        Environment env = SpringApplication.run(AresBootstrap.class, args).getEnvironment();
        String actPort = env.getProperty("management.server.port", "8581");
        String port = env.getProperty("server.port", "8580");

        log.info("Access URLs:\n----------------------------------------------------------\n\t"
                        + "Local: \t\thttp://127.0.0.1:{}\n\t"
                        + "External: \thttp://{}:{}\n\t"
                        + "health: \thttp://{}:{}/act/health\n----------------------------------------------------------",
                port,
                InetAddress.getLocalHost().getHostAddress(),
                port,
                InetAddress.getLocalHost().getHostAddress(),
                actPort
        );
    }
}
