package com.wanmi.sbc.customer.provider.impl.distribution;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionCustomerSaveProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerToDistributorModifyRequest;
import com.wanmi.sbc.customer.api.request.distribution.*;
import com.wanmi.sbc.customer.api.response.distribution.*;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerShowPhoneVO;
import com.wanmi.sbc.customer.bean.vo.DistributionCustomerVO;
import com.wanmi.sbc.customer.distribution.model.root.DistributionCustomer;
import com.wanmi.sbc.customer.distribution.service.DistributionCustomerService;
import com.wanmi.sbc.customer.mq.ProducerService;
import com.wanmi.sbc.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * <p>分销员保存服务接口实现</p>
 *
 * @author lq
 * @date 2019-02-19 10:13:15
 */
@RestController
@Validated
public class DistributionCustomerSaveController implements DistributionCustomerSaveProvider {

    @Autowired
    private DistributionCustomerService distributionCustomerService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProducerService producerService;

    /**
     * 新增分销员API（运营后台）
     *
     * @param distributionCustomerAddForBossRequest 分销员新增参数结构 {@link DistributionCustomerAddForBossResponse}
     * @return 新增的分销员信息 {@link DistributionCustomerAddResponse}
     * @author lq
     */
    @Override
    public BaseResponse<EsDistributionCustomerAddResponse> addForBoss(@RequestBody @Valid DistributionCustomerAddForBossRequest distributionCustomerAddForBossRequest) {
        DistributionCustomerShowPhoneVO vo = distributionCustomerService.addForBoss(distributionCustomerAddForBossRequest);
        if(Objects.nonNull(vo)){
            //更新会员资金-是否分销员字段
            producerService.modifyIsDistributorWithCustomerFunds(vo.getCustomerId(),vo.getCustomerName(),vo.getCustomerAccount());
            //更新会员详细信息is_distributor
            customerService.updateCustomerToDistributor(CustomerToDistributorModifyRequest.builder().customerId(vo.getCustomerId()).isDistributor(DefaultFlag.YES).build());
        }
        return BaseResponse.success(new EsDistributionCustomerAddResponse(vo));
    }

    /**
     * 新增分销员信息
     * @param distributionCustomerAdRequest
     * @return
     */
    @Override
    public BaseResponse<DistributionCustomerAddResponse> add(@RequestBody @Valid DistributionCustomerAddRequest distributionCustomerAdRequest) {
        DistributionCustomerVO customerVO = distributionCustomerService.add(distributionCustomerAdRequest);
        return BaseResponse.success(new DistributionCustomerAddResponse(customerVO));
    }

    /**
     * 更新分销员奖励信息
     * @param request
     * @return
     */
    @Override
    public BaseResponse modifyReward(@RequestBody @Valid DistributionCustomerModifyRewardRequest request) {
        distributionCustomerService.modifyReward(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 审核分销员状态
     *
     * @param request 审核分销员状态参数结构 {@link DistributionCustomeffBatchModifyRequest}
     * @return 审核分销员状态返回参数 {@link DistributionCustomerAddResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerForbiddenFlagBatchModifyResponse> modifyForbiddenFlagById(@RequestBody @Valid
                                                                                                              DistributionCustomeffBatchModifyRequest request) {
        int count = distributionCustomerService.updateForbiddenFlag(request.getForbiddenFlag(), request.getDistributionIds(), request
                .getForbiddenReason());

        return BaseResponse.success(new DistributionCustomerForbiddenFlagBatchModifyResponse(count));
    }


    /**¬
     * 修改分销员信息
     *
     * @param distributionCustomerModifyRequest 审核分销员状态参数结构 {@link DistributionCustomerModifyRequest}
     * @return 审核分销员状态返回参数 {@link DistributionCustomerModifyResponse}
     * @author lq
     */
    @Override
    public BaseResponse<DistributionCustomerModifyResponse> modify(@RequestBody @Valid DistributionCustomerModifyRequest distributionCustomerModifyRequest) {
        DistributionCustomer distributionCustomer = new DistributionCustomer();
        KsBeanUtil.copyPropertiesThird(distributionCustomerModifyRequest, distributionCustomer);
        return BaseResponse.success(new DistributionCustomerModifyResponse(
                distributionCustomerService.wrapperVo(distributionCustomerService.modify(distributionCustomer))));
    }




    /**
     * 根据会员ID更新会员账号
     * @param request 包含会员ID、会员账号 {@link DistributionCustomerModifyCustomerAccountByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyCustomerAccountByCustomerId(@RequestBody @Valid DistributionCustomerModifyCustomerAccountByCustomerIdRequest request) {
        distributionCustomerService.updateCustomerAccountByCustomerId(request.getCustomerId(),request.getCustomerAccount());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 根据会员ID更新会员名称
     * @param request 包含会员ID、会员名称 {@link DistributionCustomerModifyCustomerNameByCustomerIdRequest}
     * @return {@link BaseResponse}
     */
    @Override
    public BaseResponse modifyCustomerNameByCustomerId(@RequestBody @Valid DistributionCustomerModifyCustomerNameByCustomerIdRequest request) {
        distributionCustomerService.updateCustomerNameByCustomerId(request.getCustomerId(),request.getCustomerName());
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse modifyCustomerId(@Valid DistributionCustomerModifyCustomerIdRequest request) {
        distributionCustomerService.modifyCustomerId(request.getOldCustomerId(),request.getCustomerId());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 升级分销员
     */
    @Override
    public BaseResponse upgrade(@RequestBody @Valid DistributionCustomerUpgradeRequest request) {
        distributionCustomerService.upgrade(request.getCustomerId(),request.getDistributorLevelVOList(),request.getBaseLimitType(),request.getInviteNum());
        return BaseResponse.SUCCESSFUL();
    }

    /**
     * 分销佣金结算后更新分销员统计相关信息
     *
     * @param request
     */
    @Override
    public BaseResponse afterSettleUpdate(@RequestBody @Valid AfterSettleUpdateDistributorRequest request) {
        distributionCustomerService.afterSettleUpdate(request);
        return BaseResponse.SUCCESSFUL();
    }

    /**
     *  补发邀新记录后更新分销员统计相关信息
     *
     * @param request
     */
    @Override
    public BaseResponse afterSupplyAgainUpdate(@RequestBody @Valid AfterSupplyAgainUpdateDistributorRequest request) {
        distributionCustomerService.afterSupplyAgainUpdate(request);
        return BaseResponse.SUCCESSFUL();
    }

    @Override
    public BaseResponse batchModifyCommissionByCustomerId(@RequestBody @Valid DistributionCustomerBatchModifyCommissionByCustomerIdRequest request) {
        int result = distributionCustomerService.batchUpdateCommissionByCustomerId(request.getList());
        return BaseResponse.success(result);
    }


    @Override
    public BaseResponse initInviteCode(){
        distributionCustomerService.initInviteCode();
        return BaseResponse.success(Boolean.TRUE);
    }
}

