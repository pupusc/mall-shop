package com.wanmi.sbc.erp.configuration.shopcenter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Data
@Configuration
@RefreshScope
@ConfigurationProperties(prefix = "shop-center.router")
public class ShopCenterRouterConfig {
	//TODO 改router接口
	private String host;
	private String hostLocal;
	private Map<String, String> urlMap;

	public String getUrl(String key) {
		return urlMap.get(key);
	}
}
