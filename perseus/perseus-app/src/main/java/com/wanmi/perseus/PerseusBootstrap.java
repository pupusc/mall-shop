package com.wanmi.perseus;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>Perseus应用安装启动类</p>
 * Created by of628-wenzhi on 2017-09-22-下午2:06.
 */
@SpringBootApplication
@EnableAsync
@Slf4j
public class PerseusBootstrap {
    public static void main(String[] args) throws UnknownHostException {
        Environment env = SpringApplication.run(PerseusBootstrap.class, args).getEnvironment();
        String port = env.getProperty("server.port", "8480");
        String actPort = env.getProperty("management.server.port", "8481");

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
