package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.vo.MetaStockInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterProvider;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.MetaStockResponse;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
public class ShopCenterController implements ShopCenterProvider {
	@Override
	public BaseResponse<MetaStockResponse> searchGoodsInfo(NewGoodsInfoRequest request) {
		try {
			String host = "https://gateway-api.dushu365.com";
			//TODO
			String url = "/central-cms/admin/shopCenter/metaStock/list";
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSONObject.parseObject(str);
			List<MetaStockInfoVO> stockInfoVOS = JSONArray.parseArray(json.getString("data"), MetaStockInfoVO.class);

			MetaStockResponse result = new MetaStockResponse();
			result.setGoodsInfoList(stockInfoVOS);
			return BaseResponse.success(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseResponse.FAILED();
	}
}
