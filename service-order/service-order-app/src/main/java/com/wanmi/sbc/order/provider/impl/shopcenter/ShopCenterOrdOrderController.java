package com.wanmi.sbc.order.provider.impl.shopcenter;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.provider.ShopCenterDeliveryProvider;
import com.wanmi.sbc.erp.api.req.OrderDevItemReq;
import com.wanmi.sbc.erp.api.resp.DevItemResp;
import com.wanmi.sbc.order.api.provider.shopcenter.ShopCenterOrdOrderProvider;
import com.wanmi.sbc.order.api.req.ShopCenterSyncDeliveryReq;
import com.wanmi.sbc.order.trade.service.TradePushERPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ShopCenterOrdOrderController implements ShopCenterOrdOrderProvider {
	@Autowired
	private TradePushERPService tradePushERPService;
	@Autowired
	private ShopCenterDeliveryProvider shopCenterDeliveryProvider;

	@Override
	public BaseResponse<Void> shopCenterSyncDelivery(ShopCenterSyncDeliveryReq request) {
		OrderDevItemReq req = new OrderDevItemReq();
		req.setOrderItemId(Long.valueOf(request.getOrderItemId()));
		BaseResponse<List<DevItemResp>> response = shopCenterDeliveryProvider.listDevItem(req);

//		return tradePushERPService.fillShopCenterDelivery(request, response.getContext());
		return null;
	}
}
