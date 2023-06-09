package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterOrderProvider;
import com.wanmi.sbc.erp.api.req.CreateOrderReq;
import com.wanmi.sbc.erp.api.req.OrdItemReq;
import com.wanmi.sbc.erp.api.resp.CreateOrderResp;
import com.wanmi.sbc.erp.api.resp.OrdItemResp;
import com.wanmi.sbc.erp.api.resp.OrdOrderResp;
import com.wanmi.sbc.erp.api.resp.OrderDetailResp;
import com.wanmi.sbc.erp.api.resp.PaymentResp;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class ShopCenterOrderController implements ShopCenterOrderProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;


	@Override
	public BaseResponse<CreateOrderResp> createOrder(CreateOrderReq request) {
		String errorContent = "";
		long beginTime = System.currentTimeMillis();
		try {
//			String host = routerConfig.getHost();
			log.info("createOrder start，request:{}", JSON.toJSONString(request));
			String host = routerConfig.getHostLocal();
			String url = routerConfig.getUrl("order.createOrder");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());

			log.info("createOrder end，result:{}", str);
			JSONObject json = JSON.parseObject(str);
			String status = json.getString("status");
			errorContent = json.getString("msg");
			CreateOrderResp createOrderResp = new CreateOrderResp();
			if (Objects.equals("0000", status)) {
				String data = json.getString("data");
				createOrderResp.setCode("0000");
				createOrderResp.setThirdOrderId(data);
				return BaseResponse.success(createOrderResp);
			}


			// 重复订单
			if ("40000".equals(status)) {
//				return BaseResponse.info("40000", errorContent);
				createOrderResp.setCode("40000");
				return BaseResponse.success(createOrderResp);
			}

		} catch (Exception e) {
			log.warn("ShopCenterOrderController.createOrder异常", e);
		} finally {
			log.info("ShopCenterOrderController.createOrder param: {} cost {}s", JSON.toJSONString(request), (System.currentTimeMillis() - beginTime) / 1000);
		}
		return BaseResponse.info("99999", errorContent);
	}

	@Override
	public BaseResponse<OrderDetailResp> detailByPlatformOrderId(String platformOrderId) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("order.detailByPlatformOrderId");

			JSONObject param = new JSONObject();
			param.put("platformOrderId", platformOrderId);
			log.info("ShopCenterOrderController detailByPlatformOrderId platformOrderId {}", param);
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			OrderDetailResp data = JSON.parseObject(json.getString("data"), OrderDetailResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterOrderController.detailByPlatformOrderId.异常", e);
		}
		return BaseResponse.FAILED();
	}

//	@Override
//	public BaseResponse<List<OrderDetailResp>> listOrder(OrderQueryReq request) {
//		try {
//			String host = routerConfig.getHost();
//			String url = routerConfig.getUrl("order.listOrder");
//
//
//			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
//			String str = EntityUtils.toString(response.getEntity());
//			JSONObject json = JSON.parseObject(str);
//			List<OrderDetailResp> data = JSON.parseArray(json.getString("data"), OrderDetailResp.class);
//
//			return BaseResponse.success(data);
//		} catch (Exception e) {
//			log.warn("ShopCenterOrderController.listOrder异常", e);
//		}
//		return BaseResponse.FAILED();
//	}

//	@Override
//	public BaseResponse<OrdOrderResp> queryMasterOrderByTid(Long tid) {
//		try {
//			String host = routerConfig.getHost();
//			String url = routerConfig.getUrl("order.queryMasterOrderByTid");
//
//			JSONObject param = new JSONObject();
//			param.put("tid", tid);
//
//			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
//			String str = EntityUtils.toString(response.getEntity());
//			JSONObject json = JSON.parseObject(str);
//			OrdOrderResp data = JSON.parseObject(json.getString("data"), OrdOrderResp.class);
//
//			return BaseResponse.success(data);
//		} catch (Exception e) {
//			log.warn("ShopCenterOrderController.queryMasterOrderByTid.异常", e);
//		}
//		return BaseResponse.FAILED();
//	}


//	@Override
//	public BaseResponse<List<PaymentResp>> getPaymentByOrderId(Long orderId) {
//		try {
//			String host = routerConfig.getHost();
//			String url = routerConfig.getUrl("order.getPaymentByOrderId");
//
//			JSONObject param = new JSONObject();
//			param.put("orderId", orderId);
//
//			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
//			String str = EntityUtils.toString(response.getEntity());
//			JSONObject json = JSON.parseObject(str);
//			List<PaymentResp> data = JSON.parseArray(json.getString("data"), PaymentResp.class);
//
//			return BaseResponse.success(data);
//		} catch (Exception e) {
//			log.warn("ShopCenterSaleAfterController.getPaymentByOrderId.异常", e);
//		}
//		return BaseResponse.success(Collections.emptyList());
//	}

	@Override
	public BaseResponse<List<OrdItemResp>> listOrdItem(OrdItemReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("order.listOrdItem");


			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			List<OrdItemResp> data = JSON.parseArray(str, OrdItemResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.getPaymentByOrderId.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}
}
