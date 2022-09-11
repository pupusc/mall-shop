package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.vo.NewGoodsInfoVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterProvider;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.response.NewGoodsResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class ShopCenterController implements ShopCenterProvider {

	@Override
	public BaseResponse<NewGoodsResponse> searchGoodsInfo(NewGoodsInfoRequest request) {
		try {
			String host = "https://gateway-api.dushu365.com";
			//TODO 商品接口 改router接口，有baseResponse
			String url = "/product-meta/newGoods/goodsInfoList";
			JSONObject param = new JSONObject();
			param.put("goodsCode", request.getMetaGoodsCode());
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			List<NewGoodsInfoVO> stockInfoVOS = JSONArray.parseArray(str, NewGoodsInfoVO.class);

			NewGoodsResponse result = new NewGoodsResponse();
			result.setGoodsInfoList(stockInfoVOS);
			return BaseResponse.success(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return BaseResponse.FAILED();
	}

	@Override
	public BaseResponse<List<NewGoodsInfoVO>> listWareHoseStock(Integer pageNum, Integer pageSize, String goodsCode) {
		if (pageNum == null) {
			pageNum = 1;
		}
		if (pageSize == null) {
			pageSize = 100;
		}
		JSONObject param = new JSONObject();
		param.put("pageNo", pageNum);
		param.put("pageSize", pageSize);
		param.put("metaGoodsCode", goodsCode);
		param.put("shelfFlag", 1); // 上架状态库存

		try {
			String host = "https://gateway-api.dushu365.com";
			//TODO 库存接口 改router接口，有baseResponse
			String url = "/product-meta/newGoods/stockList";
			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, param.toJSONString());
			String str = EntityUtils.toString(response.getEntity());
			List<NewGoodsInfoVO> stockInfoVOS = JSONArray.parseArray(str, NewGoodsInfoVO.class);
			return BaseResponse.success(stockInfoVOS);
		} catch (Exception ex) {
			log.error("GuanyierpService listWareHoseStock error", ex);
		}
		return BaseResponse.success(Collections.emptyList());
	}
}
