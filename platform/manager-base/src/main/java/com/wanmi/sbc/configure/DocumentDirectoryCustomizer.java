package com.wanmi.sbc.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class DocumentDirectoryCustomizer implements WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
    @Value("${server.tomcat.basedir}")
    private String docBase;

    /**
     * 文件上传临时路径
     */
    @Override
    public void customize(ConfigurableServletWebServerFactory factory) {
        factory.setDocumentRoot(new File(docBase));
    }
}
