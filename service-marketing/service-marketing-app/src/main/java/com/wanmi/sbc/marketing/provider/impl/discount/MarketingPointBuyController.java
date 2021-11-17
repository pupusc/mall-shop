package com.wanmi.sbc.marketing.provider.impl.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingPointBuyProvider;
import com.wanmi.sbc.marketing.bean.dto.MarketingPointBuyAddRequest;
import com.wanmi.sbc.marketing.discount.model.request.MarketingPointBuySaveRequest;
import com.wanmi.sbc.marketing.discount.service.MarketingPointBuyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MarketingPointBuyController implements MarketingPointBuyProvider {

    @Autowired
    private MarketingPointBuyService marketingPointBuyService;

    @Override
    public BaseResponse add(@RequestBody MarketingPointBuyAddRequest request) {
        MarketingPointBuySaveRequest marketingPointBuySaveRequest = KsBeanUtil.convert(request, MarketingPointBuySaveRequest.class);
        marketingPointBuyService.addPointBuyDiscount(marketingPointBuySaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse update(MarketingPointBuyAddRequest request) {
        MarketingPointBuySaveRequest marketingPointBuySaveRequest = KsBeanUtil.convert(request, MarketingPointBuySaveRequest.class);
        marketingPointBuyService.updatePointBuyDiscount(marketingPointBuySaveRequest);
        return BaseResponse.SUCCESSFUL();
    }

}
