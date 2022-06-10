package com.soybean.mall;

import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.ConfigurableEnvironment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@PropertySource(value = {"application.properties","api-application.properties"}, factory = CompositePropertySourceFactory.class)
@SpringBootApplication(scanBasePackages = {"com.soybean.mall", "com.wanmi.sbc"})
@ComponentScan(basePackages = {"com.soybean.mall", "com.wanmi.sbc"})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.wanmi.sbc", "com.soybean"})
@Slf4j
public class MiniApplication {

    public static void main(String[] args) throws UnknownHostException {
        System.setProperty("nacos.logging.default.config.enabled","false");
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        ConfigurableEnvironment env = SpringApplication.run(MiniApplication.class, args).getEnvironment();
        String port = env.getProperty("server.port", "8088");
        String healthPort = env.getProperty("management.server.port", "9001");

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
