package com.wanmi.sbc.erp.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.OrderInterceptorReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 发货
 */
@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterDeliveryProvider")
public interface ShopCenterDeliveryProvider {
	/**
	 * TODO 订单拦截
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/orderInterceptor")
	BaseResponse<Boolean> orderInterceptor(@RequestBody OrderInterceptorReq request);
}
