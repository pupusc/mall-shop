package com.wanmi.sbc.order.api.provider.shopcenter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.order.api.req.ShopCenterSyncDeliveryReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.order.name}", contextId = "ShopCenterOrdOrderProvider")
public interface ShopCenterOrdOrderProvider {

	/**
	 * 同步发货
	 */
	@PostMapping("/order/${application.order.version}/groupon/instance/page-criteria")
	BaseResponse<Void> shopCenterSyncDelivery(@RequestBody ShopCenterSyncDeliveryReq request);

}
