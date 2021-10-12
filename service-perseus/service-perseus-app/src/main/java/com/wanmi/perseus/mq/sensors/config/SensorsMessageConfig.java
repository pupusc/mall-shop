package com.wanmi.perseus.mq.sensors.config;

import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
import com.sensorsdata.analytics.javasdk.consumer.BatchConsumer;
import com.sensorsdata.analytics.javasdk.consumer.ConcurrentLoggingConsumer;
import com.sensorsdata.analytics.javasdk.consumer.ConsoleConsumer;
import com.sensorsdata.analytics.javasdk.consumer.DebugConsumer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class SensorsMessageConfig {

    @Value("${shence.log.path:~/sensors/sensors.log}")
    private String shenCeLogPath;
    @Value("${shence.server.url:https://sensors-data.dushu.io/sa?project=default}")
    private String serverUrl;
    @Value("${shence.consumer.bulk-size:1}")
    private Integer bulkSize;

    @Bean(destroyMethod = "shutdown")
    public ISensorsAnalytics init() throws IOException {
        //本地日志模式（此模式会在指定路径生成相应的日志文件）
//        return new SensorsAnalytics(new ConcurrentLoggingConsumer(shenCeLogPath));
//        网络批量发送模式（此模式在容器关闭的时候，如果存在数据还没有发送完毕，就会丢失未发送的数据！！！）
        return new SensorsAnalytics(new BatchConsumer(serverUrl, bulkSize));
//        debug 模式(此模式只适用于测试集成 SDK 功能，千万不要使用到生产环境！！！)
//        return new SensorsAnalytics(new DebugConsumer("https://sensors-data.dushu.io/sa?project=default", true));
    }
}
