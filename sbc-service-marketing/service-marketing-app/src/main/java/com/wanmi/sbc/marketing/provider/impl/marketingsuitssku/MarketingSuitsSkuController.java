package com.wanmi.sbc.marketing.provider.impl.marketingsuitssku;

import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsSaveRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.marketingsuitssku.MarketingSuitsSkuProvider;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuAddResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuModifyResponse;
import com.wanmi.sbc.marketing.marketingsuitssku.service.MarketingSuitsSkuService;
import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import javax.validation.Valid;

/**
 * <p>组合活动关联商品sku表保存服务接口实现</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@RestController
@Validated
public class MarketingSuitsSkuController implements MarketingSuitsSkuProvider {
	@Autowired
	private MarketingSuitsSkuService marketingSuitsSkuService;

	@Override
	public BaseResponse deleteByIdList(@RequestBody @Valid MarketingSuitsSkuDelByIdListRequest marketingSuitsSkuDelByIdListRequest) {
//		marketingSuitsSkuService.deleteByIdList(marketingSuitsSkuDelByIdListRequest.getIdList());
		return BaseResponse.SUCCESSFUL();
	}

}

