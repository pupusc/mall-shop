package com.wanmi.sbc.marketing.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.coupon.*;
import com.wanmi.sbc.marketing.api.response.coupon.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券码查询接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@FeignClient(value = "${application.marketing.name}", contextId = "CouponCodeQueryProvider")
public interface CouponCodeQueryProvider {

    /**
     * 根据客户id查询使用优惠券列表
     *
     * @param request 包含客户id查询请求结构 {@link CouponCodeListForUseByCustomerIdRequest}
     * @return 优惠券列表 {@link CouponCodeListForUseByCustomerIdResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/list-for-use-by-customer-id")
    BaseResponse<CouponCodeListForUseByCustomerIdResponse> listForUseByCustomerId(@RequestBody @Valid
                                                                         CouponCodeListForUseByCustomerIdRequest
                                                                         request);

    /**
     * 分页查询优惠券列表
     *
     * @param request 分页查询优惠券列表请求结构 {@link CouponCodePageRequest}
     * @return 优惠券分页列表 {@link CouponCodePageResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/page")
    BaseResponse<CouponCodePageResponse> page(@RequestBody @Valid CouponCodePageRequest request);

    /**
     * 分页查询优惠券列表
     *
     * @param request 分页查询优惠券列表请求结构 {@link CouponCodeSimplePageRequest}
     * @return 优惠券分页列表 {@link CouponCodeSimplePageResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/simplePage")
    BaseResponse<CouponCodeSimplePageResponse> simplePage(@RequestBody @Valid CouponCodeSimplePageRequest request);

    /**
     * 根据客户和券码id查询不可用的平台券以及优惠券实际优惠总额的请求结构
     *
     * @param request 包含客户和券码id的查询请求结构 {@link CouponCheckoutRequest}
     * @return 操作结果 {@link CouponCheckoutResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/check-out")
    BaseResponse<CouponCheckoutResponse> checkout(@RequestBody @Valid CouponCheckoutRequest request);

    /**
     *根据条件查询优惠券码列表
     * @param request
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/list-coupon-code-by-condition")
    BaseResponse<CouponCodeListByConditionResponse> listCouponCodeByCondition(@RequestBody @Valid CouponCodeQueryRequest request);

    /**
     *根据条件查询优惠券码列表
     * @param request
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/list-not-use-status")
    BaseResponse<CouponCodeListNotUseResponse> listNotUseStatus(@RequestBody @Valid CouponCodeQueryNotUseRequest request);

    /**
     * 验证优惠券（订单提交时）
     * @param request
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/valid-order-commit")
    BaseResponse<CouponCodeValidOrderCommitResponse> validOrderCommit(@RequestBody @Valid CouponCodeValidOrderCommitRequest request);

    /**
     *根据条件分頁查询优惠券码列表
     * @param request
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/code/page-coupon-code-by-condition")
    BaseResponse<CouponCodePageByConditionResponse> pageCouponCodeByCondition(@RequestBody @Valid CouponCodeQueryRequest request);
}
