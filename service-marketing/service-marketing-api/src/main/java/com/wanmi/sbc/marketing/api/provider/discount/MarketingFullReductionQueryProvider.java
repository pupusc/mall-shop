package com.wanmi.sbc.marketing.api.provider.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.response.reduction.MarketingFullReductionByMarketingIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>营销满折业务</p>
 * author: sunkun
 * Date: 2018-11-20
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingFullReductionQueryProvider")
public interface MarketingFullReductionQueryProvider {

    /**
     * 根据营销编号查询营销等级集合
     * @param marketingFullReductionByMarketingIdRequest  {@link MarketingFullReductionByMarketingIdRequest}
     * @return {@link MarketingFullReductionByMarketingIdResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/full/reduction/list-by-marketing-id")
    BaseResponse<MarketingFullReductionByMarketingIdResponse> listByMarketingId(@RequestBody @Valid MarketingFullReductionByMarketingIdRequest marketingFullReductionByMarketingIdRequest);
}
