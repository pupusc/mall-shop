package com.wanmi.sbc.marketing.provider.impl.marketingsuits;


import com.wanmi.sbc.marketing.api.request.marketingsuits.*;
import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import com.wanmi.sbc.marketing.marketingsuits.service.MarketingSuitsService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.marketingsuits.MarketingSuitsProvider;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsAddResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsModifyResponse;

import javax.validation.Valid;

/**
 * <p>组合商品主表保存服务接口实现</p>
 * @author zhk
 * @date 2020-04-01 20:54:00
 */
@RestController
@Validated
public class MarketingSuitsController implements MarketingSuitsProvider {
	@Autowired
	private MarketingSuitsService marketingSuitsService;

	@Override
	public BaseResponse<MarketingSuitsAddResponse> add(@RequestBody @Valid MarketingSuitsSaveRequest marketingSuitsSaveRequest) {
		marketingSuitsService.add(marketingSuitsSaveRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse modify(@RequestBody @Valid MarketingSuitsSaveRequest marketingSuitsSaveRequest) {
		marketingSuitsService.modify(marketingSuitsSaveRequest);
		return BaseResponse.SUCCESSFUL();
	}

	@Override
	public BaseResponse deleteByIdList(@Valid MarketingSuitsDelByIdListRequest marketingSuitsDelByIdListRequest) {
		return null;
	}

//	@Override
//	public BaseResponse deleteByIdList(@RequestBody @Valid MarketingSuitsDelByIdListRequest marketingSuitsDelByIdListRequest) {
//		marketingSuitsService.deleteByIdList(marketingSuitsDelByIdListRequest.getIdList());
//		return BaseResponse.SUCCESSFUL();
//	}

}

