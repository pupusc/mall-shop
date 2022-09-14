package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.enums.SaleAfterRefundTypeEnum;
import com.wanmi.sbc.erp.api.enums.UnifiedSaleAfterStatusEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterSaleAfterProvider;
import com.wanmi.sbc.erp.api.req.QuerySaleAfterOrderReq;
import com.wanmi.sbc.erp.api.req.SaleAfterCancelReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmDeliverReq;
import com.wanmi.sbc.erp.api.req.SaleAfterConfirmPaymentReq;
import com.wanmi.sbc.erp.api.req.SaleAfterExamineReq;
import com.wanmi.sbc.erp.api.req.SaleAfterReq;
import com.wanmi.sbc.erp.api.resp.SaleAfterFillDeliverInfoReq;
import com.wanmi.sbc.erp.api.resp.SaleAfterItemResp;
import com.wanmi.sbc.erp.api.resp.SaleAfterOrderResp;
import com.wanmi.sbc.erp.api.resp.SaleAfterResp;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ShopCenterSaleAfterController implements ShopCenterSaleAfterProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;


	@Override
	public BaseResponse<List<SaleAfterResp>> createSaleAfter(SaleAfterReq request) {

		return null;
	}




	@Override
	public BaseResponse<List<SaleAfterResp>> saleAfterList(SaleAfterReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.saleAfterList");

			JSONObject param = (JSONObject) JSON.toJSON(request);
			param.putAll(param.getJSONObject("page"));

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			List<SaleAfterResp> data = JSON.parseArray(json.getString("data"), SaleAfterResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.saleAfterList.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}

	@Override
	public BaseResponse<List<SaleAfterOrderResp>> querySaleAfterOrder(QuerySaleAfterOrderReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.querySaleAfterOrder");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			List<SaleAfterOrderResp> data = JSON.parseArray(json.getString("data"), SaleAfterOrderResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.querySaleAfterOrder.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}

	@Override
	public BaseResponse<Boolean> examineSaleAfter(SaleAfterExamineReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.examineSaleAfter");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.examineSaleAfter.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<Boolean> cancelSaleAfter(SaleAfterCancelReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.cancelSaleAfter");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.cancelSaleAfter.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<Boolean> fillDeliverInfo(SaleAfterFillDeliverInfoReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.fillDeliverInfo");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.fillDeliverInfo.异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<Boolean> confirmDeliver(SaleAfterConfirmDeliverReq request) {
		try {
			QuerySaleAfterOrderReq querySaleAfterOrderBO = new QuerySaleAfterOrderReq();
			querySaleAfterOrderBO.setSaId(request.getSaOrderTid());
			SaleAfterOrderResp saleAfterOrderResp = querySaleAfterOrder(querySaleAfterOrderBO).getContext().get(0);
			List<SaleAfterItemResp> items = saleAfterOrderResp.getItems();
			List<Integer> refundTypeList = items.stream().map(SaleAfterItemResp::getRefundType).collect(Collectors.toList());


			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.confirmDeliver");

			JSONObject param = (JSONObject) JSON.toJSON(request);
			// 如果退款类型为子订单换货且不包含主订单仅退款，则直接改为售后完成。其他则为确认打款
			if (refundTypeList.contains(SaleAfterRefundTypeEnum.EXCHANGE_GOODS.getCode())
					&& !refundTypeList.contains(SaleAfterRefundTypeEnum.MASTER_ONLY_REFUND.getCode())) {
				param.put("status", UnifiedSaleAfterStatusEnum.SALE_AFTER_DONE.getCode());
			} else {
				param.put("status", UnifiedSaleAfterStatusEnum.REFUND_MONEY.getCode());
			}

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.confirmDeliver.异常", e);
		}
		return BaseResponse.FAILED();

	}

	@Override
	public BaseResponse<Boolean> confirmPayment(SaleAfterConfirmPaymentReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.confirmPayment");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Boolean data = JSON.parseObject(json.getString("data"), Boolean.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.confirmPayment.异常", e);
		}
		return BaseResponse.FAILED();
	}
}
