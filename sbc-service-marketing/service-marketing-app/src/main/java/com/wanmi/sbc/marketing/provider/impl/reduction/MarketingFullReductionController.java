package com.wanmi.sbc.marketing.provider.impl.reduction;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.discount.MarketingFullReductionProvider;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionAddRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionModifyRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullReductionSaveLevelListRequest;
import com.wanmi.sbc.marketing.reduction.model.request.MarketingFullReductionSaveRequest;
import com.wanmi.sbc.marketing.reduction.service.MarketingFullReductionService;
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
public class MarketingFullReductionController implements MarketingFullReductionProvider {

    @Autowired
    private MarketingFullReductionService marketingFullReductionService;


    /**
     * @param request 新增满减请求结构 {@link MarketingFullReductionAddRequest}
     * @return
     */
    @Override
    public BaseResponse add(@RequestBody @Valid MarketingFullReductionAddRequest request) {
        marketingFullReductionService.addMarketingFullReduction(KsBeanUtil.convert(request, MarketingFullReductionSaveRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 修改满减请求结构 {@link MarketingFullReductionModifyRequest}
     * @return
     */
    @Override
    public BaseResponse modify(@RequestBody @Valid MarketingFullReductionModifyRequest request) {
        marketingFullReductionService.modifyMarketingFullReduction(KsBeanUtil.convert(request, MarketingFullReductionSaveRequest.class));
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * @param request 保存多级优惠信息请求结构 {@link MarketingFullReductionSaveLevelListRequest}
     * @return
     */
    @Override
    public BaseResponse saveLevelList(@RequestBody @Valid MarketingFullReductionSaveLevelListRequest request) {
        return BaseResponse.SUCCESSFUL();
    }
}
