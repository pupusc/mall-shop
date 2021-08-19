package com.wanmi.sbc.account.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.account.api.provider.funds.CustomerFundsQueryProvider;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsByCustomerIdRequest;
import com.wanmi.sbc.account.api.request.funds.GrantAmountRequest;
import com.wanmi.sbc.account.api.response.funds.CustomerFundsByCustomerIdForEsResponse;
import com.wanmi.sbc.account.customerdrawcash.service.CustomerDrawCashService;
import com.wanmi.sbc.account.funds.model.root.CustomerFunds;
import com.wanmi.sbc.account.funds.service.CustomerFundsService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.customer.api.request.mq.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * MQ消费者
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:28
 * @version: 1.0
 */
@Slf4j
@Component
@EnableBinding(AccountSink.class)
public class ConsumerService {

    @Autowired
    private CustomerFundsService customerFundsService;


    @Autowired
    private CustomerDrawCashService customerDrawCashService;

    @Autowired
    private EsCustomerFundsMqService esCustomerFundsMqService;




    /**
     * 更新会员资金-会员账号字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT)
    public void modifyCustomerAccountWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyCustomerAccountRequest request = JSONObject.parseObject(json, CustomerFundsModifyCustomerAccountRequest.class);
            int result = customerFundsService.updateCustomerAccountByCustomerId(request.getCustomerId(),request.getCustomerAccount());
            //发mq给es 修改es 会员资金账号
            if(result != 0){
                esCustomerFundsMqService.modifyEsCustomerAccountWithCustomerFunds(request.getCustomerId(), request.getCustomerAccount());
            }

            log.info("更新会员资金-会员账号字段，是否成功 ? {}",result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新会员资金-会员账号字段，发生异常! param={}", json, e);
        }
    }


    /**
     * 更新会员提现-会员账号字段
     * @param json
     * @author chenyufei
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_ACCOUNT)
    public void modifyCustomerAccountWithCustomerDrawCash(String json) {
        try {
            CustomerDrawCashModifyCustomerAccountRequest request = JSONObject.parseObject(json, CustomerDrawCashModifyCustomerAccountRequest.class);
            int result = customerDrawCashService.updateCustomerAccountByCustomerId(request.getCustomerId(),request.getCustomerAccount());
            log.info("更新会员提现-会员账号字段，是否成功 ? {}",result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新会员提现-会员账号字段，发生异常! param={}", json, e);
        }
    }

    /**
     * 更新会员资金-会员名称字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME)
    public void modifyCustomerNameWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyCustomerNameRequest request = JSONObject.parseObject(json, CustomerFundsModifyCustomerNameRequest.class);
            int result = customerFundsService.updateCustomerNameByCustomerId(request.getCustomerId(),request.getCustomerName());
            //发mq给es 修改es 会员资金账号
            if(result != 0){
                esCustomerFundsMqService.modifyEsCustomerNameWithCustomerFunds(request.getCustomerId(), request.getCustomerName());
            }
            log.info("更新会员资金-会员名称字段，是否成功 ? {}",result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新会员资金-会员名称字段,发生异常! param={}", json, e);
        }
    }


    /**
     * 更新会员提现-会员名称字段
     * @param json
     * @author chenyufei
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_NAME)
    public void modifyCustomerNameWithCustomerDrawCash(String json) {
        try {
            CustomerDrawCashModifyCustomerNameRequest request = JSONObject.parseObject(json, CustomerDrawCashModifyCustomerNameRequest.class);
            int result = customerDrawCashService.updateCustomerNameByCustomerId(request.getCustomerId(),request.getCustomerName());
            log.info("更新会员提现-会员名称字段，是否成功 ? {}",result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新会员提现-会员名称字段,发生异常! param={}", json, e);
        }
    }

    /**
     * 更新会员资金-会员名称、会员账号字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT)
    public void modifyCustomerNameAndAccountWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyCustomerNameAndAccountRequest request = JSONObject.parseObject(json, CustomerFundsModifyCustomerNameAndAccountRequest.class);
            int result = customerFundsService.updateCustomerNameAndAccountByCustomerId(request.getCustomerId(),request.getCustomerName(),request.getCustomerAccount());
            //发mq给es 修改es 会员资金账号
            if(result != 0){
                esCustomerFundsMqService.modifyEsCustomerNameAndAccountWithCustomerFunds(request.getCustomerId(), request.getCustomerName(),request.getCustomerAccount());
            }
            log.info("更新会员资金-会员名称、会员账号字段，是否成功 ? {}",result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新会员资金-会员名称、会员账号字段,发生异常! param={}", json, e);
        }
    }


    /**
     * 新增会员，初始化会员资金信息
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_FUNDS_ADD_INIT)
    public void initCustomerFunds(String json) {
        try {
            CustomerFundsAddRequest request = JSONObject.parseObject(json, CustomerFundsAddRequest.class);
            CustomerFunds customerFunds = customerFundsService.findByCustomerId(request.getCustomerId());
            if (Objects.isNull(customerFunds)){
                CustomerFunds result = customerFundsService.init(request.getCustomerId(),request.getCustomerName(),request.getCustomerAccount(), BigDecimal.ZERO, NumberUtils.INTEGER_ZERO);
                //发mq给es 修改es 会员资金信息
                if(Objects.nonNull(result)){
                    CustomerFundsSaveRequest saveRequest = KsBeanUtil.convert(result,CustomerFundsSaveRequest.class);
                    esCustomerFundsMqService.initEsCustomerFunds(saveRequest);
                }
                log.info("新增会员，初始化会员资金信息，是否成功 ? {}", Objects.nonNull(result) ? "成功" : "失败");
            }else{
                log.info("用户ID:{},此账户会员资金信息，已存在！",request.getCustomerId());
            }
        } catch (Exception e) {
            log.error("新增会员，初始化会员资金信息, 发生异常！param={}", json, e);
        }
    }

    /**
     * 新增分销员，更新会员资金-是否分销员字段
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_IS_DISTRIBUTOR)
    public void modifyIsDistributorWithCustomerFunds(String json) {
        try {
            CustomerFundsModifyIsDistributorRequest request = JSONObject.parseObject(json, CustomerFundsModifyIsDistributorRequest.class);
            CustomerFunds customerFunds = customerFundsService.findByCustomerId(request.getCustomerId());
            CustomerFunds addResult = new CustomerFunds();
            if (Objects.isNull(customerFunds)){
                addResult = customerFundsService.init(request.getCustomerId(),request.getCustomerName(),request.getCustomerAccount(),BigDecimal.ZERO, NumberUtils.INTEGER_ONE);
                log.info("新增分销员，更新会员资金-是否分销员字段，查询不到用户ID:{0}，存在会员资金记录，执行插入一条会员资金记录，默认分销员,是否成功 ? {1}",addResult.getCustomerId(), Objects.nonNull(addResult) ? "成功" : "失败");
            }else{
                int result = customerFundsService.updateIsDistributorByCustomerId(request.getCustomerId(),NumberUtils.INTEGER_ONE);
                log.info("新增分销员，更新会员资金-是否分销员字段,是否成功 ? {}",result == 0 ? "失败" : "成功");
            }
            if(Objects.nonNull(addResult)){
                esCustomerFundsMqService.initEsCustomerFunds(KsBeanUtil.convert(addResult,CustomerFundsSaveRequest.class));
            }else{
                customerFunds.setDistributor(NumberUtils.INTEGER_ONE);
                esCustomerFundsMqService.initEsCustomerFunds(KsBeanUtil.convert(customerFunds,CustomerFundsSaveRequest.class));
            }

        } catch (Exception e) {
            log.error("新增分销员，更新会员资金-是否分销员字段, 发生异常！param={}", json, e);
        }
    }

    /**
     * 邀新注册-发放邀新奖金
     * @param json
     */
    @StreamListener(JmsDestinationConstants.Q_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT)
    public void grantAmountWithCustomerFunds(String json) {
        try {
            GrantAmountRequest request = JSONObject.parseObject(json, GrantAmountRequest.class);
            CustomerFunds funds = customerFundsService.grantAmount(request);
            //根据customerId查询会员资金信息同步到es
            if (Objects.nonNull(funds)) {
                EsCustomerFundsGrantAmountRequest saveRequest = KsBeanUtil.convert(funds, EsCustomerFundsGrantAmountRequest.class);
                esCustomerFundsMqService.modifyEsInviteGrantAmountWithCustomerFunds(saveRequest);
            } else {
                esCustomerFundsMqService.modifyEsInviteGrantAmountWithCustomerFunds(EsCustomerFundsGrantAmountRequest
                        .builder()
                        .customerId(request.getCustomerId())
                        .amount(request.getAmount()).build());
            }
        } catch (Exception e) {
            log.error("邀新注册-发放奖金, 发生异常！param={}", json, e);
        }
    }


}
