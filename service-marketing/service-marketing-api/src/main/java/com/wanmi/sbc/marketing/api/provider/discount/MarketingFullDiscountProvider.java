package com.wanmi.sbc.marketing.api.provider.discount;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountAddRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountModifyRequest;
import com.wanmi.sbc.marketing.api.request.discount.MarketingFullDiscountSaveLevelListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>营销满折业务</p>
 * author: sunkun
 * Date: 2018-11-20
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingFullDiscountProvider")
public interface MarketingFullDiscountProvider {

    /**
     * 新增满折
     * @param request 新增满折请求结构 {@link MarketingFullDiscountAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/full/discount/add")
    BaseResponse add(@RequestBody @Valid MarketingFullDiscountAddRequest request);

    /**
     * 修改满折
     * @param request 修改满折请求结构 {@link MarketingFullDiscountModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/full/discount/modify")
    BaseResponse modify(@RequestBody @Valid MarketingFullDiscountModifyRequest request);

    /**
     * 保存多级优惠信息
     * @param request 保存多级优惠信息请求结构 {@link MarketingFullDiscountSaveLevelListRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/full/discount/save-level-list")
    BaseResponse saveLevelList(@RequestBody @Valid MarketingFullDiscountSaveLevelListRequest request);
}
