package com.wanmi.sbc.customer.api.provider.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.customer.api.request.detail.CustomerStateBatchModifyRequest;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddForBossResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerAddResponse;
import com.wanmi.sbc.customer.api.response.distribution.DistributionCustomerModifyResponse;
import com.wanmi.sbc.customer.api.response.distribution.EsDistributionCustomerAddResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>分销员保存服务Provider</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@FeignClient(value = "${application.customer.name}", contextId = "DistributionCustomerSaveProvider")
public interface DistributionCustomerSaveProvider {

    /**
     * 新增分销员API（运营后台）
     *
     * @param distributionCustomerAddForBossRequest 分销员新增参数结构 {@link DistributionCustomerAddRequest}
     * @return 新增的分销员信息 {@link DistributionCustomerAddResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/add-for-boss")
    BaseResponse<EsDistributionCustomerAddResponse> addForBoss(@RequestBody @Valid DistributionCustomerAddForBossRequest distributionCustomerAddForBossRequest);

    /**
     * 新增分销员信息
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/add")
    BaseResponse<DistributionCustomerAddResponse> add(@RequestBody @Valid DistributionCustomerAddRequest request);

    /**
     * 更新分销员奖励信息
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/modify-reward")
    BaseResponse modifyReward(@RequestBody @Valid DistributionCustomerModifyRewardRequest request);

    /**
     * 更新分销员账户状态
     *
     * @param request 批量更新账户状态和禁用原因request{@link CustomerStateBatchModifyRequest}
     * @return 修改会员状态结果{@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/modify-forbidden-flag-by-distribution-id")
    BaseResponse modifyForbiddenFlagById(@RequestBody @Valid DistributionCustomeffBatchModifyRequest request);


    /**
     * 修改分销员API
     *
     * @param distributionCustomerModifyRequest 分销员修改参数结构 {@link DistributionCustomerModifyRequest}
     * @return 修改的分销员信息 {@link DistributionCustomerModifyResponse}
     * @author lq
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/modify")
    BaseResponse<DistributionCustomerModifyResponse> modify(@RequestBody @Valid DistributionCustomerModifyRequest distributionCustomerModifyRequest);

    /**
     * 根据会员ID更新会员账号
     *
     * @param request 包含会员ID、会员账号 {@link DistributionCustomerModifyCustomerAccountByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/modify-customer-account-by-id")
    BaseResponse modifyCustomerAccountByCustomerId(@RequestBody @Valid DistributionCustomerModifyCustomerAccountByCustomerIdRequest request);

    /**
     * 根据会员ID更新会员名称
     *
     * @param request 包含会员ID、会员名称 {@link DistributionCustomerModifyCustomerNameByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/modify-customer-name-by-id")
    BaseResponse modifyCustomerNameByCustomerId(@RequestBody @Valid DistributionCustomerModifyCustomerNameByCustomerIdRequest request);
    /**
     * 根据更新会员ID
     *
     * @param request 替换会员绑定ID {@link DistributionCustomerModifyCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/modify-customer-id")
    BaseResponse modifyCustomerId(@RequestBody @Valid DistributionCustomerModifyCustomerIdRequest request);

    /**
     * 升级分销员
     *
     * @param request {@link DistributionCustomerUpgradeRequest}
     * @return {@link BaseResponse}
     */
    @PostMapping("/customer/${application.customer.version}/distributioncustomer/upgrade")
    BaseResponse upgrade(@RequestBody @Valid DistributionCustomerUpgradeRequest request);

    /**
     *  分销佣金结算后更新分销员统计相关信息
     */
    @PostMapping("/customer/${application.customer.version}/distributor-update-after-settle")
    BaseResponse afterSettleUpdate(@RequestBody @Valid AfterSettleUpdateDistributorRequest request);

    /**
     *  补发邀新记录后更新分销员统计相关信息
     */
    @PostMapping("/customer/${application.customer.version}/distributor-update-after-supply-again")
    BaseResponse afterSupplyAgainUpdate(@RequestBody @Valid AfterSupplyAgainUpdateDistributorRequest request);


    /**
     * 批量更新更新分销员提成-已入账佣金包含提成
     * @param request
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/batch-modify-commission-by-customer-id")
    BaseResponse batchModifyCommissionByCustomerId(@RequestBody @Valid DistributionCustomerBatchModifyCommissionByCustomerIdRequest request);

    /**
     * 初始化历史分销员-邀请码
     * @return
     */
    @PostMapping("/customer/${application.customer.version}/distribution/init-invite-code")
    BaseResponse initInviteCode();
}

