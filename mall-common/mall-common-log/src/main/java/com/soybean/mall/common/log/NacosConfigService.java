package com.soybean.mall.common.log;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.PropertyChangeType;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public final class NacosConfigService {
    private static Logger logger = LoggerFactory.getLogger(NacosConfigService.class);

    private static final long TIMEOUT_MS = 1000 * 2;
    private static final String PUBLIC_DATA_ID = "log.filter.properties";
    private static final String PUBLIC_DATA_GROUP = "PUBLIC";

    private static NacosConfigService instance;
    private String dataId;
    private String dataGroup;
    private ConfigService configService;
    private Map<String, String> appData = new ConcurrentHashMap<>();
    private Map<String, String> commonData = new ConcurrentHashMap<>();
    private Map<String, List<Runnable>> dataListener = new ConcurrentHashMap<>();

    private NacosConfigService() {
    }

    public static NacosConfigService getInstance() {
        if (instance == null) {
            throw new RuntimeException("配置服务尚未初始化");
        }
        return instance;
    }

    public static void initConfig(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            throw new RuntimeException("Spring容器注入失败");
        }
        instance = new NacosConfigService();
        //instance.dataId = applicationContext.getEnvironment().getProperty("spring.application.name");
        //instance.dataId += "."+ applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.file-extension");
        //instance.dataGroup = applicationContext.getEnvironment().getProperty("spring.cloud.nacos.config.group");
        instance.dataId = PUBLIC_DATA_ID;
        instance.dataGroup = PUBLIC_DATA_GROUP;
        instance.configService = applicationContext.getBean(NacosConfigManager.class).getConfigService();

        logger.info("日志模块初始化配置：dataId={}, dataGroup={}, configService={}", instance.dataId, instance.dataGroup, instance.configService);
        if (StringUtils.isBlank(instance.dataId) || StringUtils.isBlank(instance.dataGroup) || Objects.isNull(instance.configService)) {
            throw new RuntimeException("日志模块参数加载错误");
        }
        instance.loadConfig();
        logger.info("日志模块初始化配置完成！");
    }

    private void loadConfig() {
        try {
            String config = configService.getConfigAndSignListener(dataId, dataGroup, TIMEOUT_MS, new AbstractConfigChangeListener() {
                @Override
                public void receiveConfigChange(ConfigChangeEvent configChangeEvent) {
                    logger.info("收到监听消息：{}", configChangeEvent);
                    handEvent(configChangeEvent);
                }
            });
            parseConfig(config);
        } catch (NacosException e) {
            logger.error("监听配置数据发生异常，服务端状态：{}", configService.getServerStatus(), e);
            throw new RuntimeException("日志模块初始化配置失败");
        }
    }

    private void handEvent(ConfigChangeEvent configChangeEvent) {
        if (CollectionUtils.isEmpty(configChangeEvent.getChangeItems())) {
            return;
        }
        for (ConfigChangeItem changeItem : configChangeEvent.getChangeItems()) {
            if (PropertyChangeType.DELETED.equals(changeItem.getType())) {
                appData.remove(changeItem.getKey());
            } else {
                appData.put(changeItem.getKey(), changeItem.getNewValue());
            }
            if (!CollectionUtils.isEmpty(dataListener.get(changeItem.getKey()))) {
                for (Runnable runnable : dataListener.get(changeItem.getKey())) {
                    runnable.run();
                }
            }
        }
    }

    private void parseConfig(String config) {
        appData.clear();
        if (StringUtils.isBlank(config)) {
            return;
        }
        for (String line : config.split("\n")) {
            line = line.trim();
            if (StringUtils.isBlank(line) || line.startsWith("#")) {
                continue;
            }
            String[] splitArr = line.split("=", 2);
            if (splitArr.length != 2 || StringUtils.isBlank(splitArr[0]) || StringUtils.isBlank(splitArr[1])) {
                continue;
            }
            appData.put(splitArr[0], splitArr[1]);
        }
    }

    public String getStringProperty(String key, String defaultValue) {
        String value = appData.get(key);
        return Objects.nonNull(value) ? value : defaultValue;
    }

    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = appData.get(key);
        return Objects.nonNull(value) ? Boolean.parseBoolean(value) : defaultValue;
    }

    public void addChangeListener(String key, Runnable runnable) {
        if (dataListener.get(key) == null) {
            dataListener.put(key, new ArrayList<>());
        }
        dataListener.get(key).add(runnable);
    }

    public void removeChangeListener(String key, Runnable runnable) {
        if (dataListener.get(key) == null) {
            return;
        }
        dataListener.get(key).remove(runnable);
    }
}
