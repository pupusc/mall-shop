package com.wanmi.sbc.init;


import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 优惠券券码表-历史数据迁移
 * @author: Geek Wang
 * @createDate: 2019/4/25 10:00
 * @version: 1.0
 */
@Slf4j
@Order(5)
@Component
public class CouponCodeHistoricalDataRunner implements CommandLineRunner {

	@Autowired
	private CouponCodeProvider couponCodeProvider;

	@Override
	public void run(String... args) throws Exception {
		BaseResponse baseResponse = couponCodeProvider.dataMigrationFromCouponCode();
		log.info("优惠券券码-历史数据迁移成功，结果数:{}",baseResponse.getContext());
	}
}
