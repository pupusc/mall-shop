package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.distribution.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>分销员查询服务Provider</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCustomerQueryProvider")
public interface DistributionCustomerQueryProvider {

    /**
     * 分页查询分销员API
     *
     * @param distributionCustomerPageReq 分页请求参数和筛选对象 {@link DistributionCustomerPageRequest}
     * @return 分销员分页列表信息 {@link DistributionCustomerPageResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/page")
    BaseResponse<DistributionCustomerPageResponse> page(@RequestBody @Valid DistributionCustomerPageRequest distributionCustomerPageReq);


    /**
     * 分页查询分销员API,显示手机号
     *
     * @param distributionCustomerPageReq 分页请求参数和筛选对象 {@link DistributionCustomerPageRequest}
     * @return 分销员分页列表信息 {@link DistributionCustomerPageResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/page-show-phone")
    BaseResponse<EsDistributionCustomerPageResponse> pageShowPhone(@RequestBody @Valid DistributionCustomerPageRequest distributionCustomerPageReq);


    /**
     * 导出查询分销员API
     *
     * @param distributionCustomerExportReq 导出请求参数和筛选对象 {@link DistributionCustomerExportRequest}
     * @return 分销员导出信息 {@link DistributionCustomerExportResponse}
     * @author of2975
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/export")
    BaseResponse<DistributionCustomerExportResponse> export(@RequestBody @Valid DistributionCustomerExportRequest distributionCustomerExportReq);

    /**
     * 列表查询分销员API
     *
     * @param distributionCustomerListReq 列表请求参数和筛选对象 {@link DistributionCustomerListRequest}
     * @return 分销员的列表信息 {@link DistributionCustomerListResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/list")
    BaseResponse<DistributionCustomerListResponse> list(@RequestBody @Valid DistributionCustomerListRequest distributionCustomerListReq);


    /**
     * 查询下单人的佣金受益人列表
     * @param request 请求对象
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/list-for-order-commit")
    BaseResponse<DistributionCustomerListForOrderCommitResponse> listDistributorsForOrderCommit(
            @RequestBody @Valid DistributionCustomerListForOrderCommitRequest request);


    /**
     * 单个查询分销员API
     *
     * @param distributionCustomerByIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-by-id")
    BaseResponse<DistributionCustomerByIdResponse> getById(@RequestBody @Valid DistributionCustomerByIdRequest distributionCustomerByIdRequest);


    /**
     * 单个查询分销员API
     *
     * @param distributionCustomerByIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByIdRequest}
     * @return 分销员详情 {@link EsDistributionCustomerByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-by-id-show-phone")
    BaseResponse<EsDistributionCustomerByIdResponse> getByIdShowPhone(@RequestBody @Valid DistributionCustomerByIdRequest distributionCustomerByIdRequest);

    /**
     * 单个查询分销员API
     *
     * @param distributionCustomerSimByIdRequest 单个查询分销员请求参数 {@link DistributionCustomerSimByIdRequest}
     * @return 分销员详情 {@link DistributionCustomerSimByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-sim-by-id")
    BaseResponse<DistributionCustomerSimByIdResponse> getSimInfoById(@RequestBody @Valid DistributionCustomerSimByIdRequest
                                                                             distributionCustomerSimByIdRequest);

    /**
     * 根据会员编号查询分销员API（未删除）
     *
     * @param distributionCustomerByCustomerIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByCustomerIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-by-customer-id")
    BaseResponse<DistributionCustomerByCustomerIdResponse> getByCustomerId(@RequestBody @Valid DistributionCustomerByCustomerIdRequest distributionCustomerByCustomerIdRequest);


    /**
     * 根据会员编号查询分销员API（未删除）
     *
     * @param distributionCustomerByCustomerIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByCustomerIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-by-customer-id-show-phone")
    BaseResponse<EsDistributionCustomerByCustomerIdResponse> getByCustomerIdShowPhone(@RequestBody @Valid DistributionCustomerByCustomerIdRequest distributionCustomerByCustomerIdRequest);

    /**
     * 根据被邀人编号查询分销员API（未删除）--已作废
     *
     * @param distributionCustomerByInviteCustomerIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByCustomerIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-by-invite-customer")
    BaseResponse<DistributionCustomerByCustomerIdResponse> getByInviteCustomer(@RequestBody @Valid DistributionCustomerByInviteCustomerIdRequest distributionCustomerByInviteCustomerIdRequest);

    /**
     * 根据会员编号查询分销员API（未删除）
     *
     * @param distributionCustomerByCustomerIdRequest 单个查询分销员请求参数 {@link DistributionCustomerByCustomerIdRequest}
     * @return 分销员详情 {@link DistributionCustomerByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/getDistribute-by-customer-id")
    BaseResponse<DistributionCustomerByCustomerIdResponse> getByCustomerIdAndDistributorFlagAndDelFlag(@RequestBody @Valid DistributionCustomerByCustomerIdRequest distributionCustomerByCustomerIdRequest);


    /**
     * 根据编号查询分销员分销状态
     *
     * @param request 单个查询分销员请求参数 {@link DistributionCustomerEnableByIdRequest}
     * @return 分销状态 {@link DistributionCustomerEnableByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/check-enable-by-distribution-id")
    BaseResponse<DistributionCustomerEnableByIdResponse> checkEnableByDistributionId(@RequestBody @Valid DistributionCustomerEnableByIdRequest request);

    /**
     * 根据会员编号查询分销员分销状态
     *
     * @param request 单个查询分销员请求参数 {@link DistributionCustomerEnableByIdRequest}
     * @return 分销状态 {@link DistributionCustomerEnableByIdResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/check-enable-by-customer-id")
    BaseResponse<DistributionCustomerEnableByCustomerIdResponse> checkEnableByCustomerId(@RequestBody @Valid DistributionCustomerEnableByCustomerIdRequest request);


    /**
     * 根据会员的Id查询邀请人的信息
     *
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/find-invitor-by-customer-id")
    BaseResponse<DistributionCustomerSimByIdResponse> findDistributionCutomerByCustomerId(@RequestBody @Valid DistributionInviteNewByCustomerIdRequest request);

    /**
     * 根据邀请码查询分销员信息
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/get-by-invite-code")
    BaseResponse<DistributionCustomerByInviteCodeResponse> getByInviteCode(@RequestBody @Valid DistributionCustomerByInviteCodeRequest request);

    /**
     * 是否分销员
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution-customer/check-is-distributor")
    BaseResponse<DistributionCustomerCheckResponse> checkIsDistributor(@RequestBody @Valid DistributionCustomerCheckRequest request);

}

