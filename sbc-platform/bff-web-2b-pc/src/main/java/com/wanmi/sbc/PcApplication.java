package com.wanmi.sbc;

import com.wanmi.sbc.common.configure.CompositePropertySourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.statemachine.config.EnableWithStateMachine;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>B2B-PC Bootstrap</p>
 * Created by of628-wenzhi on 2017-07-04-下午4:00.
 */
@SpringBootApplication
@EnableWithStateMachine
@EnableAsync
@Slf4j
@ComponentScan(basePackages = {"com.wanmi.sbc", "com.wanmi.ms.autoconfigure"})
@PropertySource(value = {"web-base-application.properties","application.properties", "api-application.properties"}, factory = CompositePropertySourceFactory.class)
@EnableFeignClients
@EnableJpaAuditing
@EnableCaching
public class PcApplication {
    public static void main(String[] args) throws UnknownHostException {

        System.setProperty("es.set.netty.runtime.available.processors", "false");
        Environment env = SpringApplication.run(PcApplication.class, args).getEnvironment();
        String port = env.getProperty("server.port", "8089");
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
