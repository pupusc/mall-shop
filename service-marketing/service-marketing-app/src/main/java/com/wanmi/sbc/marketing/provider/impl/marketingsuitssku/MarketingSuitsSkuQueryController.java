package com.wanmi.sbc.marketing.provider.impl.marketingsuitssku;

import com.wanmi.sbc.marketing.api.request.marketingsuitssku.*;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketinSuitsCountBySkuIdResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.validation.annotation.Validated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.marketingsuitssku.MarketingSuitsSkuQueryProvider;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuPageResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuListResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketingSuitsSkuByIdResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import com.wanmi.sbc.marketing.marketingsuitssku.service.MarketingSuitsSkuService;
import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>组合活动关联商品sku表查询服务接口实现</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@RestController
@Validated
public class MarketingSuitsSkuQueryController implements MarketingSuitsSkuQueryProvider {
	@Autowired
	private MarketingSuitsSkuService marketingSuitsSkuService;

	@Override
	public BaseResponse<MarketingSuitsSkuPageResponse> page(@RequestBody @Valid MarketingSuitsSkuPageRequest marketingSuitsSkuPageReq) {
		MarketingSuitsSkuQueryRequest queryReq = KsBeanUtil.convert(marketingSuitsSkuPageReq, MarketingSuitsSkuQueryRequest.class);
		Page<MarketingSuitsSku> marketingSuitsSkuPage = marketingSuitsSkuService.page(queryReq);
		Page<MarketingSuitsSkuVO> newPage = marketingSuitsSkuPage.map(entity -> marketingSuitsSkuService.wrapperVo(entity));
		MicroServicePage<MarketingSuitsSkuVO> microPage = new MicroServicePage<>(newPage, marketingSuitsSkuPageReq.getPageable());
		MarketingSuitsSkuPageResponse finalRes = new MarketingSuitsSkuPageResponse(microPage);
		return BaseResponse.success(finalRes);
	}

	@Override
	public BaseResponse<MarketingSuitsSkuListResponse> list(@RequestBody @Valid MarketingSuitsSkuListRequest marketingSuitsSkuListReq) {
		MarketingSuitsSkuQueryRequest queryReq = KsBeanUtil.convert(marketingSuitsSkuListReq, MarketingSuitsSkuQueryRequest.class);
		List<MarketingSuitsSku> marketingSuitsSkuList = marketingSuitsSkuService.list(queryReq);
		List<MarketingSuitsSkuVO> newList = marketingSuitsSkuList.stream().map(entity -> marketingSuitsSkuService.wrapperVo(entity)).collect(Collectors.toList());
		return BaseResponse.success(new MarketingSuitsSkuListResponse(newList));
	}

	@Override
	public BaseResponse<MarketingSuitsSkuByIdResponse> getById(@RequestBody @Valid MarketingSuitsSkuByIdRequest marketingSuitsSkuByIdRequest) {
		MarketingSuitsSku marketingSuitsSku =
		marketingSuitsSkuService.getOne(marketingSuitsSkuByIdRequest.getId());
		return BaseResponse.success(new MarketingSuitsSkuByIdResponse(marketingSuitsSkuService.wrapperVo(marketingSuitsSku)));
	}

	@Override
	public BaseResponse<MarketinSuitsCountBySkuIdResponse> getMarketingCountBySkuId(@RequestBody @Valid MarketingSuitsCountBySkuIdRequest marketingSuitsCountBySkuIdRequest) {
			return BaseResponse.success(marketingSuitsSkuService.count(marketingSuitsCountBySkuIdRequest));
	}

}

