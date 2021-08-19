package com.wanmi.sbc.marketing.api.provider.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.response.discount.MarketingFullDiscountByMarketingIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>营销满折业务</p>
 * author: sunkun
 * Date: 2018-11-20
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingFullDiscountQueryProvider")
public interface MarketingFullDiscountQueryProvider {

    /**
     * 根据营销编号查询营销等级集合
     * @param marketingFullDiscountByMarketingIdRequest  {@link MarketingFullDiscountByMarketingIdRequest}
     * @return {@link MarketingFullDiscountByMarketingIdResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/full/discount/list-by-marketing-id")
    BaseResponse<MarketingFullDiscountByMarketingIdResponse> listByMarketingId(@RequestBody @Valid MarketingFullDiscountByMarketingIdRequest marketingFullDiscountByMarketingIdRequest);
}
