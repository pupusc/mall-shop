package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponMarketingScopeQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponMarketingScopeByScopeIdRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponMarketingScopeByScopeIdResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponMarketingScopeVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponMarketingScope;
import com.wanmi.sbc.marketing.coupon.service.CouponMarketingScopeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

/**
 * <p>对优惠券商品作用范围查询接口</p>
 * Created by daiyitian on 2018-11-24-下午6:23.
 */
@Validated
@RestController
public class CouponMarketingScopeQueryController implements CouponMarketingScopeQueryProvider {

    @Autowired
    private CouponMarketingScopeService couponMarketingScopeService;

    /**
     * 根据优惠券范围id查询优惠券商品作用范列表
     *
     * @param request 包含优惠券范围id的查询请求结构 {@link CouponMarketingScopeByScopeIdRequest}
     * @return 优惠券商品作用范列表 {@link CouponMarketingScopeByScopeIdResponse}
     */
    @Override
    public BaseResponse<CouponMarketingScopeByScopeIdResponse> listByScopeId(@RequestBody @Valid
                                                                              CouponMarketingScopeByScopeIdRequest
                                                                              request){
        List<CouponMarketingScope> scopeList = couponMarketingScopeService.listScopeByScopeId(request.getScopeId());
        if(CollectionUtils.isEmpty(scopeList)){
            return BaseResponse.success(CouponMarketingScopeByScopeIdResponse.builder()
                    .scopeVOList(Collections.emptyList()).build());
        }
        return BaseResponse.success(CouponMarketingScopeByScopeIdResponse.builder()
                .scopeVOList(KsBeanUtil.convert(scopeList, CouponMarketingScopeVO.class)).build());
    }

}