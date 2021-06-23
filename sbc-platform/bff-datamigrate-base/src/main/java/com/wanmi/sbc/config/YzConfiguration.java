package com.wanmi.sbc.config;

import com.youzan.cloud.open.sdk.core.client.core.DefaultYZClient;
import com.youzan.cloud.open.sdk.core.client.core.YouZanClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class YzConfiguration {

    @Bean
    YouZanClient yzClient() {
        YouZanClient yzClient = new DefaultYZClient();
        return yzClient;
    }
}
