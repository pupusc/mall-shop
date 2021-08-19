package com.wanmi.sbc.marketing.provider.impl.buyoutprice;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.marketing.api.provider.buyoutprice.MarketingBuyoutPriceQueryProvider;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceIdRequest;
import com.wanmi.sbc.marketing.api.request.buyoutprice.MarketingBuyoutPriceSearchRequest;
import com.wanmi.sbc.marketing.api.response.buyoutprice.MarketingBuyoutPriceMarketingIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingPageResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingPageVO;
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
public class MarketingBuyoutPriceQueryController implements MarketingBuyoutPriceQueryProvider{

    @Autowired
    private MarketingBuyoutPriceService marketingBuyoutPriceService;

    /**
     * 查询一口价活动的详情
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<MarketingBuyoutPriceMarketingIdResponse> details(@RequestBody @Valid MarketingBuyoutPriceIdRequest request) {
        MarketingBuyoutPriceMarketingIdResponse buyoutPriceMarketingId = marketingBuyoutPriceService.details(request);
        return BaseResponse.success(buyoutPriceMarketingId);
    }

    /**
     * 根据字段模糊查询
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse<MarketingPageResponse> search(@Valid MarketingBuyoutPriceSearchRequest request) {
        MicroServicePage<MarketingPageVO> marketingPage = marketingBuyoutPriceService.search(request);
        return BaseResponse.success(MarketingPageResponse.builder().marketingVOS(marketingPage).build());
    }
}
