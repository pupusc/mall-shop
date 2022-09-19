package com.wanmi.sbc.erp.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.req.OrderDevItemReq;
import com.wanmi.sbc.erp.api.req.OrderInterceptorReq;
import com.wanmi.sbc.erp.api.resp.DevItemResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 发货
 */
@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterDeliveryProvider")
public interface ShopCenterDeliveryProvider {
	/**
	 *  订单拦截
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/orderInterceptor")
	BaseResponse<Boolean> orderInterceptor(@RequestBody OrderInterceptorReq request);

	/**
	 *  发货数量
	 */
	@PostMapping("/erp/${application.erp.version}/shopcenter/listDevItem")
	BaseResponse<List<DevItemResp>> listDevItem(@RequestBody OrderDevItemReq request);
}
