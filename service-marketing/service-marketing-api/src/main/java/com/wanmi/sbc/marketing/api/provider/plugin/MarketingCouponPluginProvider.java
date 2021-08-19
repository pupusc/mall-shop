package com.wanmi.sbc.marketing.api.provider.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingCouponWrapperRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingCouponWrapperResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>优惠券插件服务操作接口</p>
 * Created by daiyitian on 2018-11-24-下午6:23.
 */
@FeignClient(value = "${application.marketing.name}", contextId = "MarketingCouponPluginProvider")
public interface MarketingCouponPluginProvider {

    /**
     * 优惠券的订单营销处理
     * @param request 包含营销处理结构 {@link MarketingCouponWrapperRequest}
     * @return 处理结果 {@link MarketingCouponWrapperResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/plugin/coupon/wrapper")
    BaseResponse<MarketingCouponWrapperResponse> wrapper(@RequestBody @Valid MarketingCouponWrapperRequest
                                                                      request);

}