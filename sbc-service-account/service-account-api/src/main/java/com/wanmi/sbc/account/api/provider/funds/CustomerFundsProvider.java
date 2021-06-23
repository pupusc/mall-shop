package com.wanmi.sbc.account.api.provider.funds;

import com.wanmi.sbc.account.api.request.funds.*;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * 会员资金新增、更新接口
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 14:33
 * @version: 1.0
 */
@FeignClient(value = "${application.account.name}", contextId = "CustomerFundsProvider")
public interface CustomerFundsProvider {

    /**
     * 新增会员资金
     *
     * @param request 新增会员资金数据集 {@link CustomerFundsAddRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/add")
    BaseResponse add(@RequestBody @Valid CustomerFundsAddRequest request);

    /**
     * 根据会员ID更新会员账号
     *
     * @param request 包含会员ID、会员账号 {@link CustomerFundsModifyCustomerAccountByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/modify-customer-account-by-id")
    BaseResponse modifyCustomerAccountByCustomerId(@RequestBody @Valid CustomerFundsModifyCustomerAccountByCustomerIdRequest request);

    /**
     * 根据会员ID更新已提现金额
     *
     * @param request 包含会员ID、已提现金额 {@link CustomerFundsModifyAlreadyDrawCashAmountByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/modify-already-draw-cash-amount-by-id")
    BaseResponse modifyAlreadyDrawCashAmountByCustomerId(@RequestBody @Valid CustomerFundsModifyAlreadyDrawCashAmountByCustomerIdRequest request);

    /**
     * 根据会员ID更新账户余额
     *
     * @param request 包含会员ID、账户余额 {@link CustomerFundsModifyAccountBalanceByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/modify-account-balance-by-id")
    BaseResponse modifyAccountBalanceByCustomerId(@RequestBody @Valid CustomerFundsModifyAccountBalanceByCustomerIdRequest request);

    /**
     * 根据会员ID更新冻结金额
     *
     * @param request 包含会员ID、冻结金额 {@link CustomerFundsModifyBlockedBalanceByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/modify-block-balance-by-id")
    BaseResponse modifyBlockedBalanceByCustomerId(@RequestBody @Valid CustomerFundsModifyBlockedBalanceByCustomerIdRequest request);

    /**
     * 根据会员ID更新会员名称
     *
     * @param request 包含会员ID、会员名称 {@link CustomerFundsModifyCustomerNameByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/modify-customer-name-by-id")
    BaseResponse modifyCustomerNameByCustomerId(@RequestBody @Valid CustomerFundsModifyCustomerNameByCustomerIdRequest request);

    /**
     * 根据会员资金ID和提现状态更新会员资金（提交：更新冻结余额、可提现余额；同意：账户余额、冻结余额；驳回：冻结余额、可提现余额）
     *
     * @param request 包含会员资金ID、提现金额、提现状态  {@link CustomerFundsModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/modify-customer-funds-by-id-and-withdraw-amount-status")
    BaseResponse modifyCustomerFundsByIdAndWithdrawAmountStatus(@RequestBody @Valid CustomerFundsModifyRequest request);

    /**
     * 根据会员资金ID和提现状态更新会员资金（提交：更新冻结余额、可提现余额；同意：账户余额、冻结余额；驳回：冻结余额、可提现余额）
     *
     * @param request 包含会员资金ID、提现金额、提现状态  {@link CustomerFundsModifyRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/account/${application.account.version}/funds/agree-amount-paid-and-expenditure")
    BaseResponse agreeAmountPaidAndExpenditure(@RequestBody @Valid CustomerFundsModifyRequest request);

    /**
     * 发放邀新奖励、分销佣金
     */
    @PostMapping("/account/${application.account.version}/funds/grant-amount")
    BaseResponse grantAmount(@RequestBody @Valid GrantAmountRequest request);

    /**
     * 初始化会员资金统计缓存
     *
     * @return
     */
    @PostMapping("/account/${application.account.version}/funds/init-statistics-cache")
    BaseResponse initStatisticsCache();

    /**
     * 初始化会员资金统计缓存
     *
     * @return
     */
    @PostMapping("/account/${application.account.version}/funds/balancePay")
    BaseResponse balancePay(@RequestBody @Valid CustomerFundsModifyRequest request);

    /**
     * @return com.wanmi.sbc.common.base.BaseResponse
     * @Author lvzhenwei
     * @Description 增加账户余额
     * @Date 14:58 2019/7/16
     * @Param [request]
     **/
    @PostMapping("/account/${application.account.version}/funds/add-amount")
    BaseResponse addAmount(@RequestBody @Valid CustomerFundsAddAmountRequest request);
}
