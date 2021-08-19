package com.wanmi.ares.source.mq;

import com.wanmi.ares.constants.MQConstant;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @Author: songhanlin
 * @Date: Created In 10:50 2019-04-04
 * @Description: 消息接收处理 Sink
 */
public interface MessageSink {

    /**
     * 新增客户信息
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_CREATE)
    SubscribableChannel createCustomer();

    /**
     * 修改客户信息
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_MODIFY)
    SubscribableChannel modifyCustomer();

    /**
     * 删除客户信息
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_DELETE)
    SubscribableChannel deleteCustomer();

    /**
     * 修改客户审核状态
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_CHECK)
    SubscribableChannel modifyCustomerCheckState();

    /**
     * 新增店铺客户关系
     */
    @Input(MQConstant.Q_ARES_STORE_CUSTOMER_CREATE)
    SubscribableChannel createStoreCustomer();


    /**
     * 修改店铺客户关系
     */
    @Input(MQConstant.Q_ARES_STORE_CUSTOMER_MODIFY)
    SubscribableChannel modifyStoreCustomer();

    /**
     * 删除店铺客户关系
     */
    @Input(MQConstant.Q_ARES_STORE_CUSTOMER_DELETE)
    SubscribableChannel deleteStoreCustomer();

    /**
     * 新增平台的客户等级
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_LEVEL_CREATE)
    SubscribableChannel createCustomerLevel();


    /**
     * 修改平台的客户等级
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_LEVEL_MODIFY)
    SubscribableChannel modifyCustomerLevel();

    /**
     * 删除平台的客户等级
     */
    @Input(MQConstant.Q_ARES_CUSTOMER_LEVEL_DELETE)
    SubscribableChannel deleteCustomerLevel();

    /**
     * 新增业务员
     */
    @Input(MQConstant.Q_ARES_EMPLOYEE_CREATE)
    SubscribableChannel createEmployee();

    /**
     * 修改业务员
     */
    @Input(MQConstant.Q_ARES_EMPLOYEE_MODIFY)
    SubscribableChannel modifyEmployee();

    /**
     * 删除业务员
     */
    @Input(MQConstant.Q_ARES_EMPLOYEE_DELETE)
    SubscribableChannel deleteEmployee();

    /**
     * 新增店铺
     */
    @Input(MQConstant.Q_ARES_STORE_CREATE)
    SubscribableChannel createStore();

    /**
     * 修改店铺
     */
    @Input(MQConstant.Q_ARES_STORE_MODIFY)
    SubscribableChannel modifyStore();
    /**
     * 流量信息同步
     */
    @Input(MQConstant.Q_FLOW_CUSTOMER_SYNCHRONIZATION)
    SubscribableChannel flowSynchronization();

}
