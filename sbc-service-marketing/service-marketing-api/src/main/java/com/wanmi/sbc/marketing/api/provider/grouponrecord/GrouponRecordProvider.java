package com.wanmi.sbc.marketing.api.provider.grouponrecord;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordDecrBuyNumRequest;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordIncrBuyNumRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 拼团记录
 */
@FeignClient(value = "${application.marketing.name}", contextId = "GrouponRecordProvider")
public interface GrouponRecordProvider {


	/**
	 * 根据活动ID、会员ID、SKU编号更新已购买数量（增加操作）
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/groupon/record/incr-buy-num")
	BaseResponse incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(@RequestBody @Valid GrouponRecordIncrBuyNumRequest
																						request);

	/**
	 * 根据活动ID、会员ID、SKU编号更新已购买数量（减少操作）
	 * @param request
	 * @return
	 */
	@PostMapping("/marketing/${application.marketing.version}/groupon/record/decr-buy-num")
	BaseResponse decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(@RequestBody @Valid GrouponRecordDecrBuyNumRequest
																				  request);
}

