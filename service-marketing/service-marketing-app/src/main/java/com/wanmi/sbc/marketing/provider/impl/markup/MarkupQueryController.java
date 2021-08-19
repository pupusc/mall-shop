package com.wanmi.sbc.marketing.provider.impl.markup;

import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelBySkuRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelBySkuResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupListResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupSkuIdsResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.markup.MarkupQueryProvider;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelByIdRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelByIdResponse;
import com.wanmi.sbc.marketing.markup.service.MarkupService;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>加价购活动查询服务接口实现</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@RestController
@Validated
public class MarkupQueryController implements MarkupQueryProvider {
	@Autowired
	private MarkupService markupService;

	@Override
	public BaseResponse<MarkupLevelByIdResponse> getLevelById(@RequestBody @Valid MarkupLevelByIdRequest markupLevelByIdRequest) {
		return BaseResponse.success(markupService.getLevelById(markupLevelByIdRequest));
	}

	@Override
	public BaseResponse<MarkupListResponse> getMarkupList(@Valid MarkupListRequest markupListRequest) {
		return BaseResponse.success(MarkupListResponse.builder().levelList(markupService.getMarkupList(markupListRequest)).build());
	}

	@Override
	public BaseResponse<MarkupLevelBySkuResponse> getMarkupListBySku(MarkupLevelBySkuRequest markupLevelBySkuRequest) {
		return BaseResponse.success(markupService.getMarkupListBySku(markupLevelBySkuRequest));
	}

	@Override
	public BaseResponse<MarkupSkuIdsResponse> getMarkupSku(MarketingIdRequest marketingIdRequest) {
		return BaseResponse.success(MarkupSkuIdsResponse.builder().levelList(markupService.getMarkupSku(marketingIdRequest)).build());
	}
}

