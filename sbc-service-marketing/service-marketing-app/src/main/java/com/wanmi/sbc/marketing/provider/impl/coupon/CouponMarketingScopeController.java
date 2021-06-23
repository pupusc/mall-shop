package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponMarketingScopeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeBatchAddRequest;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.coupon.service.CouponMarketingScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>对优惠券商品作用范围操作接口</p>
 * Created by daiyitian on 2018-11-24-下午6:23.
 */
@Validated
@RestController
public class CouponMarketingScopeController implements CouponMarketingScopeProvider {

    @Autowired
    private CouponMarketingScopeService couponMarketingScopeService;

    /**
     * 批量新增优惠券商品作用范围
     *
     * @param request 批量新增优惠券商品作用范围请求结构 {@link CouponMarketingScopeBatchAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @Override
    public BaseResponse batchAdd(@RequestBody @Valid CouponMarketingScopeBatchAddRequest request){
        couponMarketingScopeService.addBatchCouponMarketingScope(KsBeanUtil.convert(request.getScopeDTOList(),
                CouponMarketingScope.class));
        return BaseResponse.SUCCESSFUL();
    }

}