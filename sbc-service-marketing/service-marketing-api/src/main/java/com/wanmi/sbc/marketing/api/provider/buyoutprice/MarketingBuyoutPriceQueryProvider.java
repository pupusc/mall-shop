package com.wanmi.sbc.marketing.api.provider.buyoutprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceIdRequest;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceSearchRequest;
import com.wanmi.sbc.marketing.api.response.buyoutprice.MarketingBuyoutPriceMarketingIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>营销一口价业务</p>
 * author: weiwenhao
 * Date: 2020-04-13
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingBuyoutPriceQueryProvider")
public interface MarketingBuyoutPriceQueryProvider {

    /**
     * 查詢一口价活动的详情
     * @param request
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/buyout_price/details")
    BaseResponse<MarketingBuyoutPriceMarketingIdResponse> details(@RequestBody @Valid MarketingBuyoutPriceIdRequest request);

    /**
     * 根据字段模糊搜索
     * @param request
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/buyout_price/search")
    BaseResponse<MarketingPageResponse> search(@RequestBody @Valid MarketingBuyoutPriceSearchRequest request);

}
