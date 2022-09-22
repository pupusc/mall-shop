package com.wanmi.sbc.erp.provider.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.HttpUtil;
import com.wanmi.sbc.erp.api.provider.ShopCenterSaleAfterProvider;
import com.wanmi.sbc.erp.api.req.SaleAfterCreateNewReq;
import com.wanmi.sbc.erp.configuration.shopcenter.ShopCenterRouterConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Slf4j
@RestController
public class ShopCenterSaleAfterController implements ShopCenterSaleAfterProvider {
	@Autowired
	private ShopCenterRouterConfig routerConfig;

	@Override
	public BaseResponse<Long> createSaleAfter(SaleAfterCreateNewReq request) {
		try {
			String host = routerConfig.getHost();
			String url = routerConfig.getUrl("saleAfter.createSaleAfter");

			HttpResponse response = HttpUtil.doPost(host, url, new HashMap<>(), null, JSON.toJSONString(request));
			String str = EntityUtils.toString(response.getEntity());
			JSONObject json = JSON.parseObject(str);
			Long data = JSON.parseObject(json.getString("data"), Long.class);

			return BaseResponse.success(data);
		} catch (Exception e) {
			log.warn("ShopCenterSaleAfterController.createSaleAfter.异常", e);
		}
		return BaseResponse.FAILED();
	}
}
