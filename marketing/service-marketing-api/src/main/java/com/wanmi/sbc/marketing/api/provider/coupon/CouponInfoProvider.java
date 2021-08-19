package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoAddRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoCopyByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoDeleteByIdRequest;
import com.wanmi.sbc.marketing.api.request.coupon.CouponInfoModifyRequest;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoAddResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoCopyByIdResponse;
import com.wanmi.sbc.marketing.api.response.coupon.CouponInfoModifyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponInfoProvider")
public interface CouponInfoProvider {

    /**
     * 新增优惠券
     *
     * @param request 优惠券新增信息结构 {@link CouponInfoAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/info/add")
    BaseResponse<CouponInfoAddResponse>  add(@RequestBody @Valid CouponInfoAddRequest request);

    /**
     * 修改优惠券
     *
     * @param request 优惠券修改信息结构 {@link CouponInfoModifyRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/info/modify")
    BaseResponse<CouponInfoModifyResponse> modify(@RequestBody @Valid CouponInfoModifyRequest request);

    /**
     * 根据id删除优惠券
     *
     * @param request 包含id的删除请求结构 {@link CouponInfoDeleteByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/info/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid CouponInfoDeleteByIdRequest request);

    /**
     * 根据id复制优惠券
     *
     * @param request 包含id的复制请求结构 {@link CouponInfoCopyByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/info/copy-by-id")
    BaseResponse<CouponInfoCopyByIdResponse> copyById(@RequestBody @Valid CouponInfoCopyByIdRequest request);
}
