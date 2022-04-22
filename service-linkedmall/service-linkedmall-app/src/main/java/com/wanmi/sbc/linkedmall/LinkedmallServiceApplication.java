package com.wanmi.sbc.linkedmall;

import com.wanmi.sbc.linkedmall.configuration.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Ares应用安装启动类</p>
 * Created by of628-wenzhi on 2017-09-18-上午11:18.
 */
@SpringBootApplication(scanBasePackages = {"com.wanmi.sbc", "com.soybean.mall"})
@EnableAsync
@Slf4j
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wanmi.sbc"})
@PropertySource(value = {"api-application.properties"}, factory = CompositePropertySourceFactory.class)
public class LinkedmallServiceApplication {
    public static void main(String[] args) throws UnknownHostException {
        Environment env = SpringApplication.run(LinkedmallServiceApplication.class, args).getEnvironment();
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
