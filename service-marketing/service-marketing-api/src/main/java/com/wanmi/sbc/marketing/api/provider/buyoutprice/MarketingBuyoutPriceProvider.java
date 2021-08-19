package com.wanmi.sbc.marketing.api.provider.buyoutprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>营销一口价业务</p>
 * author: weiwenhao
 * Date: 2020-04-13
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingBuyoutPriceProvider")
public interface MarketingBuyoutPriceProvider {

    /**
     * 一口价活动
     * @param request 一口价请求结构 {@link MarketingBuyoutPriceAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/buyout_price/add")
    BaseResponse add(@RequestBody @Valid MarketingBuyoutPriceAddRequest request);

    /**
     * 修改一口价
     * @param request 修改一口价请求结构 {@link MarketingBuyoutPriceAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/buyout_price/modify")
    BaseResponse modify(@RequestBody @Valid MarketingBuyoutPriceAddRequest request);

}
