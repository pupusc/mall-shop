package com.wanmi.sbc.marketing.provider.impl.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponInfoQueryProvider;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.*;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import com.wanmi.sbc.marketing.coupon.model.root.CouponInfo;
import com.wanmi.sbc.marketing.coupon.response.CouponInfoResponse;
import com.wanmi.sbc.marketing.coupon.service.CouponInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

/**
 * <p>对优惠券查询接口</p>
 * Created by daiyitian on 2018-11-5-下午6:23.
 */
@Validated
@RestController
public class CouponInfoQueryController implements CouponInfoQueryProvider {

    @Autowired
    private CouponInfoService couponInfoService;

    /**
     * 根据条件分页查询条件分页查询
     *
     * @param request 条件分页查询请求结构 {@link CouponInfoPageRequest}
     * @return 优惠券分页列表 {@link CouponInfoPageResponse}
     */
    @Override
    public BaseResponse<CouponInfoPageResponse> page(@RequestBody @Valid CouponInfoPageRequest request){
        CouponInfoQueryRequest queryRequest = new CouponInfoQueryRequest();
        KsBeanUtil.copyPropertiesThird(request, queryRequest);
        return BaseResponse.success(couponInfoService.getCouponInfoPage(queryRequest));
    }

    /**
     * 根据id查询优惠券信息
     *
     * @param request 包含id的查询请求结构 {@link CouponInfoByIdRequest}
     * @return 优惠券信息 {@link CouponInfoByIdResponse}
     */
    @Override
    public BaseResponse<CouponInfoByIdResponse> getById(@RequestBody @Valid CouponInfoByIdRequest request){
        CouponInfoVO vo = couponInfoService.queryCouponInfo(request.getCouponId());
        if(Objects.isNull(vo)){
            return BaseResponse.SUCCESSFUL();
        }
        CouponInfoByIdResponse response = new CouponInfoByIdResponse();
        KsBeanUtil.copyPropertiesThird(vo, response);
        return BaseResponse.success(response);
    }

    /**
     * 根据id查询优惠券详情信息
     *
     * @param request 包含id的查询详情请求结构 {@link CouponInfoDetailByIdRequest}
     * @return 优惠券详情信息 {@link CouponInfoDetailByIdResponse}
     */
    @Override
    public BaseResponse<CouponInfoDetailByIdResponse> getDetailById(@RequestBody @Valid CouponInfoDetailByIdRequest
                                                                                request){
        CouponInfoResponse couponInfoResponse = couponInfoService.queryCouponInfoDetail(request.getCouponId());
        CouponInfoDetailByIdResponse response = KsBeanUtil.convert(
                couponInfoResponse, CouponInfoDetailByIdResponse.class);
        if (Objects.nonNull(couponInfoResponse.getGoodsList())) {
            response.getGoodsList().setGoodsInfoPage(couponInfoResponse.getGoodsList().getGoodsInfoPage());
        }
        return BaseResponse.success(response);
    }

    /**
     * 条件查询优惠券列表
     * @param request
     * @return
     */
    @Override
    public BaseResponse<CouponInfosQueryResponse> queryCouponInfos(CouponInfoQueryRequest request) {
        List<CouponInfo> couponInfoList =  couponInfoService.queryCouponInfos(request);
        CouponInfosQueryResponse response = new CouponInfosQueryResponse();
        response.setCouponCodeList(KsBeanUtil.copyListProperties(couponInfoList, CouponInfoVO.class));
        return BaseResponse.success(response);
    }


    /**
     * 根据条件分页查询条件分页查询
     *
     * @param request 条件分页查询请求结构 {@link CouponInfoListByPageRequest}
     * @return 优惠券分页列表 {@link CouponInfoPageResponse}
     */
    @Override
    public BaseResponse<CouponInfoListByPageResponse> listByPage(@RequestBody @Valid CouponInfoListByPageRequest request){
        return BaseResponse.success(new CouponInfoListByPageResponse(couponInfoService.page(request)));
    }
}
