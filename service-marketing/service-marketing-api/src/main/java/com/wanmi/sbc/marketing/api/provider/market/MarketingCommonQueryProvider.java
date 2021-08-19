package com.wanmi.sbc.marketing.api.provider.market;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.market.InfoForPurchseRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketInfoForPurchaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-23
 */
@FeignClient(value = "${application.marketing.name}", contextId = "PurchaseCacheProvider")
public interface MarketingCommonQueryProvider {

    /**
     * 查询购物车相关营销信息
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/activity/query-info-for-purchase")
    BaseResponse<MarketInfoForPurchaseResponse> queryInfoForPurchase(@RequestBody @Valid InfoForPurchseRequest request);

}
