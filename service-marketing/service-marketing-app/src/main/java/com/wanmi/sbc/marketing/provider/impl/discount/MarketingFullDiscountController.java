package com.wanmi.sbc.marketing.provider.impl.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullDiscountProvider;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountAddRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountModifyRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountSaveLevelListRequest;
import com.wanmi.sbc.marketing.discount.model.request.MarketingFullDiscountSaveRequest;
import com.wanmi.sbc.marketing.discount.service.MarketingFullDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description:
 * @Date: 2018-11-22 10:41
 */
@Validated
@RestController
public class MarketingFullDiscountController implements MarketingFullDiscountProvider {

    @Autowired
    private MarketingFullDiscountService marketingFullDiscountService;

    /**
     * @param request 新增满折请求结构 {@link MarketingFullDiscountAddRequest}
     * @return
     */
    @Override
    public BaseResponse add(@RequestBody @Valid MarketingFullDiscountAddRequest request) {
        marketingFullDiscountService.addMarketingFullDiscount(KsBeanUtil.convert(request, MarketingFullDiscountSaveRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 修改满折请求结构 {@link MarketingFullDiscountModifyRequest}
     * @return
     */
    @Override
    public BaseResponse modify(@RequestBody @Valid MarketingFullDiscountModifyRequest request) {
        marketingFullDiscountService.modifyMarketingFullDiscount(KsBeanUtil.convert(request, MarketingFullDiscountSaveRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 保存多级优惠信息请求结构 {@link MarketingFullDiscountSaveLevelListRequest}
     * @return
     */
    @Override
    public BaseResponse saveLevelList(@RequestBody @Valid MarketingFullDiscountSaveLevelListRequest request) {
        return BaseResponse.SUCCESSFUL();
    }
}
