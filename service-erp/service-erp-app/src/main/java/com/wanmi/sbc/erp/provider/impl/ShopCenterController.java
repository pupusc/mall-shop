package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.vo.NewGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterProvider;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.NewGoodsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
public class ShopCenterController implements ShopCenterProvider {
	@Value("${shopCenter.searchGoodsInfo.host}")
	private String host;
	@Value("${shopCenter.searchGoodsInfo.url}")
	private String url;

	@Override
	public BaseResponse<NewGoodsResponse> searchGoodsInfo(NewGoodsInfoRequest request) {
		try {
			host = StringUtils.isNotEmpty(host) ? host : "http://callback-test.dushu365.com";
			//TODO 商品接口 改router接口，有baseResponse
			url = StringUtils.isNotEmpty(url) ? url : "/product-meta/newGoods/list/arcticfoxenv/test35";
			JSONObject param = new JSONObject();
			// TODO 查询商城
			param.put("saleChannelId", 21);
			param.put("goodsCode", request.getGoodsCode());
			if (Objects.nonNull(request.getValidFlag())) {
				param.put("validFlag", request.getValidFlag());
			}
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSONObject.parseObject(str);
			List<NewGoodsInfoVO> stockInfoVOS = JSONArray.parseArray(json.getString("data"), NewGoodsInfoVO.class);
//			List<NewGoodsInfoVO> stockInfoVOS = JSONArray.parseArray(str, NewGoodsInfoVO.class);

			NewGoodsResponse result = new NewGoodsResponse();
			result.setGoodsInfoList(stockInfoVOS);
			return BaseResponse.success(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseResponse.FAILED();
	}
}
