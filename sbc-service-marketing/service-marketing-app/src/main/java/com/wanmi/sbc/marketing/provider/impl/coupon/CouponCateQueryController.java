package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCateQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateGetByCouponCateIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCateListLimitThreeByCateIdsRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateGetByCouponCateIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateListForSupplierResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateListLimitThreeByCateIdsResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponCateListResponse;
import com.wanmi.sbc.marketing.bean.vo.CouponCateVO;
import com.wanmi.sbc.marketing.coupon.response.CouponCateResponse;
import com.wanmi.sbc.marketing.coupon.service.CouponCateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * <p></p>
 * author: sunkun
 * Date: 2018-11-24
 */
@Validated
@RestController
public class CouponCateQueryController implements CouponCateQueryProvider {

    @Autowired
    private CouponCateService couponCateService;

    /**
     * 查询优惠券分类列表提供给优惠券使用, 最多可以批量查询3个
     *
     * @param request 请求结构 {@link CouponCateListLimitThreeByCateIdsRequest}
     * @return
     */
    @Override
    public BaseResponse<CouponCateListLimitThreeByCateIdsResponse> listLimitThreeByCateIds(@RequestBody @Valid CouponCateListLimitThreeByCateIdsRequest request) {
        List<CouponCateResponse> couponCateResponses = couponCateService.listCouponCateLimitThreeByCouponCateIds(request.getCouponCateIds());
        return BaseResponse.success(new CouponCateListLimitThreeByCateIdsResponse(KsBeanUtil.convertList(couponCateResponses, CouponCateVO.class)));
    }

    /**
     * 查询优惠券分类列表
     *
     * @return
     */
    @Override
    public BaseResponse<CouponCateListResponse> list() {
        List<CouponCateResponse> couponCateResponses = couponCateService.listCouponCate();
        return BaseResponse.success(new CouponCateListResponse(KsBeanUtil.convertList(couponCateResponses, CouponCateVO.class)));
    }

    /**
     * 根据优惠券分类Id查询单个优惠券分类
     *
     * @param request 请求结构 {@link CouponCateGetByCouponCateIdRequest}
     * @return
     */
    @Override
    public BaseResponse<CouponCateGetByCouponCateIdResponse> getByCouponCateId(@RequestBody @Valid CouponCateGetByCouponCateIdRequest request) {
        CouponCateResponse couponCateResponse = couponCateService.getCouponCateByCouponCateId(request.getCouponCateId());
        return BaseResponse.success(KsBeanUtil.convert(couponCateResponse, CouponCateGetByCouponCateIdResponse.class));
    }

    /**
     * 查询优惠券分类列表提供给商家使用
     *
     * @return
     */
    @Override
    public BaseResponse<CouponCateListForSupplierResponse> listForSupplier() {
        List<CouponCateResponse> couponCateResponses = couponCateService.listCouponCateForSupplier();
        return BaseResponse.success(new CouponCateListForSupplierResponse(KsBeanUtil.convertList(couponCateResponses, CouponCateVO.class)));
    }
}
