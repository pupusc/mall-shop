package com.wanmi.sbc.marketing.provider.impl.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullDiscountQueryProvider;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.response.discount.MarketingFullDiscountByMarketingIdResponse;
import com.wanmi.sbc.marketing.bean.vo.MarketingFullDiscountLevelVO;
import com.wanmi.sbc.marketing.discount.model.entity.MarketingFullDiscountLevel;
import com.wanmi.sbc.marketing.discount.service.MarketingFullDiscountService;
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
public class MarketingFullDiscountQueryController implements MarketingFullDiscountQueryProvider{

    @Autowired
    private MarketingFullDiscountService marketingFullDiscountService;

    @Override
    public BaseResponse<MarketingFullDiscountByMarketingIdResponse> listByMarketingId(@RequestBody @Valid MarketingFullDiscountByMarketingIdRequest marketingFullDiscountByMarketingIdRequest){
        List<MarketingFullDiscountLevel> marketingFullDiscountLevelList =  marketingFullDiscountService.findByMarketingId(marketingFullDiscountByMarketingIdRequest.getMarketingId());
        if (CollectionUtils.isEmpty(marketingFullDiscountLevelList)){
            return BaseResponse.success(new MarketingFullDiscountByMarketingIdResponse(Collections.EMPTY_LIST));
        }
        List<MarketingFullDiscountLevelVO> marketingFullDiscountLevelVOList = KsBeanUtil.convert(marketingFullDiscountLevelList, MarketingFullDiscountLevelVO.class);
        return BaseResponse.success(new MarketingFullDiscountByMarketingIdResponse(marketingFullDiscountLevelVOList));
    }
}
