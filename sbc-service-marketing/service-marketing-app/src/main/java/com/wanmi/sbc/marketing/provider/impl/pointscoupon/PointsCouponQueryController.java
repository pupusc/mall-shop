package com.wanmi.sbc.marketing.provider.impl.pointscoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.provider.pointscoupon.PointsCouponQueryProvider;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponByIdRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponPageRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponQueryRequest;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponByIdResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponListResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponPageResponse;
import com.wanmi.sbc.marketing.bean.vo.PointsCouponVO;
import com.wanmi.sbc.marketing.coupon.service.CouponInfoService;
import com.wanmi.sbc.marketing.pointscoupon.model.root.PointsCoupon;
import com.wanmi.sbc.marketing.pointscoupon.service.PointsCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>积分兑换券表查询服务接口实现</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@RestController
@Validated
public class PointsCouponQueryController implements PointsCouponQueryProvider {
    @Autowired
    private PointsCouponService pointsCouponService;

    @Autowired
    private CouponInfoService couponInfoService;

    @Override
    public BaseResponse<PointsCouponPageResponse> page(@RequestBody @Valid PointsCouponPageRequest pointsCouponPageReq) {
        PointsCouponQueryRequest queryReq = new PointsCouponQueryRequest();
        KsBeanUtil.copyPropertiesThird(pointsCouponPageReq, queryReq);
        Page<PointsCoupon> pointsCouponPage = pointsCouponService.page(queryReq);
        Page<PointsCouponVO> newPage = couponInfoService.getPointsCouponInfoPage(pointsCouponPage);
        MicroServicePage<PointsCouponVO> microPage = new MicroServicePage<>(newPage, pointsCouponPageReq.getPageable());
        PointsCouponPageResponse finalRes = new PointsCouponPageResponse(microPage);
        return BaseResponse.success(finalRes);
    }

    @Override
    public BaseResponse<PointsCouponByIdResponse> getById(@RequestBody @Valid PointsCouponByIdRequest pointsCouponByIdRequest) {
        PointsCoupon pointsCoupon = pointsCouponService.getById(pointsCouponByIdRequest.getPointsCouponId());
        return BaseResponse.success(new PointsCouponByIdResponse(pointsCouponService.wrapperVo(pointsCoupon)));
    }

    @Override
    public BaseResponse<PointsCouponListResponse> queryOverdueList() {
        List<PointsCoupon> pointsCoupons = pointsCouponService.queryOverdueList();
        List<PointsCouponVO> pointsCouponVOS = pointsCoupons.stream().map(entity -> pointsCouponService.wrapperVo(entity)).collect(Collectors.toList());
        return BaseResponse.success(new PointsCouponListResponse(pointsCouponVOS));
    }
}

