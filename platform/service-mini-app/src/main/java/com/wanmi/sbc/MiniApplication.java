package com.wanmi.sbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class MiniApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.wanmi.sbc.MiniApplication.class, args);
    }

}
