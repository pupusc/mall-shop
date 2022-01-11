package com.wanmi.sbc.vote.bean;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "vote")
public class VoteConfig {

    private Map<String, VoteBean> goods;
}
