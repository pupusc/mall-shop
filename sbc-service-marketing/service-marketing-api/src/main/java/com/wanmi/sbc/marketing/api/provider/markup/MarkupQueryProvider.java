package com.wanmi.sbc.marketing.api.provider.markup;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.market.MarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelByIdRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupLevelBySkuRequest;
import com.wanmi.sbc.marketing.api.request.markup.MarkupListRequest;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelByIdResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupLevelBySkuResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupListResponse;
import com.wanmi.sbc.marketing.api.response.markup.MarkupSkuIdsResponse;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import java.util.List;

/**
 * <p>加价购活动查询服务Provider</p>
 * @author he
 * @date 2021-02-04 16:09:09
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarkupQueryProvider")
public interface MarkupQueryProvider {


	/**
	 * 单个查询加价购活动API
	 *
	 * @author he
	 * @param markupLevelByIdRequest 单个查询加价购活动请求参数 {@link MarkupLevelByIdRequest}
	 * @return 加价购活动详情 {@link MarkupLevelByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/markup/get-by-id")
	BaseResponse<MarkupLevelByIdResponse> getLevelById(@RequestBody @Valid MarkupLevelByIdRequest markupLevelByIdRequest);
	/**
	 * 单个查询加价购活动API
	 *
	 * @author he
	 * @param markupLevelByIdRequest 单个查询加价购活动请求参数 {@link MarkupLevelByIdRequest}
	 * @return 加价购活动详情 {@link MarkupLevelByIdResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/markup/get-list")
	BaseResponse<MarkupListResponse> getMarkupList(@RequestBody @Valid  MarkupListRequest markupListRequest);
	/**
	 * 通过sku和金额查询满足加价购活动API
	 *
	 * @author he
	 * @param markupLevelBySkuRequest 通过sku和金额查询满足加价购活动API {@link MarkupLevelByIdRequest}
	 * @return 加价购活动详情 {@link MarkupLevelBySkuResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/markup/get-markup-by-sku")
	BaseResponse<MarkupLevelBySkuResponse> getMarkupListBySku(@RequestBody @Valid MarkupLevelBySkuRequest markupLevelBySkuRequest);

	/**
	 * 已参加换购的商品sku
	 *
	 * @author he
	 * @param marketingIdRequest 已参加换购的商品skuAPI {@link MarkupLevelByIdRequest}
	 * @return 已参加换购的商品sku {@link MarkupSkuIdsResponse}
	 */
	@PostMapping("/marketing/${application.marketing.version}/markup/get-markup-sku-by-marktingId")
	BaseResponse<MarkupSkuIdsResponse> getMarkupSku(@RequestBody @Valid MarketingIdRequest marketingIdRequest);
}

