//package com.wanmi.sbc.common.configure;
//
//import com.sensorsdata.analytics.javasdk.ISensorsAnalytics;
//import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
//import com.sensorsdata.analytics.javasdk.consumer.ConcurrentLoggingConsumer;
////import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.util.StringUtils;
//
//import java.io.IOException;
//
//@Configuration
//public class SensorsConfig {
//
////    @Value("${shence.log.path}")
//    private String shenCeLogPath;
//
//    @Bean(destroyMethod = "shutdown")
//    public ISensorsAnalytics init() throws IOException {
//        //本地日志模式（此模式会在指定路径生成相应的日志文件）
//        if (StringUtils.isEmpty(shenCeLogPath)) {
//            shenCeLogPath = "/data/tmp/shence";
//        }
//        return new SensorsAnalytics(new ConcurrentLoggingConsumer(shenCeLogPath));
//        //debug 模式(此模式只适用于测试集成 SDK 功能，千万不要使用到生产环境！！！)
//        //return new SensorsAnalytics(new DebugConsumer("数据接收地址", true));
//        //网络批量发送模式（此模式在容器关闭的时候，如果存在数据还没有发送完毕，就会丢失未发送的数据！！！）
//        //return new SensorsAnalytics(new BatchConsumer("数据接收地址"));
//    }
//
//}