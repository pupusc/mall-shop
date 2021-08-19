package com.wanmi.sbc.account.provider.impl.funds;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsProvider;
import com.wanmi.sbc.account.api.request.funds.*;
import com.wanmi.sbc.account.bean.enums.WithdrawAmountStatus;
import com.wanmi.sbc.account.funds.service.CustomerFundsService;
import com.wanmi.sbc.common.base.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 会员资金新增、更新接口
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 14:33
 * @version: 1.0
 */
@RestController
@Validated
public class CustomerFundsController implements CustomerFundsProvider {

    @Autowired
    private CustomerFundsService customerFundsService;

    /**
     * 新增会员资金
     *
     * @param request 新增会员资金数据集 {@link CustomerFundsAddRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse add(@RequestBody @Valid CustomerFundsAddRequest request) {
        customerFundsService.init(request.getCustomerId(), request.getCustomerName(), request.getCustomerAccount(), request.getAccountBalance(), request.getDistributor());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID更新会员账号
     *
     * @param request 包含会员ID、会员账号 {@link CustomerFundsModifyCustomerAccountByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyCustomerAccountByCustomerId(@RequestBody @Valid CustomerFundsModifyCustomerAccountByCustomerIdRequest request) {
        customerFundsService.updateCustomerAccountByCustomerId(request.getCustomerId(), request.getCustomerAccount());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID更新已提现金额
     *
     * @param request 包含会员ID、已提现金额 {@link CustomerFundsModifyAlreadyDrawCashAmountByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyAlreadyDrawCashAmountByCustomerId(@RequestBody CustomerFundsModifyAlreadyDrawCashAmountByCustomerIdRequest request) {
        customerFundsService.updateAlreadyDrawCashAmountByCustomerId(request.getCustomerId(), request.getAlreadyDrawCashAmount());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID更新账户余额
     *
     * @param request 包含会员ID、账户余额 {@link CustomerFundsModifyAccountBalanceByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyAccountBalanceByCustomerId(@RequestBody CustomerFundsModifyAccountBalanceByCustomerIdRequest request) {
        customerFundsService.updateAccountBalanceByCustomerId(request.getCustomerId(), request.getAccountBalance());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID更新冻结金额
     *
     * @param request 包含会员ID、冻结金额 {@link CustomerFundsModifyBlockedBalanceByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyBlockedBalanceByCustomerId(@RequestBody CustomerFundsModifyBlockedBalanceByCustomerIdRequest request) {
        customerFundsService.updateBlockedBalanceByCustomerId(request.getCustomerId(), request.getBlockedBalance());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID更新会员名称
     *
     * @param request 包含会员ID、会员名称 {@link CustomerFundsModifyCustomerNameByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyCustomerNameByCustomerId(@RequestBody @Valid CustomerFundsModifyCustomerNameByCustomerIdRequest request) {
        customerFundsService.updateCustomerNameByCustomerId(request.getCustomerId(), request.getCustomerName());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员资金ID和提现状态更新会员资金（提交：更新冻结余额、可提现余额；同意：账户余额、冻结余额；驳回：冻结余额、可提现余额）
     *
     * @param request 包含会员资金ID、提现金额、提现状态  {@link CustomerFundsModifyRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyCustomerFundsByIdAndWithdrawAmountStatus(@RequestBody @Valid CustomerFundsModifyRequest request) {
        int result = 0;
        WithdrawAmountStatus withdrawAmountStatus = request.getWithdrawAmountStatus();
        if (withdrawAmountStatus == WithdrawAmountStatus.SUBMIT) {
            result = customerFundsService.submitWithdrawCashApply(request.getCustomerFundsId(), request.getWithdrawAmount());
        } else if (withdrawAmountStatus == WithdrawAmountStatus.AGREE) {
            result = customerFundsService.agreeWithdrawCashApply(request.getCustomerFundsId(), request.getWithdrawAmount());
        } else if (withdrawAmountStatus == WithdrawAmountStatus.REJECT) {
            result = customerFundsService.rejectWithdrawCashApply(request.getCustomerFundsId(), request.getWithdrawAmount());
        }
        return result > 0 ? BaseResponse.SUCCESSFUL() : BaseResponse.FAILED();
    }

    /**
     * 根据会员资金ID和提现金额更新支出金额、支出数（同意用户提现申请）
     *
     * @param request 包含会员资金ID、提现金额  {@link CustomerFundsModifyRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse agreeAmountPaidAndExpenditure(@RequestBody @Valid CustomerFundsModifyRequest request) {
        int result = customerFundsService.agreeAmountPaidAndExpenditure(request.getCustomerFundsId(), request.getWithdrawAmount());
        return result > 0 ? BaseResponse.SUCCESSFUL() : BaseResponse.FAILED();
    }

    /**
     * 发放邀新奖励、分销佣金
     *
     * @param request
     */
    @Override
    public BaseResponse grantAmount(@RequestBody @Valid GrantAmountRequest request) {
        //更新会员资金表 保存余额明细
        customerFundsService.grantAmount(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 初始化会员资金统计缓存
     *
     * @return
     */
    @Override
    public BaseResponse initStatisticsCache() {
        Boolean map = customerFundsService.initStatisticsCache();
        return BaseResponse.success(map);
    }

    /**
     * 使用余额付款，更新资金信息
     *
     * @param request
     * @return
     */
    @Override
    public BaseResponse balancePay(@RequestBody @Valid CustomerFundsModifyRequest request) {
        int result = customerFundsService.balancePay(request.getCustomerFundsId(), request.getExpenseAmount());
        return result > 0 ? BaseResponse.SUCCESSFUL() : BaseResponse.FAILED();
    }

    /**
     * @Author lvzhenwei
     * @Description 余额支付订单退款增加账户余额金额
     * @Date 11:16 2019/7/24
     * @Param [request]
     * @return com.wanmi.sbc.common.base.BaseResponse
     **/
    @Override
    public BaseResponse addAmount(@RequestBody @Valid CustomerFundsAddAmountRequest request) {
        customerFundsService.addAmount(request);
        return BaseResponse.SUCCESSFUL();
    }
}
