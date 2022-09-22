package com.wanmi.sbc.order.provider.impl.shopcenter;
import com.google.common.collect.Lists;
import java.util.Date;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.erp.api.provider.ShopCenterDeliveryProvider;
import com.wanmi.sbc.erp.api.req.OrderDevItemReq;
import com.wanmi.sbc.erp.api.resp.DevItemResp;
import com.wanmi.sbc.order.api.provider.shopcenter.ShopCenterOrdOrderProvider;
import com.wanmi.sbc.order.api.req.ShopCenterSyncDeliveryExtReq;
import com.wanmi.sbc.order.api.req.ShopCenterSyncDeliveryReq;
import com.wanmi.sbc.order.trade.service.TradePushERPService;
import org.springframework.beans.BeanUtils;
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
		List<DevItemResp> context = response.getContext();

		DevItemResp devItemResp = context.get(0);

		ShopCenterSyncDeliveryExtReq shopCenterSyncDeliveryExtReq = new ShopCenterSyncDeliveryExtReq();
		shopCenterSyncDeliveryExtReq.setNum(devItemResp.getNum());
		shopCenterSyncDeliveryExtReq.setPrice(devItemResp.getPrice());
		shopCenterSyncDeliveryExtReq.setOrderId(devItemResp.getOrderId());
		shopCenterSyncDeliveryExtReq.setOrderItemId(request.getOrderItemId());
		shopCenterSyncDeliveryExtReq.setDevDeliveryId(devItemResp.getDevDeliveryId());
		shopCenterSyncDeliveryExtReq.setMetaGoodsCode(devItemResp.getMetaGoodsCode());
		shopCenterSyncDeliveryExtReq.setMetaSkuCode(devItemResp.getMetaSkuCode());
		shopCenterSyncDeliveryExtReq.setThirdOrderId(devItemResp.getThirdOrderId());
		shopCenterSyncDeliveryExtReq.setThirdOrderNo(devItemResp.getThirdOrderNo());
		shopCenterSyncDeliveryExtReq.setExpressCode(devItemResp.getExpressCode());
		shopCenterSyncDeliveryExtReq.setExpressNo(devItemResp.getExpressNo());
		shopCenterSyncDeliveryExtReq.setStatus(devItemResp.getStatus());
		shopCenterSyncDeliveryExtReq.setLogisticStatus(devItemResp.getLogisticStatus());
		shopCenterSyncDeliveryExtReq.setInterceptStatus(devItemResp.getInterceptStatus());
		shopCenterSyncDeliveryExtReq.setActualNum(devItemResp.getActualNum());
		shopCenterSyncDeliveryExtReq.setRightsBeginTime(devItemResp.getRightsBeginTime());
		shopCenterSyncDeliveryExtReq.setRightsEndTime(devItemResp.getRightsEndTime());
		shopCenterSyncDeliveryExtReq.setPlatformOrderId(request.getPlatformOrderId());
		shopCenterSyncDeliveryExtReq.setPlatformItemIds(request.getPlatformItemIds());
		shopCenterSyncDeliveryExtReq.setExpressNo(request.getExpressNo());
		shopCenterSyncDeliveryExtReq.setExpressCode(request.getExpressCode());
		shopCenterSyncDeliveryExtReq.setPlatformSkuId(request.getPlatformSkuId());
		shopCenterSyncDeliveryExtReq.setOrderItemId(request.getOrderItemId());

		tradePushERPService.fillShopCenterDelivery(shopCenterSyncDeliveryExtReq);
		return BaseResponse.success(null);
	}
}
