package com.wanmi.sbc.customer;

import com.wanmi.sbc.account.api.provider.funds.CustomerFundsDetailQueryProvider;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailPageRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdResponse;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsStatisticsResponse;
import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.vo.CustomerFundsDetailVO;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.customer.api.provider.distribution.DistributionInviteNewQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.DistributionInviteNewListRequest;
import com.wanmi.sbc.customer.api.response.customer.DistributionInviteNewListResponse;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.bean.vo.DistributionInviteNewForPageVO;
import com.wanmi.sbc.marketing.api.provider.distributionrecord.DistributionRecordQueryProvider;
import com.wanmi.sbc.marketing.api.request.distributionrecord.DistributionRecordListRequest;
import com.wanmi.sbc.marketing.api.response.distributionrecord.DistributionRecordListResponse;
import com.wanmi.sbc.marketing.bean.enums.CommissionReceived;
import com.wanmi.sbc.util.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 会员资金页面控制类
 *
 * @author chenyufei
 */
@RestController("CustomerFundsController")
@RequestMapping("/customer/funds")
@Api(tags = "CustomerFundsController", description = "S2B 会员端-会员资金API")
public class CustomerFundsController {

    @Autowired
    private CustomerFundsQueryProvider customerFundsQueryProvider;

    @Autowired
    private CustomerFundsDetailQueryProvider customerFundsDetailQueryProvider;

    @Autowired
    private CommonUtil commonUtil;

    @Autowired
    private DistributionRecordQueryProvider distributionRecordQueryProvider;

    @Autowired
    private DistributionInviteNewQueryProvider distributionInviteNewQueryProvider;

    /**
     * 获取会员资金统计（会员余额总额、冻结余额总额、可提现余额总额）
     *
     * @return
     */
    @ApiOperation(value = "S2B 会员端-获取会员资金统计（会员余额总额、冻结余额总额、可提现余额总额）")
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public BaseResponse<CustomerFundsStatisticsResponse> statistics() {

        CustomerFundsByCustomerIdRequest request = new CustomerFundsByCustomerIdRequest();
        request.setCustomerId(commonUtil.getOperatorId());
        CustomerFundsByCustomerIdResponse idResponse = this.customerFundsQueryProvider.getByCustomerId(request).getContext();


        //获取当前用户的分销员id
        //待入账佣金金额 排除入账失败数据
        DistributionRecordListResponse distributionRecordListResponse = distributionRecordQueryProvider.list(
                DistributionRecordListRequest
                        .builder()
                        .distributorCustomerId(commonUtil.getOperatorId())
                        .commissionState(CommissionReceived.UNRECEIVE)
                        .deleteFlag(DeleteFlag.NO)
                        .build()).getContext();
        // 获得待入账得分销佣金
        BigDecimal commissionTotal = BigDecimal.ZERO;
        if(CollectionUtils.isNotEmpty(distributionRecordListResponse.getDistributionRecordVOList())){
            commissionTotal = distributionRecordListResponse.getDistributionRecordVOList().stream()
                    .map(vo -> vo.getCommissionGoods()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        DistributionInviteNewListResponse distributionInviteNewListResponse = distributionInviteNewQueryProvider.listDistributionInviteNewRecord(
                DistributionInviteNewListRequest
                        .builder()
                        .requestCustomerId(commonUtil.getOperatorId())
                        .isRewardRecorded(InvalidFlag.NO)
                        .build()
        ).getContext();
        // 获得待入账得邀新奖励金额
        BigDecimal rewardCashTotal = new BigDecimal(0);
        if(CollectionUtils.isNotEmpty(distributionInviteNewListResponse.getInviteNewListVOList())){
            rewardCashTotal = distributionInviteNewListResponse.getInviteNewListVOList().stream()
                    .map(DistributionInviteNewForPageVO::getRewardCash).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        BigDecimal blockedBalanceTotal = commissionTotal.add(rewardCashTotal);

        return BaseResponse.success(new CustomerFundsStatisticsResponse(idResponse.getAccountBalance() == null ?
                BigDecimal.ZERO : idResponse.getAccountBalance(), blockedBalanceTotal, idResponse.getWithdrawAmount() == null ?
                BigDecimal.ZERO : idResponse.getWithdrawAmount(), idResponse.getAlreadyDrawAmount() == null ?
                BigDecimal.ZERO : idResponse.getAlreadyDrawAmount()));
    }


    /**
     * 获取余额明细分页列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "S2B web公用-余额账单明细分页列表")
    @RequestMapping(value = "/page", method = RequestMethod.POST)
    public BaseResponse<MicroServicePage<CustomerFundsDetailVO>> page(@RequestBody CustomerFundsDetailPageRequest request) {
        String customerId = commonUtil.getOperatorId();
        request.setCustomerId(customerId);
        request.setFundsStatus(FundsStatus.YES);
        return BaseResponse.success(customerFundsDetailQueryProvider.page(request).getContext().getMicroServicePage());
    }


}
