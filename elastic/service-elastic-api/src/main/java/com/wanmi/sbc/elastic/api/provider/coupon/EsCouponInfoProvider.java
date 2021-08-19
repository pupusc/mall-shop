package com.wanmi.sbc.elastic.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoAddRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoDeleteByIdRequest;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoInitRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsCouponInfoProvider")
public interface EsCouponInfoProvider {

    /**
     * 初始化优惠券ES数据
     *
     * @param request 初始化ES条件 {@link EsCouponInfoInitRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/info/init")
    BaseResponse init(@RequestBody @Valid EsCouponInfoInitRequest request);

    /**
     * 新增优惠券
     *
     * @param request 优惠券新增信息结构 {@link EsCouponInfoAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/info/add")
    BaseResponse add(@RequestBody @Valid EsCouponInfoAddRequest request);

    /**
     * 删除优惠券
     *
     * @param request 优惠券ID {@link EsCouponInfoDeleteByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/info/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid EsCouponInfoDeleteByIdRequest request);
}
