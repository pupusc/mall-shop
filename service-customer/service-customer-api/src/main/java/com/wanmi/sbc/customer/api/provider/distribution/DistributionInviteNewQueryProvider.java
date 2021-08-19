package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.*;
import com.wanmi.sbc.customer.api.request.distribution.DistributionCustomerExportRequest;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewExportResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListByInvitedCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListResponse;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewPageResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountByRequestCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountCouponByRequestCustomerIdResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCountInvitedCustResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Created by feitingting on 2019/2/21.
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionInviteNewQueryProvider")
public interface DistributionInviteNewQueryProvider {

    /**
     * 根据条件分页查询邀新记录
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/invite-new-page")
    BaseResponse<DistributionInviteNewPageResponse> findDistributionInviteNewRecord(@RequestBody @Valid
                                                                                            DistributionInviteNewPageRequest request);

    /**
     * 根据条件导出查询邀新记录
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/invite-new-export")
    BaseResponse<DistributionInviteNewExportResponse> exportDistributionInviteNewRecord(@RequestBody @Valid
                                                                                                DistributionInviteNewExportRequest request);

    /**
     * 根据条件查询所有邀新记录
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/invite-new-list")
    BaseResponse<DistributionInviteNewListResponse> listDistributionInviteNewRecord(@RequestBody @Valid
                                                                                            DistributionInviteNewListRequest request);

    /**
     * 统计邀新记录数量
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/count-invited-customers")
    BaseResponse<DistributionCountInvitedCustResponse> countInvitedCustomers(@RequestBody @Valid
                                                                                     DistributionCountInvitedCustRequest request);

    /**
     * 根据受邀人会员ID查询邀新记录
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/list-by-invited-customer-id")
    BaseResponse<DistributionInviteNewListByInvitedCustomerIdResponse> listByInvitedCustomerId(@RequestBody @Valid
                                                                                                       DistributionInviteNewListByInviteCustomerIdRequest request);

    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券）
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/count-coupon-by-request-customer-id")
    BaseResponse<DistributionCountCouponByRequestCustomerIdResponse> countCouponByRequestCustomerId(@RequestBody
                                                                                                    @Valid DistributionCountCouponByRequestCustomerIdRequest request);

    /**
     * 根据邀请人会员ID统计邀新总人数、有效邀新人数、奖励已入账邀新人数（优惠券 & 奖金）
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/count-by-request-customer-id")
    BaseResponse<DistributionCountByRequestCustomerIdResponse> countByRequestCustomerId(@RequestBody @Valid
                                                                                                DistributionCountByRequestCustomerIdRequest request);

    /**
     * 根据条件分页查询邀新记录
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/invite-new-record-page")
    BaseResponse<DistributionInviteNewPageResponse> findInviteNewRecordPage(@RequestBody @Valid
                                                                                    DistributionInviteNewPageRequest request);


    /**
     * 统计邀新记录数量
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/distinct-count-invited-customers")
    BaseResponse<DistributionCountInvitedCustResponse> distinctCountInvitedCustomers(@RequestBody @Valid
                                                                                     DistributionCountInvitedCustRequest request);
}
