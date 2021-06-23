package com.wanmi.sbc.marketing.api.provider.gift;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftDetailListByMarketingIdAndLevelIdRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdAndCustomerRequest;
import com.wanmi.sbc.marketing.api.request.gift.FullGiftLevelListByMarketingIdRequest;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftDetailListByMarketingIdAndLevelIdResponse;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftLevelListByMarketingIdAndCustomerResponse;
import com.wanmi.sbc.marketing.api.response.gift.FullGiftLevelListByMarketingIdResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: ZhangLingKe
 * @Description: 满赠优惠服务查询操作接口
 * @Date: 2018-11-15 15:56
 */
@FeignClient(value = "${application.marketing.name}", contextId = "FullGiftQueryProvider")
public interface FullGiftQueryProvider {

    /**
     * 根据marketingid获取满赠等级列表
     * @param request 营销id {@link FullGiftLevelListByMarketingIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/full-gift/list-by-marketing-id")
    BaseResponse<FullGiftLevelListByMarketingIdResponse> listLevelByMarketingId(@RequestBody @Valid FullGiftLevelListByMarketingIdRequest request);

    /**
     * 根据营销获取用户满赠等级列表
     * @param request 营销id与客户信息 {@link FullGiftLevelListByMarketingIdAndCustomerRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/full-gift/list-gift-by-marketing-id-and-customer")
    BaseResponse<FullGiftLevelListByMarketingIdAndCustomerResponse> listGiftByMarketingIdAndCustomer(@RequestBody @Valid FullGiftLevelListByMarketingIdAndCustomerRequest request);

    /**
     * 根据营销和等级获取满赠详情信息列表
     * @param request 营销id与等级id {@link FullGiftDetailListByMarketingIdAndLevelIdRequest}
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/full-gift/list-detail-by-marketing-id-and-level-id")
    BaseResponse<FullGiftDetailListByMarketingIdAndLevelIdResponse> listDetailByMarketingIdAndLevelId(@RequestBody @Valid FullGiftDetailListByMarketingIdAndLevelIdRequest request);

}
