package com.soybean.mall.common.logger;

import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class NacosConfigService {
    private static Logger logger = LoggerFactory.getLogger(NacosConfigService.class);
    private static final long queryTimeOut = 1000 * 2;

    @Autowired
    private ConfigService configService;
    @Autowired
    private NacosConfigProperties nacosConfigProperties;

    public String getProperty(String key, String defaultValue) {
        if (Objects.isNull(configService)) {
            logger.error("配置中心服务没有实例化");
            return defaultValue;
        }
        if (Objects.isNull(nacosConfigProperties)) {
            logger.error("配置中心属性没有初始化");
            return defaultValue;
        }
        if (!"UP".equals(configService.getServerStatus())) {
            logger.error("配置中心服务端已下线");
            return defaultValue;
        }

        String result = defaultValue;
        try {
            result = configService.getConfig(key, nacosConfigProperties.getGroup(), queryTimeOut);
        } catch (NacosException e) {
            logger.error("查询配置数据发生异常", e);
        }
        return result;
    }
}
