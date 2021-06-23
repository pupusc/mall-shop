package com.wanmi.sbc.marketing.provider.impl.plugin;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.plugin.MarketingCouponPluginProvider;
import com.wanmi.sbc.marketing.api.request.plugin.MarketingCouponWrapperRequest;
import com.wanmi.sbc.marketing.api.response.plugin.MarketingCouponWrapperResponse;
import com.wanmi.sbc.marketing.common.request.TradeMarketingPluginRequest;
import com.wanmi.sbc.marketing.common.response.TradeMarketingResponse;
import com.wanmi.sbc.marketing.plugin.impl.CouponPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>优惠券插件服务操作接口</p>
 * Created by daiyitian on 2018-11-24-下午6:23.
 */
@Validated
@RestController
public class MarketingCouponPluginController implements MarketingCouponPluginProvider {

    @Autowired
    private CouponPlugin couponPlugin;

    /**
     * 优惠券的订单营销处理
     * @param request 包含营销处理结构 {@link MarketingCouponWrapperRequest}
     * @return 处理结果 {@link MarketingCouponWrapperResponse}
     */
    @Override
    public BaseResponse<MarketingCouponWrapperResponse> wrapper(@RequestBody @Valid MarketingCouponWrapperRequest
                                                                 request){
        TradeMarketingResponse response = couponPlugin.wraperMarketingFullInfo(KsBeanUtil.convert(request,
                TradeMarketingPluginRequest.class));
        if (response != null){
            return BaseResponse.success(KsBeanUtil.convert(response, MarketingCouponWrapperResponse.class));
        }
        return BaseResponse.SUCCESSFUL();
    }

}