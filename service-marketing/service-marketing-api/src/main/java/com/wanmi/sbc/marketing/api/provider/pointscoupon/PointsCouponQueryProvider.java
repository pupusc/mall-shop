package com.wanmi.sbc.marketing.api.provider.pointscoupon;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponByIdRequest;
import com.wanmi.sbc.marketing.api.request.pointscoupon.PointsCouponPageRequest;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponByIdResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponListResponse;
import com.wanmi.sbc.marketing.api.response.pointscoupon.PointsCouponPageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>积分兑换券表查询服务Provider</p>
 *
 * @author yang
 * @date 2019-06-11 10:07:09
 */
@FeignClient(value = "${application.marketing.name}", contextId = "PointsCouponQueryProvider")
public interface PointsCouponQueryProvider {

    /**
     * 分页查询积分兑换券表API
     *
     * @param pointsCouponPageReq 分页请求参数和筛选对象 {@link PointsCouponPageRequest}
     * @return 积分兑换券表分页列表信息 {@link PointsCouponPageResponse}
     * @author yang
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/page")
    BaseResponse<PointsCouponPageResponse> page(@RequestBody @Valid PointsCouponPageRequest pointsCouponPageReq);

    /**
     * 单个查询积分兑换券表API
     *
     * @param pointsCouponByIdRequest 单个查询积分兑换券表请求参数 {@link PointsCouponByIdRequest}
     * @return 积分兑换券表详情 {@link PointsCouponByIdResponse}
     * @author yang
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/get-by-id")
    BaseResponse<PointsCouponByIdResponse> getById(@RequestBody @Valid PointsCouponByIdRequest pointsCouponByIdRequest);

    /**
     * 查询过期积分兑换券
     *
     * @return
     */
    @PostMapping("/marketing/${application.marketing.version}/pointscoupon/query_overdue_list")
    BaseResponse<PointsCouponListResponse> queryOverdueList();
}

