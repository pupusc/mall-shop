package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.vo.NewGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterProvider;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.NewGoodsResponse;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ShopCenterController implements ShopCenterProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;

	@Override
	public BaseResponse<NewGoodsResponse> searchGoodsInfo(NewGoodsInfoRequest request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("product.searchGoodsInfo");

			// 先查询虚拟商品
			JSONObject param = (JSONObject) JSON.toJSON(request);
			param.put("bizType", 4);
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			List<NewGoodsInfoVO> infoVOList = JSON.parseArray(json.getString("data"), NewGoodsInfoVO.class);
			if (CollectionUtils.isNotEmpty(infoVOList)) {
				NewGoodsResponse result = new NewGoodsResponse();
				result.setGoodsInfoList(infoVOList);
				return BaseResponse.success(result);
			}


			// 查询商城渠道商品
			JSONObject param1 = (JSONObject) JSON.toJSON(request);
			param1.put("saleChannelId", 21);
			HttpResponse response1 = HttpUtil.doPost(host, url, new HashMap<>(), null, param1.toJSONString());
			String str1 = EntityUtils.toString(response1.getEntity());
			JSONObject json1 = JSON.parseObject(str1);
			List<NewGoodsInfoVO> infoVOList1 = JSON.parseArray(json1.getString("data"), NewGoodsInfoVO.class);
			NewGoodsResponse result = new NewGoodsResponse();
			result.setGoodsInfoList(infoVOList1);
			return BaseResponse.success(result);
		} catch (Exception e) {
			log.warn("ShopCenterController.searchGoodsInfo异常", e);
		}
		return BaseResponse.FAILED();
	}
}
