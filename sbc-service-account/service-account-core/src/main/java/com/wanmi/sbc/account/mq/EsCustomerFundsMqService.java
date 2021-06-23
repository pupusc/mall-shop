package com.wanmi.sbc.account.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.customer.api.request.employee.EmployeeHandoverRequest;
import com.wanmi.sbc.customer.api.request.mq.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@EnableBinding
public class EsCustomerFundsMqService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 修改会员账号，发送MQ消息，同时修改es会员资金里的会员账号字段
     * @param customerId 会员ID
     * @param customerAccount 会员账号
     */
    public void modifyEsCustomerAccountWithCustomerFunds(String customerId,String customerAccount) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyCustomerAccountRequest(customerId,customerAccount))));
    }


    /**
     * 修改ES会员名称，发送MQ消息，同时修改会员资金里的会员名称字段
     * @param customerId 会员ID
     * @param customerName 会员名称
     */
    public void modifyEsCustomerNameWithCustomerFunds(String customerId,String customerName) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyCustomerNameRequest(customerId,customerName))));
    }

    /**
     * 修改ES会员名称、会员账号，发送MQ消息，同时修改会员资金里的会员名称、会员账号字段
     * @param customerId 会员ID
     * @param customerName 会员名称
     * @param customerAccount 会员账号
     */
    public void modifyEsCustomerNameAndAccountWithCustomerFunds(String customerId,String customerName,String customerAccount) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyCustomerNameAndAccountRequest(customerId,customerName,customerAccount))));

    }

    /**
     * 初始化ES会员资金信息
     */
    public void initEsCustomerFunds(CustomerFundsSaveRequest request){
        resolver.resolveDestination(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_ADD_INIT).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }


    /**
     * ES邀新注册-发放奖励奖金
     * @param request
     */
    public void modifyEsInviteGrantAmountWithCustomerFunds(EsCustomerFundsGrantAmountRequest request) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ES_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

}
