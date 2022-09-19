package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterDeliveryProvider;
import com.wanmi.sbc.erp.api.req.OrderDevItemReq;
import com.wanmi.sbc.erp.api.req.OrderInterceptorReq;
import com.wanmi.sbc.erp.api.resp.DevItemResp;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ShopCenterDeliveryController implements ShopCenterDeliveryProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;

	@Override
	public BaseResponse<Boolean> orderInterceptor(OrderInterceptorReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("delivery.orderInterceptor");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			Boolean data = JSON.parseObject(str, Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.orderInterceptor.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<List<DevItemResp>> listDevItem(OrderDevItemReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("delivery.listDevItem");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			List<DevItemResp> data = JSON.parseArray(str, DevItemResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.listDevItem.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}
}
