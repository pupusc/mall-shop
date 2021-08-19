package com.wanmi.sbc.account.api.provider.company;

import com.wanmi.sbc.account.api.request.company.CompanyAccountByCompanyInfoIdAndDefaultFlagRequest;
import com.wanmi.sbc.account.api.request.company.CompanyAccountCountByCompanyInfoIdRequest;
import com.wanmi.sbc.account.api.request.company.CompanyAccountFindByAccountIdRequest;
import com.wanmi.sbc.account.api.response.company.CompanyAccountCountResponse;
import com.wanmi.sbc.account.api.response.company.CompanyAccountFindResponse;
import com.wanmi.sbc.account.api.response.company.CompanyAccountListResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对商家收款账户查询接口</p>
 * Created by daiyitian on 2018-10-13-下午6:23.
 */
@FeignClient(value = "${application.account.name}", contextId = "CompanyAccountQueryProvider")
public interface CompanyAccountQueryProvider {

    /**
     * 根据商户编号和默认标识查询商家收款账户列表
     *
     * @param request 查询商家收款账户数据结构 {@link CompanyAccountByCompanyInfoIdAndDefaultFlagRequest}
     * @return {@link CompanyAccountListResponse}
     */
    @PostMapping("/account/${application.account.version}/company/list-by-company-info-id-and-default-flag")
    BaseResponse<CompanyAccountListResponse> listByCompanyInfoIdAndDefaultFlag(@RequestBody @Valid
                                                        CompanyAccountByCompanyInfoIdAndDefaultFlagRequest request);

    /**
     * 统计商家收款账户
     *
     * @param request 统计商家收款账户数据结构 {@link CompanyAccountCountByCompanyInfoIdRequest}
     * @return {@link CompanyAccountCountResponse}
     */
    @PostMapping("/account/${application.account.version}/company/count-by-company-info-id")
    BaseResponse<CompanyAccountCountResponse> countByCompanyInfoId(@RequestBody @Valid
                                                             CompanyAccountCountByCompanyInfoIdRequest request);
    /**
     * 根据accountId查询商家收款账户
     *
     * @param request 账户id {@link CompanyAccountFindByAccountIdRequest}
     * @return {@link CompanyAccountFindResponse}
     */
    @PostMapping("/account/${application.account.version}/company/get-by-account-id")
    BaseResponse<CompanyAccountFindResponse> getByAccountId(@RequestBody @Valid
                                                                    CompanyAccountFindByAccountIdRequest request);
}
