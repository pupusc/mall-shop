package com.wanmi.sbc.customer.api.provider.company;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.company.*;
import com.wanmi.sbc.customer.api.response.company.*;
import com.wanmi.sbc.customer.api.response.store.StoreListForDistributionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 公司信息-公司信息查询API
 * @Author: daiyitian
 * @CreateDate: 2018/9/11 16:25
 * @Version: 1.0
 */
@FeignClient(value = "${application.customer.name}", contextId = "CompanyInfoQueryProvider")
public interface CompanyInfoQueryProvider {


    /**
     * 查询首个公司信息
     *
     * @return 公司信息 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/get-company-info")
    BaseResponse<CompanyInfoGetResponse> getCompanyInfo();

    /**
     * 查询首个公司信息，不存在则新增再返回
     *
     * @return 公司信息 {@link CompanyInfoGetWithAddResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/get-company-info-with-add")
    BaseResponse<CompanyInfoGetWithAddResponse> getCompanyInfoWithAdd();

    /**
     * 根据id查询公司信息
     *
     * @param request 包含id的查询参数 {@link CompanyInfoByIdResponse}
     * @return 公司信息 {@link CompanyInfoGetResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/get-company-info-by-id")
    BaseResponse<CompanyInfoByIdResponse> getCompanyInfoById(@RequestBody @Valid CompanyInfoByIdRequest request);

    /**
     * 公司信息分页列表
     *
     * @param request 条件查询参数 {@link CompanyPageRequest}
     * @return 公司信息分页列表 {@link CompanyInfoPageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/page-company-info")
    BaseResponse<CompanyInfoPageResponse> pageCompanyInfo(@RequestBody CompanyPageRequest request);

    /**
     * 公司信息列表
     *
     * @param request 条件查询参数 {@link CompanyListRequest}
     * @return 公司信息列表 {@link CompanyInfoListResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/list-company-info")
    BaseResponse<CompanyInfoListResponse> listCompanyInfo(@RequestBody CompanyListRequest request);

    /**
     * 公司收款账户分页列表
     *
     * @param request 条件查询参数 {@link CompanyAccountPageRequest}
     * @return 返回分页信息 {@link CompanyAccountPageResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/page-company-account")
    BaseResponse<CompanyAccountPageResponse> pageCompanyAccount(@RequestBody CompanyAccountPageRequest request);

    /**
     * 根据待审核状态统计商户数字
     *
     * @return 返回待审核统计 {@link CompanyWaitCheckCountResponse}
     */
    @PostMapping("/customer/${application.customer.version}/company/count-company-by-wait-check")
    BaseResponse<CompanyWaitCheckCountResponse> countCompanyByWaitCheck();

    @PostMapping("/customer/${application.customer.version}/company/query-by-company-info-ids")
    BaseResponse<CompanyInfoQueryByIdsResponse> queryByCompanyInfoIds(@RequestBody @Valid
                                                                              CompanyInfoQueryByIdsRequest request);

    /**
     * 根据商家编号模糊列表查询
     * @param recordRequest
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/company/query-by-company-info-code")
    BaseResponse<StoreListForDistributionResponse> queryByCompanyCode(@RequestBody @Valid
                                                                               CompanyInfoForDistributionRecordRequest recordRequest);
}
