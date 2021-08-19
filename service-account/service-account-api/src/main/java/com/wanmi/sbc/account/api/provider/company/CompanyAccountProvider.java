package com.wanmi.sbc.account.api.provider.company;

import com.wanmi.sbc.account.api.request.company.*;
import com.wanmi.sbc.account.api.response.company.CompanyAccountAddResponse;
import com.wanmi.sbc.account.api.response.company.CompanyAccountModifyResponse;
import com.wanmi.sbc.account.api.response.storeInformation.StoreAuditStateResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.BoolFlag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>对商家收款账户操作接口</p>
 * Created by of628-wenzhi on 2018-10-13-下午6:23.
 */
@FeignClient(value = "${application.account.name}", contextId = "CompanyAccountProvider")
public interface CompanyAccountProvider {

    /**
     * 新增商家收款账户
     *
     * @param request 新增商家收款账户数据结构 {@link CompanyAccountAddRequest}
     * @return {@link CompanyAccountAddResponse}
     */
    @PostMapping("/account/${application.account.version}/company/add")
    BaseResponse<CompanyAccountAddResponse> add(@RequestBody @Valid CompanyAccountAddRequest request);

    /**
     * 删除商家收款账户
     *
     * @param request 删除商家收款账户数据结构 {@link CompanyAccountDeleteByIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/delete")
    BaseResponse delete(@RequestBody @Valid CompanyAccountDeleteByIdRequest request);

    /**
     * 修改商家收款账户
     *
     * @param request 修改商家收款账户数据结构 {@link CompanyAccountModifyRequest}
     * @return {@link CompanyAccountModifyResponse}
     */
    @PostMapping("/account/${application.account.version}/company/modify")
    BaseResponse<CompanyAccountModifyResponse> modify(@RequestBody @Valid CompanyAccountModifyRequest request);

    /**
     * 设为主账号
     *
     * @param request 设为主账号数据结构 {@link CompanyAccountModifyPrimaryRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/modify-primary")
    BaseResponse modifyPrimary(@RequestBody @Valid CompanyAccountModifyPrimaryRequest request);

    /**
     * 批量修改商家收款账户
     *
     * @param request 批量修改商家收款账户数据结构 {@link CompanyAccountBatchRenewalRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/batch-renewal")
    BaseResponse batchRenewal(@RequestBody @Valid CompanyAccountBatchRenewalRequest request);

    /**
     * 批量修改商家收款账户
     *
     * @param request 批量修改商家收款账户数据结构 {@link CompanyAccountBatchRenewalRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/batch-renewal-return-state")
    BaseResponse<StoreAuditStateResponse> batchRenewalReturnState(@RequestBody @Valid CompanyAccountBatchRenewalRequest request);


    /**
     * 打款
     *
     * @param request 打款数据结构 {@link CompanyAccountRemitRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/remit")
    BaseResponse remit(@RequestBody @Valid CompanyAccountRemitRequest request);

    /**
     * 确认打款
     *
     * @param request 确认打款数据结构 {@link CompanyAccountAffirmRemitRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/affirm-remit")
    BaseResponse affirmRemit(@RequestBody @Valid CompanyAccountAffirmRemitRequest request);



    /**
     * 确认打款 返回确认打款状态 用于拿到返回结果刷入es
     *
     * @param request 确认打款数据结构 {@link CompanyAccountAffirmRemitRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/company/affirm-remit-return")
    BaseResponse<BoolFlag> affirmRemitAndReturn(@RequestBody @Valid CompanyAccountAffirmRemitRequest request);
}
