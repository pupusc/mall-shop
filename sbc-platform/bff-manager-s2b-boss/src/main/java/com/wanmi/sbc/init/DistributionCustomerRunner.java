package com.wanmi.sbc.init;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 初始化历史分销员-邀请码
 * @author: Geek Wang
 * @createDate: 2019/4/25 10:08
 * @version: 1.0
 */
@Slf4j
@Order(3)
@Component
public class DistributionCustomerRunner implements CommandLineRunner {

	@Autowired
	private DistributionCustomerSaveProvider distributionCustomerSaveProvider;

	@Override
	public void run(String... args) throws Exception {
		BaseResponse baseResponse = distributionCustomerSaveProvider.initInviteCode();
		log.info("========初始化历史分销员-邀请码,是否成功：{}==============",baseResponse.getContext().equals(Boolean.TRUE) ? "成功" : "失败");
	}
}
