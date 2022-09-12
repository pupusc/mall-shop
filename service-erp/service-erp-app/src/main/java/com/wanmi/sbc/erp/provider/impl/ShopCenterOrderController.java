package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.OrderQueryReq;
import com.wanmi.sbc.erp.api.resp.CreateOrderResp;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ShopCenterOrderController implements ShopCenterOrderProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;


	@Override
	public BaseResponse<CreateOrderResp> createOrder(CreateOrderReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("order.createOrder");


			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			CreateOrderResp data = JSON.parseObject(json.getString("data"), CreateOrderResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterOrderController.createOrder异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<OrderDetailResp> detailByOrderNumber(Long orderNumber) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("order.detailByOrderNumber");

			JSONObject param = new JSONObject();
			param.put("orderNumber", orderNumber);

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			OrderDetailResp data = JSON.parseObject(json.getString("data"), OrderDetailResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterOrderController.detailByOrderNumber异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<List<OrderDetailResp>> listOrder(OrderQueryReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("order.listOrder");


			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			List<OrderDetailResp> data = JSON.parseArray(json.getString("data"), OrderDetailResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterOrderController.listOrder异常", e);
		}
		return BaseResponse.FAILED();
	}
}
