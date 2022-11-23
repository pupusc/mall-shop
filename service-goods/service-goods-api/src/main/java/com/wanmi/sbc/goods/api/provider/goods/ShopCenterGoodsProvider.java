package com.wanmi.sbc.goods.api.provider.goods;

import com.wanmi.sbc.common.base.BaseResponse;
//import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncCostPriceReq;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncStockReq;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
//import com.wanmi.sbc.goods.api.response.goods.ShopCenterCostPriceSyncResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "${application.goods.name}", contextId = "ShopCenterGoodsProvider")
public interface ShopCenterGoodsProvider {

//	@PostMapping("/goods/${application.goods.version}/shopCenter/sync-goods-stock")
//	BaseResponse<ShopCenterStockSyncResp> shopCenterSyncGoodsStock(@RequestBody ShopCenterSyncStockReq request);

	@PostMapping("/goods/${application.goods.version}/shopCenter/sync-goods-stock-cost-price")
	BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> updateStockAndPrice(@RequestBody ShopCenterSyncStockReq request);

//	@PostMapping("/goods/${application.goods.version}/shopCenter/sync-cost-price")
//	BaseResponse<ShopCenterCostPriceSyncResp> shopCenterSyncCostPrice(@RequestBody ShopCenterSyncCostPriceReq request);
}
