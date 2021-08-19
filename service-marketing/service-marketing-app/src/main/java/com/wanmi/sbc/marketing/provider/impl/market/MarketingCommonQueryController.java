package com.wanmi.sbc.marketing.provider.impl.market;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.provider.market.MarketingCommonQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.InfoForPurchseRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketInfoForPurchaseResponse;
import com.wanmi.sbc.marketing.common.service.MarketingCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Validated
@RestController
public class MarketingCommonQueryController implements MarketingCommonQueryProvider {

    @Autowired
    private MarketingCommonService marketingCommonService;

    @Override
    public BaseResponse<MarketInfoForPurchaseResponse> queryInfoForPurchase(@RequestBody @Valid InfoForPurchseRequest request) {
        return BaseResponse.success(marketingCommonService.queryInfoForPurchase(request));
    }

}
