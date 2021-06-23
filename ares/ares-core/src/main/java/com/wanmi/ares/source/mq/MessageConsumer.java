package com.wanmi.ares.source.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.ares.constants.MQConstant;
import com.wanmi.ares.enums.CheckState;
import com.wanmi.ares.report.customer.dao.CustomerOrderReportMapper;
import com.wanmi.ares.report.flow.service.FlowReportService;
import com.wanmi.ares.request.mq.*;
import com.wanmi.ares.source.model.root.*;
import com.wanmi.ares.source.service.*;
import com.wanmi.ares.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 消息接收处理Bean
 * <p>
 * 为了对具体消息的生产消费情况做更好地监控，消息以业务中最小粒度的操作区分
 * </p>
 * Created by of628-wenzhi on 2017-10-10-下午2:19.
 */
@Slf4j
@Component
@EnableBinding(MessageSink.class)
public class MessageConsumer {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CustomerLevelService customerLevelService;

    @Autowired
    private CustomerAndLevelService customerAndLevelService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private StoreService storeService;

    @Autowired
    private FlowReportService flowReportService;

    @Autowired
    private CustomerOrderReportMapper customerOrderReportMapper;

    /**
     * 新增客户信息
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_CREATE)
    public void createCustomer(String json) {
        try {
            CustomerAddRequest request = JSONObject.parseObject(json, CustomerAddRequest.class);
            Customer customer = new Customer();
            BeanUtils.copyProperties(customer, request);
            customer.setReceiveTime(LocalDateTime.now());
            customerService.insertCustomer(customer);
        } catch (Exception e) {
            log.error("Activemq consumer execute method [customer.create] error, param={}", json, e);
        }
    }

    /**
     * 修改客户信息
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_MODIFY)
    public void modifyCustomer(String json) {
        try {
            CustomerBaseRequest request = JSONObject.parseObject(json, CustomerBaseRequest.class);
            if (StringUtils.isBlank(request.getId())) {
                log.error("Activemq consumer execute method [customer.modify] id is empty error, param={}", json);
                return;
            }

            Customer customer = customerService.queryCustomerById(request.getId());
            if (customer != null) {
                LocalDate checkDate = customer.getCheckDate();
                CheckState checkState = customer.getCheckState();
                if (!Objects.equals(request.getEmployeeId(), customer.getEmployeeId())) {//如果修改前后绑定的业务员不一致,则修改业务员绑定日期
                    customer.setBindDate(LocalDate.now());
                }
                BeanUtils.copyProperties(customer, request);
                customer.setCheckDate(checkDate);
                customer.setCheckState(checkState);
                customer.setReceiveTime(LocalDateTime.now());
                customer.setDelFlag(false);

                customerService.updateCustomerById(customer);
                //修改用户日报表
                customerOrderReportMapper.updateCustomerDayReport(customer);
            }
        } catch (Exception e) {
            log.error("Activemq consumer execute method [customer.modify] error, param={}", json, e);
        }
    }

    /**
     * 删除客户信息
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_DELETE)
    public void deleteCustomer(String json) {
        try {
            List<String> customerIdList = JSONObject.parseArray(json, String.class);
            customerService.deleteByCustomerIds(customerIdList);
        } catch (Exception e) {
            log.error("Activemq consumer execute method [customer.delete] error, param={}", json, e);
        }
    }

    /**
     * 修改客户审核状态
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_CHECK)
    public void modifyCustomerCheckState(String json) {
        try {
            CustomerCheckRequest request = JSONObject.parseObject(json, CustomerCheckRequest.class);
            if (StringUtils.isBlank(request.getId())) {
                log.error("Activemq consumer execute method [customer.check] id is empty error, param={}", json);
                return;
            }

            Customer customer = customerService.queryCustomerById(request.getId());
            if (customer != null) {
                customer.setCheckDate(request.getCheckDate());
                customer.setCheckState(request.getCheckState());
                customer.setBindDate(request.getBindDate());
                customer.setReceiveTime(LocalDateTime.now());
                customer.setDelFlag(false);
                customerService.updateCustomerById(customer);
            }
        } catch (Exception e) {
            log.error("Activemq consumer execute method [customer.checkState] error, param={}", json, e);
        }
    }

    /**
     * 新增店铺客户关系
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_STORE_CUSTOMER_CREATE)
    public void createStoreCustomer(String json) {
        try {
            CustomerAndLevelRequest request = JSONObject.parseObject(json, CustomerAndLevelRequest.class);
            CustomerAndLevel customerLevel = new CustomerAndLevel();
            BeanUtils.copyProperties(customerLevel, request);
            customerLevel.setReceiveTime(LocalDateTime.now());
            customerLevel.setBindTime(LocalDate.now());
            customerAndLevelService.insert(customerLevel);
        } catch (Exception e) {
            log.error("Activemq store.customer execute method [customer.create] error, param={}", json, e);
        }
    }


    /**
     * 修改店铺客户关系
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_STORE_CUSTOMER_MODIFY)
    public void modifyStoreCustomer(String json) {
        try {
            CustomerAndLevelRequest request = JSONObject.parseObject(json, CustomerAndLevelRequest.class);
            Customer customer = customerService.queryCustomerById(request.getCustomerId());
            if (customer != null) {
                if (!Objects.equals(request.getEmployeeId(), customer.getEmployeeId())) {//如果修改前后绑定的业务员不一致,则修改业务员绑定日期
                    customer.setBindDate(LocalDate.now());
                }
                customerAndLevelService.update(request, customer.getBindDate());
            }
        } catch (Exception e) {
            log.error("Activemq store.customer execute method [customer.modify] error, param={}", json, e);
        }
    }

    /**
     * 删除店铺客户关系
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_STORE_CUSTOMER_DELETE)
    public void deleteStoreCustomer(String json) {
        try {
            String id = JSONObject.parseObject(json, String.class);
            customerAndLevelService.delete(id);
        } catch (Exception e) {
            log.error("Activemq store.customer execute method [customer.delete] error, param={}", json, e);
        }
    }

    /**
     * 新增平台的客户等级
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_LEVEL_CREATE)
    public void createCustomerLevel(String json) {
        try {
            CustomerLevelRequest request = JSONObject.parseObject(json, CustomerLevelRequest.class);
            CustomerLevel customerLevel = new CustomerLevel();
            BeanUtils.copyProperties(customerLevel, request);
            customerLevel.setReceiveTime(LocalDateTime.now());
            customerLevelService.insertCustomerLevel(customerLevel);
        } catch (Exception e) {
            log.error("Activemq consumer.level execute method [level.create] error, param={}", json, e);
        }
    }


    /**
     * 修改平台的客户等级
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_LEVEL_MODIFY)
    public void modifyCustomerLevel(String json) {
        try {
            CustomerLevelRequest request = JSONObject.parseObject(json, CustomerLevelRequest.class);
            CustomerLevel customerLevel = new CustomerLevel();
            BeanUtils.copyProperties(customerLevel, request);
            customerLevel.setReceiveTime(LocalDateTime.now());
            customerLevelService.updateCustomerLevel(customerLevel);
        } catch (Exception e) {
            log.error("Activemq consumer.level execute method [level.modify] error, param={}", json, e);
        }
    }

    /**
     * 删除平台的客户等级
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_CUSTOMER_LEVEL_DELETE)
    public void deleteCustomerLevel(String json) {
        try {
            String levelId = JSONObject.parseObject(json, String.class);
            customerLevelService.deleteLevel(levelId);
        } catch (Exception e) {
            log.error("Activemq consumer.level execute method [level.delete] error, param={}", json, e);
        }
    }

    /**
     * 新增业务员
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_EMPLOYEE_CREATE)
    public void createEmployee(String json) {
        try {
            EmployeeRequest request = JSONObject.parseObject(json, EmployeeRequest.class);
            Employee employee = new Employee();
            BeanUtils.copyProperties(employee, request);
            employee.setReceiveTime(LocalDateTime.now());
            employeeService.insertEmployee(employee);
        } catch (Exception e) {
            log.error("Activemq employee execute method [employee.create] error, param={}", json, e);
        }
    }

    /**
     * 修改业务员
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_EMPLOYEE_MODIFY)
    public void modifyEmployee(String json) {
        try {
            EmployeeRequest request = JSONObject.parseObject(json, EmployeeRequest.class);
            employeeService.modify(request);
        } catch (Exception e) {
            log.error("Activemq employee execute method [employee.modify] error, param={}", json, e);
        }
    }

    /**
     * 删除业务员
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_EMPLOYEE_DELETE)
    public void deleteEmployee(String json) {
        List<String> employeeIdList = JSONObject.parseArray(json, String.class);
        employeeIdList.stream().forEach(employeeId -> {
            try {
                employeeService.delete(employeeId);
            } catch (Exception e) {
                log.error("Activemq employee execute method [employee.delete] error, param={}", json, e);
            }
        });
    }

    /**
     * 新增店铺
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_STORE_CREATE)
    public void createStore(String json) {
        try {
            StoreRequest request = JSONObject.parseObject(json, StoreRequest.class);
            Store store = new Store();
            BeanUtils.copyProperties(store, request);
            store.setReceiveTime(LocalDateTime.now());
            storeService.insertStore(store);
        } catch (Exception e) {
            log.error("Activemq store execute method [store.create] error, param={}", json, e);
        }
    }

    /**
     * 修改店铺
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_ARES_STORE_MODIFY)
    public void modifyStore(String json) {
        try {
            StoreRequest request = JSONObject.parseObject(json, StoreRequest.class);
            Store store = new Store();
            BeanUtils.copyProperties(store, request);
            store.setReceiveTime(LocalDateTime.now());
            storeService.updateStoreById(store);
        } catch (Exception e) {
            log.error("Activemq store execute method [store.modify] error, param={}", json, e);
        }
    }

    /**
     * 流量信息同步
     *
     * @param json
     */
    @StreamListener(MQConstant.Q_FLOW_CUSTOMER_SYNCHRONIZATION)
    public void flowSynchronization(String json) {
        try {
            FlowRequest request = JSONObject.parseObject(json, FlowRequest.class);
            request.setId(Constants.companyId);
            flowReportService.update(request);
        } catch (Exception e) {
            log.error("Activemq consumer execute method [customer.synchronization] error, param={}", json, e);
        }
    }
}
