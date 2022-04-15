package com.soybean.mall.common.logger;

import org.springframework.stereotype.Component;

@Component
public class IConfigService {

    private NacosConfigService nacosConfigService;

    public static void addChangeListener(Object o) {

    }

    public static String getProperty(String key, String defaultValue) {
        return null;
    }

    public static boolean getBooleanProperty(String keyClass, Boolean defaultValue) {
        return false;
    }
}
