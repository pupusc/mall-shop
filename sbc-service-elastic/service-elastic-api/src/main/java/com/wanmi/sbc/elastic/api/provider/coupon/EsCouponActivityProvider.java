package com.wanmi.sbc.elastic.api.provider.coupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.elastic.api.request.coupon.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对优惠券操作接口</p>
 * Created by daiyitian on 2018-11-23-下午6:23.
 */
@FeignClient(value = "${application.elastic.name}", contextId = "EsCouponActivityProvider")
public interface EsCouponActivityProvider {

    /**
     * 初始化优惠券活动ES数据
     *
     * @param request 初始化ES条件 {@link EsCouponActivityInitRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/activity/init")
    BaseResponse init(@RequestBody @Valid EsCouponActivityInitRequest request);

    /**
     * 新增优惠券活动
     *
     * @param request 优惠券活动新增信息结构 {@link EsCouponActivityAddRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/activity/add")
    BaseResponse add(@RequestBody @Valid EsCouponActivityAddRequest request);

    /**
     * 删除优惠券活动
     *
     * @param request 优惠券ID {@link EsCouponActivityDeleteByIdRequest}
     * @return 操作结果 {@link BaseResponse}
     */
    @PostMapping("/elastic/${application.elastic.version}/coupon/activity/delete-by-id")
    BaseResponse deleteById(@RequestBody @Valid EsCouponActivityDeleteByIdRequest request);

    /**
     * 开始活动
     * @param request 开始活动请求结构 {@link EsCouponActivityStartByIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/activity/start")
    BaseResponse start(@RequestBody @Valid EsCouponActivityStartByIdRequest request);

    /**
     * 暂停活动
     * @param request 暂停活动请求结构 {@link EsCouponActivityPauseByIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/marketing/${application.marketing.version}/coupon/activity/pause")
    BaseResponse pause(@RequestBody @Valid EsCouponActivityPauseByIdRequest request);

}
