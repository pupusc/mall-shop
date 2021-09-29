//package com.wanmi.sbc.order.configuration;
//
//import com.sensorsdata.analytics.javasdk.SensorsAnalytics;
//import com.sensorsdata.analytics.javasdk.consumer.ConcurrentLoggingConsumer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.IOException;
//
///**
// * Description:
// * Company    : 上海黄豆网络科技有限公司
// * Author     : duanlongshan@dushu365.com
// * Date       : 2021/9/28 4:49 下午
// * Modify     : 修改日期          修改人员        修改说明          JIRA编号
// ********************************************************************/
//
//@Configuration
//@Slf4j
//public class SensorsDataConfigure {
//
//    @Value("${sensors.data.path}")
//    private String sensorsDataPath;
//
//    @Bean
//    public SensorsAnalytics sensorsAnalytics() throws IOException {
//        log.info("埋点神策初始化地址为：{}", sensorsDataPath);
//        return new SensorsAnalytics(new ConcurrentLoggingConsumer(sensorsDataPath));
//    }
//}
