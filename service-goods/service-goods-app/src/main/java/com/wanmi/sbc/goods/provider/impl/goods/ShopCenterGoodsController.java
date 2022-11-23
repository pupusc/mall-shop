package com.wanmi.sbc.goods.provider.impl.goods;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.goods.api.provider.goods.ShopCenterGoodsProvider;
//import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncCostPriceReq;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncStockReq;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
//import com.wanmi.sbc.goods.api.response.goods.ShopCenterCostPriceSyncResp;
import com.wanmi.sbc.goods.info.service.ShopCenterGoodsStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShopCenterGoodsController implements ShopCenterGoodsProvider {
	@Autowired
	private ShopCenterGoodsStockService shopCenterGoodsStockService;

//	@Override
//	public BaseResponse<ShopCenterStockSyncResp> shopCenterSyncGoodsStock(ShopCenterSyncStockReq request) {
//		return shopCenterGoodsStockService.updateStock(request.getGoodsCode(), request.getQuantity());
//	}

	@Override
	public BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> updateStockAndPrice(ShopCenterSyncStockReq request) {
		return BaseResponse.success(shopCenterGoodsStockService.updateStockAndPrice(request.getGoodsCode(), request.getQuantity()));
	}

//	@Override
//	public BaseResponse<ShopCenterCostPriceSyncResp> shopCenterSyncCostPrice(ShopCenterSyncCostPriceReq request) {
//		return shopCenterGoodsStockService.updateCostPrice(request.getGoodsCode(), request.getGoodsPrice());
//	}
}
