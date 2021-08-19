package com.wanmi.sbc.configure;

import org.apache.catalina.valves.ErrorReportValve;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * tomcat请求参数配置
 *
 * @author qiaoyu
 */
@Configuration
public class TomcatConfig {

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addContextCustomizers((context) -> {
            ErrorReportValve valve = new ErrorReportValve();
            valve.setShowServerInfo(false);
            valve.setShowReport(false);
            context.getParent().getPipeline().addValve(valve);
        });
        return factory;
    }
}
