package com.wanmi.sbc.marketing.api.provider.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountAddRequest;
import com.wanmi.sbc.marketing.bean.dto.MarketingPointBuyAddRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(value = "${application.marketing.name}", contextId = "MarketingPointBuyProvider")
public interface MarketingPointBuyProvider {

    /**
     * 添加积分换购活动
     */
    @PostMapping("/marketing/${application.marketing.version}/point-buy/add")
    BaseResponse add(@RequestBody MarketingPointBuyAddRequest request);

    /**
     * 添加积分换购活动
     */
    @PostMapping("/marketing/${application.marketing.version}/point-buy/update")
    BaseResponse update(@RequestBody MarketingPointBuyAddRequest request);
}
