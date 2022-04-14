package com.soybean.mall.common.logger;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NacosConfigService {
    @NacosInjected
    private ConfigService configService;

//    @Autowired
//    private NacosConfigProperties nacosConfigProperties;

    public String getProperty(String key, String defaultValue) {
//        configService.addListener();
        return null;
    }
}
