package com.wanmi.sbc.elastic.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.coupon.EsCouponInfoPageRequest;
import com.wanmi.sbc.elastic.api.response.coupon.EsCouponInfoPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券查询接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsCouponInfoQueryProvider")
public interface EsCouponInfoQueryProvider {

    /**
     * 根据条件分页查询优惠券分页列表
     *
     * @param request 条件分页查询请求结构 {@link EsCouponInfoPageRequest}
     * @return 优惠券分页列表 {@link EsCouponInfoPageResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/info/page")
    BaseResponse<EsCouponInfoPageResponse> page(@RequestBody @Valid EsCouponInfoPageRequest request);

}
