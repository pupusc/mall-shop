package com.wanmi.sbc.marketing.provider.impl.market;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.market.MarketingScopeQueryProvider;
import com.wanmi.sbc.marketing.api.request.market.MarketingScopeByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.request.market.MarketingScopeListInvalidMarketingRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingScopeByMarketingIdResponse;
import com.wanmi.sbc.marketing.api.response.market.MarketingScopeListInvalidMarketingResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingScopeVO;
import com.wanmi.sbc.marketing.bean.vo.MarketingVO;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.model.root.MarketingScope;
import com.wanmi.sbc.marketing.common.service.MarketingScopeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-21 14:27
 */
@Validated
@RestController
public class MarketingScopeQueryController implements MarketingScopeQueryProvider {

    @Autowired
    private MarketingScopeService marketingScopeService;

    @Override
    public BaseResponse<MarketingScopeByMarketingIdResponse> listByMarketingId(@RequestBody @Valid MarketingScopeByMarketingIdRequest marketingScopeByMarketingIdRequest) {
        List<MarketingScope> marketingFullDiscountLevelList =  marketingScopeService.findByMarketingId(marketingScopeByMarketingIdRequest.getMarketingId());
        if (CollectionUtils.isEmpty(marketingFullDiscountLevelList)){
            return BaseResponse.success(new MarketingScopeByMarketingIdResponse(Collections.EMPTY_LIST));
        }
        List<MarketingScopeVO> marketingFullDiscountLevelVOList = KsBeanUtil.convert(marketingFullDiscountLevelList, MarketingScopeVO.class);
        return BaseResponse.success(new MarketingScopeByMarketingIdResponse(marketingFullDiscountLevelVOList));
    }

    @Override
    public BaseResponse<MarketingScopeListInvalidMarketingResponse> listInvalidMarketing(@RequestBody @Valid MarketingScopeListInvalidMarketingRequest request) {
        List<Marketing> marketingList = marketingScopeService.listInvalidMarketing(request);
        List<MarketingVO> marketingVOList = KsBeanUtil.convert(marketingList, MarketingVO.class);
        return BaseResponse.success(new MarketingScopeListInvalidMarketingResponse(marketingVOList));
    }
}
