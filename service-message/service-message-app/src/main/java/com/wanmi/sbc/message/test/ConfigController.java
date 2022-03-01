package com.wanmi.sbc.message.test;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
@RefreshScope
@Slf4j
public class ConfigController {
    @Value("${my.message1:NULL}")
    private String message1;

    @Value("${my.message2:NULL}")
    private String message2;

    @RequestMapping("/get")
    public String get() {
        log.info("===========>>这是一次测试操作<<===========");
        String res = "message1 = " + message1 + "\n" + "message2 = " + message2;
        return res;
    }
}