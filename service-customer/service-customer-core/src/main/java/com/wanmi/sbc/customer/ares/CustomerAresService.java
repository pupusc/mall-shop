package com.wanmi.sbc.customer.ares;

import com.wanmi.sbc.customer.bean.enums.AresFunctionType;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.CustomerType;
import com.wanmi.sbc.customer.bean.enums.StoreState;
import com.wanmi.sbc.customer.employee.repository.EmployeeRepository;
import com.wanmi.sbc.customer.level.repository.CustomerLevelRepository;
import com.wanmi.sbc.customer.repository.CustomerRepository;
import com.wanmi.sbc.customer.store.repository.StoreRepository;
import com.wanmi.sbc.customer.storecustomer.repository.StoreCustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

import static com.wanmi.sbc.customer.bean.enums.AresFunctionType.*;

/**
 * 会员相关数据埋点 - 统计系统
 * Created by bail on 2017/10/12
 */
@Service
@Slf4j
@EnableBinding
public class CustomerAresService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerLevelRepository customerLevelRepository;

    @Autowired
    private StoreCustomerRepository storeCustomerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private BinderAwareChannelResolver resolver;

    private static final String S2B_COMPANY_ID = "0";//s2b平台端商户标识默认值

    private static final Map<CheckState, com.wanmi.ares.enums.CheckState> CUS_CHECK_STAT_MAP = new EnumMap<>
            (CheckState.class);//用户审核状态转换策略map

    static {
        CUS_CHECK_STAT_MAP.put(CheckState.WAIT_CHECK, com.wanmi.ares.enums.CheckState.WAIT_CHECK);
        CUS_CHECK_STAT_MAP.put(CheckState.CHECKED, com.wanmi.ares.enums.CheckState.CHECKED);
        CUS_CHECK_STAT_MAP.put(CheckState.NOT_PASS, com.wanmi.ares.enums.CheckState.NOT_PASS);
    }

    private static final Map<CustomerType, com.wanmi.ares.enums.CustomerType> CUSTOMER_TYPE_MAP = new EnumMap<>
            (CustomerType.class);//客户类型转换策略map

    static {
        CUSTOMER_TYPE_MAP.put(CustomerType.PLATFORM, com.wanmi.ares.enums.CustomerType.PLATFORM);
        CUSTOMER_TYPE_MAP.put(CustomerType.SUPPLIER, com.wanmi.ares.enums.CustomerType.SUPPLIER);
    }

    private static final Map<StoreState, com.wanmi.ares.enums.StoreState> STORE_STATE_MAP = new EnumMap<>(StoreState
            .class);//客户类型转换策略map

    static {
        STORE_STATE_MAP.put(StoreState.CLOSED, com.wanmi.ares.enums.StoreState.CLOSED);
        STORE_STATE_MAP.put(StoreState.OPENING, com.wanmi.ares.enums.StoreState.OPENING);
    }

    /**
     * 初始化会员
     */
    public void initCustomerES() {
        customerRepository.findAll().forEach(customer -> dispatchFunction(ADD_CUSTOMER, customer, customer
                .getCustomerDetail()));//不知道是boss创建 还是注册的
    }

    /**
     * 初始化会员等级
     */
    public void initCustomerLevelES() {
        customerLevelRepository.findAll().forEach(customerLevel -> dispatchFunction(ADD_CUSTOMER_LEVEL,
                customerLevel));
    }

    /**
     * 初始化店铺会员(会员等级)关系
     */
    public void initStoreCustomerRelaES() {
        storeCustomerRepository.findAll().forEach(storeCustomerRela -> dispatchFunction
                (ADD_STORE_CUSTOMER_RELA, storeCustomerRela));
    }

    /**
     * 初始化业务员
     */
    public void initEmployeeES() {
        employeeRepository.findAll().forEach(employee -> dispatchFunction(ADD_EMPLOYEE, employee));
    }

    /**
     * 初始化店铺
     */
    public void initStoreES() {
        storeRepository.findAll().forEach(store -> dispatchFunction(ADD_STORE, store));
    }

    /**
     * 埋点处理的分发方法
     *
     * @param funcType 类别,依据此进行分发
     * @param objs     多个入参对象
     */
    @Async
    public void dispatchFunction(AresFunctionType funcType, Object... objs) {

    }



}
