package com.wanmi.sbc.init;

import com.wanmi.sbc.setting.api.provider.InitProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化数据
 * @version: 1.0
 */
@Slf4j
@Order(6)
@Component
public class InitRunner implements CommandLineRunner {

	@Autowired
	private InitProvider initProvider;

	@Override
	public void run(String... args) throws Exception {
		initProvider.init();
		log.info("=======系统初始化（公司信息、默认素材分类等）");
	}
}
