package com.wanmi.sbc.marketing.provider.impl.reduction;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullReductionQueryProvider;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.response.reduction.MarketingFullReductionByMarketingIdResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullReductionLevelVO;
import com.wanmi.sbc.marketing.reduction.model.entity.MarketingFullReductionLevel;
import com.wanmi.sbc.marketing.reduction.service.MarketingFullReductionService;
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
 * @Date: 2018-11-22 10:41
 */
@Validated
@RestController
public class MarketingFullReductionQueryController implements MarketingFullReductionQueryProvider{

    @Autowired
    private MarketingFullReductionService marketingFullReductionService;


    @Override
    public BaseResponse<MarketingFullReductionByMarketingIdResponse> listByMarketingId(@RequestBody @Valid MarketingFullReductionByMarketingIdRequest marketingFullReductionByMarketingIdRequest) {
        List<MarketingFullReductionLevel> marketingFullDiscountLevelList =  marketingFullReductionService.findByMarketingId(marketingFullReductionByMarketingIdRequest.getMarketingId());
        if (CollectionUtils.isEmpty(marketingFullDiscountLevelList)){
            return BaseResponse.success(new MarketingFullReductionByMarketingIdResponse(Collections.EMPTY_LIST));
        }
        List<MarketingFullReductionLevelVO> marketingFullDiscountLevelVOList = KsBeanUtil.convert(marketingFullDiscountLevelList, MarketingFullReductionLevelVO.class);
        return BaseResponse.success(new MarketingFullReductionByMarketingIdResponse(marketingFullDiscountLevelVOList));
    }
}
