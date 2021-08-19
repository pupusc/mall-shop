package com.wanmi.sbc.marketing.provider.impl.buyoutprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.buyoutprice.MarketingBuyoutPriceProvider;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceAddRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionAddRequest;
import com.wanmi.sbc.marketing.buyoutprice.model.request.MarketingBuyoutPriceSaveRequest;
import com.wanmi.sbc.marketing.buyoutprice.service.MarketingBuyoutPriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: weiwnehao
 * @Description:
 * @Date: 2020-04-13
 */
@Validated
@RestController
public class MarketingBuyoutPriceController implements MarketingBuyoutPriceProvider {

    @Autowired
    private MarketingBuyoutPriceService marketingBuyoutPriceService;

    /**
     * @param request 一口价请求结构 {@link MarketingFullReductionAddRequest}
     * @return
     */
    @Override
    public BaseResponse add(@RequestBody @Valid MarketingBuyoutPriceAddRequest request) {
        marketingBuyoutPriceService.addMarketingBuyoutPrice(KsBeanUtil.convert(request,
                MarketingBuyoutPriceSaveRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 修改一口价请求结构 {@link MarketingBuyoutPriceAddRequest}
     * @return
     */
    @Override
    public BaseResponse modify(@RequestBody @Valid MarketingBuyoutPriceAddRequest request) {
        marketingBuyoutPriceService.modifyMarketingBuyoutPrice(KsBeanUtil.convert(request,
                MarketingBuyoutPriceSaveRequest.class));
        return BaseResponse.SUCCESSFUL();
    }
}
