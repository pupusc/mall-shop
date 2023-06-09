package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.enums.ShopCenterEnum;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;
import com.wanmi.sbc.erp.api.req.SalePlatformQueryReq;
import com.wanmi.sbc.erp.api.req.ShopCenterGoodsStockOrCostPriceReq;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.resp.NewGoodsInfoResp;
import com.wanmi.sbc.erp.api.resp.SalePlatformResp;
import com.wanmi.sbc.erp.api.resp.ShopCenterGoodsCostPriceResp;
import com.wanmi.sbc.erp.api.resp.ShopCenterGoodsStockResp;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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
public class ShopCenterProductController implements ShopCenterProductProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;

	@Override
	public BaseResponse<List<NewGoodsInfoResp>> searchGoodsInfo(NewGoodsInfoRequest request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("product.searchGoodsInfo");

			// 先查询虚拟商品
			JSONObject param = (JSONObject) JSON.toJSON(request);
			param.put("includeVirtualGoods", 1);
			log.info("ShopCenterProductController searchGoodsInfo searchGoodsInfo 虚拟 reqt {}", JSON.toJSONString(param));
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			log.info("ShopCenterProductController searchGoodsInfo searchGoodsInfo 虚拟 resp {}", str);
			JSONObject json = JSON.parseObject(str);
			List<NewGoodsInfoResp> infoVOList = JSON.parseArray(json.getString("data"), NewGoodsInfoResp.class);
			if (CollectionUtils.isNotEmpty(infoVOList)) {
				return BaseResponse.success(infoVOList);
			}


			// 查询商城渠道商品
			JSONObject param1 = (JSONObject) JSON.toJSON(request);
			param1.put("saleChannelId", 21);
			log.info("ShopCenterProductController searchGoodsInfo searchGoodsInfo 渠道 reqt {}", JSON.toJSONString(param1));
			HttpResponse response1 = HttpUtil.doPost(host, url, new HashMap<>(), null, param1.toJSONString());
			String str1 = EntityUtils.toString(response1.getEntity());
			log.info("ShopCenterProductController searchGoodsInfo searchGoodsInfo 渠道 resp {}", str1);
			JSONObject json1 = JSON.parseObject(str1);
			List<NewGoodsInfoResp> infoVOList1 = JSON.parseArray(json1.getString("data"), NewGoodsInfoResp.class);
			return BaseResponse.success(infoVOList1);
		} catch (Exception e) {
			log.warn("ShopCenterController.searchGoodsInfo.异常", e);
		}
		return BaseResponse.success(Collections.emptyList());
	}

//	@Override
//	public BaseResponse<List<GoodsPackRsp>> searchPackByGoodsCodes(List<String> request) {
//		try {
//			String host = routerConfig.getHost();
//			String url = routerConfig.getUrl("product.searchPackByGoodsCodes");
//
//			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
//			String str = EntityUtils.toString(response.getEntity());
//			JSONObject json = JSON.parseObject(str);
//			List<GoodsPackRsp> data = JSON.parseArray(json.getString("data"), GoodsPackRsp.class);
//
//			return BaseResponse.success(data);
//		} catch (Exception e) {
//			log.warn("ShopCenterController.searchPackByGoodsCodes.异常", e);
//		}
//		return BaseResponse.success(Collections.emptyList());
//	}

	@Override
	public BaseResponse<SalePlatformResp> getSalePlatform(SalePlatformQueryReq request) {
		try {
			String host = routerConfig.getHost();
//			String host = routerConfig.getHostLocal();
			String url = routerConfig.getUrl("product.getSalePlatform");
			log.info("ShopCenterProductController getSalePlatform param {}", JSON.toJSONString(request));
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			log.info("ShopCenterProductController getSalePlatform result {}", JSON.toJSONString(str));

			SalePlatformResp data = JSON.parseObject(str, SalePlatformResp.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterProductController.getSalePlatform异常", e);
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<List<ShopCenterGoodsStockResp>> searchGoodsStock(ShopCenterGoodsStockOrCostPriceReq shopCenterGoodsStockReq) {

		String errorContent = "访问失败";
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("product.searchGoodsStock");
			String req = JSON.toJSONString(shopCenterGoodsStockReq);
			log.info("ShopCenterProductController searchGoodsStock request {}", req);
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, req);
			String str = EntityUtils.toString(response.getEntity());
			log.info("ShopCenterProductController searchGoodsStock resp {}", str);
			JSONObject json = JSON.parseObject(str);
			String status = json.getString("status");
			errorContent = json.getString("msg");
			if (Objects.equals("0000", status)) {
				List<ShopCenterGoodsStockResp> data = JSON.parseArray(json.getString("data"), ShopCenterGoodsStockResp.class);
				return BaseResponse.success(data);
			}
		} catch (Exception e) {
			log.error("ShopCenterController.searchGoodsStock.异常", e);
		}
		return BaseResponse.info("99999", errorContent);
	}

	@Override
	public BaseResponse<List<ShopCenterGoodsCostPriceResp>> searchGoodsCostPrice(ShopCenterGoodsStockOrCostPriceReq shopCenterGoodsStockReq) {

		String errorContent = "访问失败";
		try {

			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("product.searchGoodsCostprice");
			String req = JSON.toJSONString(shopCenterGoodsStockReq);
			log.info("ShopCenterProductController searchGoodsCostPrice request {}", req);
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, req);
			String str = EntityUtils.toString(response.getEntity());
			log.info("ShopCenterProductController searchGoodsCostPrice resp {}", str);
			JSONObject json = JSON.parseObject(str);
			String status = json.getString("status");
			errorContent = json.getString("msg");
			if (Objects.equals("0000", status)) {
				List<ShopCenterGoodsCostPriceResp> data = JSON.parseArray(json.getString("data"), ShopCenterGoodsCostPriceResp.class);
				return BaseResponse.success(data);
			}
		} catch (Exception e) {
			log.error("ShopCenterController.searchGoodsCostPrice.异常", e);
		}
		return BaseResponse.info("99999", errorContent);
	}
}
