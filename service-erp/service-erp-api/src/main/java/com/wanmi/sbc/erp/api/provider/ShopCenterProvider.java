package com.wanmi.sbc.erp.api.provider;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.NewGoodsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.erp.name}", contextId = "ShopCenterProvider")
public interface ShopCenterProvider {

	@PostMapping("/erp/${application.erp.version}/shopcenter/search-goods-info")
	BaseResponse<NewGoodsResponse> searchGoodsInfo(@RequestBody NewGoodsInfoRequest request);
}
