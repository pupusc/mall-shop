package com.wanmi.sbc.customer.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.customer.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.customer.api.request.customer.CustomerToDistributorModifyRequest;
import com.wanmi.sbc.customer.api.request.employee.EmployeeHandoverRequest;
import com.wanmi.sbc.customer.api.request.mq.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * MQ生产者
 * @author: Geek Wang
 * @createDate: 2019/2/25 13:57
 * @version: 1.0
 */
@Service
@Slf4j
@EnableBinding
public class ProducerService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * 修改会员账号，发送MQ消息，同时修改会员资金里的会员账号字段
     * @param customerId 会员ID
     * @param customerAccount 会员账号
     */
    public void modifyCustomerAccountWithCustomerFunds(String customerId,String customerAccount) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyCustomerAccountRequest(customerId,customerAccount))));
    }

    /**
     * 修改会员名称，发送MQ消息，同时修改会员资金里的会员名称字段
     * @param customerId 会员ID
     * @param customerName 会员名称
     */
    public void modifyCustomerNameWithCustomerFunds(String customerId,String customerName) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyCustomerNameRequest(customerId,customerName))));
    }

    /**
     * 修改会员名称、会员账号，发送MQ消息，同时修改会员资金里的会员名称、会员账号字段
     * @param customerId 会员ID
     * @param customerName 会员名称
     * @param customerAccount 会员账号
     */
    public void modifyCustomerNameAndAccountWithCustomerFunds(String customerId,String customerName,String customerAccount) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_CUSTOMER_NAME_AND_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyCustomerNameAndAccountRequest(customerId,customerName,customerAccount))));

    }

    /**
     * 初始化会员资金信息
     * @param customerId 会员ID
     * @param customerName 会员名称
     * @param customerAccount 会员账号
     */
    public void initCustomerFunds(String customerId,String customerName,String customerAccount){
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_FUNDS_ADD_INIT).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsAddRequest(customerId,customerName,customerAccount))));
    }

    /**
     * 新增分销员，更新会员资金-是否分销员字段
     * @param customerId 会员ID
     * @param customerName 会员名称
     * @param customerAccount 会员账号
     */
    public void modifyIsDistributorWithCustomerFunds(String customerId,String customerName,String customerAccount) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_FUNDS_MODIFY_IS_DISTRIBUTOR).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerFundsModifyIsDistributorRequest(customerId,customerName,customerAccount))));

    }


    /**
     * 修改会员账号，发送MQ消息，同时修改会员提现管理里的会员账号字段
     * @param customerId 会员ID
     * @param customerAccount 会员账号
     * @author chenyufei
     */
    public void modifyCustomerAccountWithCustomerDrawCash(String customerId,String customerAccount) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_ACCOUNT).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerDrawCashModifyCustomerAccountRequest(customerId,customerAccount))));

    }

    /**
     * 修改会员名称，发送MQ消息，同时修改会员提现管理里的会员名称字段
     * @param customerId 会员ID
     * @param customerName 会员名称
     * @author chenyufei
     */
    public void modifyCustomerNameWithCustomerDrawCash(String customerId,String customerName) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_DRAW_CASH_MODIFY_CUSTOMER_NAME).send(new GenericMessage<>(JSONObject.toJSONString(new CustomerDrawCashModifyCustomerNameRequest(customerId,customerName))));

    }

    /**
     * 邀新注册-发放奖励奖金
     * @param request
     */
    public void modifyInviteGrantAmountWithCustomerFunds(CustomerFundsGrantAmountRequest request) {
        resolver.resolveDestination(JmsDestinationConstants.Q_ACCOUNT_FUNDS_INVITE_GRANT_AMOUNT).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 邀新注册-发放优惠券
     * @param request
     */
    public void addCouponGroupFromInviteNew(CouponGroupAddRequest request){
        resolver.resolveDestination(JmsDestinationConstants.Q_MARKET_COUPON_INVITE_NEW_ADD).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 发送push、站内信、短信
     * @param request
     */
    public void sendMessage(MessageMQRequest request){
        resolver.resolveDestination(JmsDestinationConstants.Q_SMS_SERVICE_MESSAGE_SEND).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }

    /**
     * 业务员交接数据
     * @param employeeIds
     */
    public void modifyEmployeeData(List<String> employeeIds, String newEmployeeId){
        resolver.resolveDestination(JmsDestinationConstants.Q_ORDER_SERVICE_MODIFY_EMPLOYEE_DATA).send(new GenericMessage<>(JSONObject.toJSONString(new EmployeeHandoverRequest(employeeIds, newEmployeeId))));
    }


    /**
     * 修改会员ES的是否分销员字段
     * @param customerId
     * @param isDistributor
     */
    public void updateCustomerToDistributor(String customerId,DefaultFlag isDistributor){
        resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_CUSTOMER_DISTRIBUTOR).send(new GenericMessage<>(JSONObject.toJSONString(CustomerToDistributorModifyRequest.builder().customerId(customerId).isDistributor(isDistributor).build())));
    }

}
