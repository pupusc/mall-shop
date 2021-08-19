package com.wanmi.sbc.customer.account.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.account.model.root.CustomerAccount;
import com.wanmi.sbc.customer.account.repository.CustomerAccountRepository;
import com.wanmi.sbc.customer.account.request.CustomerAccountSaveRequest;
import com.wanmi.sbc.customer.api.constant.EmployeeErrorCode;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 会员银行账户信息服务
 * Created by CHENLI on 2017/4/18.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class CustomerAccountService {

    @Autowired
    private CustomerAccountRepository customerAccountRepository;

    /**
     * 查询会员的银行账户信息
     *
     * @param customerId
     * @return
     */
    public List<CustomerAccount> findCustomerAccountList(String customerId) {
        List<CustomerAccount> accountList = customerAccountRepository.findCustomerAccountList(customerId);
        if (CollectionUtils.isNotEmpty(accountList)) {
            return accountList;
        }
        return Collections.emptyList();
    }

    /**
     * 根据ID查询银行账户
     *
     * @param customerAccountId
     * @return
     */
    public Optional<CustomerAccount> findById(String customerAccountId) {
        return customerAccountRepository.findByCustomerAccountIdAndDelFlag(customerAccountId, DeleteFlag.NO);
    }

    /**
     * 根据id查询银行账户(不管是否删除)
     *
     * @param customerAccountId
     * @return
     */
    public CustomerAccount findByAccountId(String customerAccountId) {
        return customerAccountRepository.findById(customerAccountId).orElse(null);
    }

    /**
     * 根据银行账号查询银行账户
     *
     * @param customerAccountNo
     * @return
     */
    public Optional<CustomerAccount> findByCustomerAccountNoAndDelFlag(String customerAccountNo) {
        return customerAccountRepository.findByCustomerAccountNoAndDelFlag(customerAccountNo, DeleteFlag.NO);
    }

    /**
     * 查询会员有几个银行账号
     *
     * @param customerId
     * @return
     */
    public Integer countCustomerAccount(String customerId) {
        return customerAccountRepository.countCustomerAccount(customerId);
    }

    /**
     * 新增会员银行账户
     *
     * @param accountSaveRequest
     */
    @Transactional
    public CustomerAccount addCustomerAccount(CustomerAccountSaveRequest accountSaveRequest, String employeeId) {
        if (findByCustomerAccountNoAndDelFlag(accountSaveRequest.getCustomerAccountNo()).isPresent()) {
            throw new SbcRuntimeException(EmployeeErrorCode.BANK_ACCOUNT_EXIST);
        }
        CustomerAccount customerAccount = new CustomerAccount();
        BeanUtils.copyProperties(accountSaveRequest, customerAccount);
        customerAccount.setDelFlag(DeleteFlag.NO);
        customerAccount.setCreatePerson(employeeId);
        customerAccount.setCreateTime(LocalDateTime.now());
        return customerAccountRepository.save(customerAccount);
    }

    /**
     * boss端编辑会员银行账户
     *
     * @param accountSaveRequest
     */
    @Transactional
    public CustomerAccount editCustomerAccount(CustomerAccountSaveRequest accountSaveRequest, String employeeId) {
        CustomerAccount customerAccount =
                customerAccountRepository.findById(accountSaveRequest.getCustomerAccountId()).orElse(null);
        KsBeanUtil.copyProperties(accountSaveRequest, customerAccount);
        customerAccount.setUpdatePerson(employeeId);
        customerAccount.setUpdateTime(LocalDateTime.now());
        return customerAccountRepository.save(customerAccount);
    }

    /**
     * boss端删除会员银行账户
     *
     * @param customerAccountId
     */
    @Transactional
    public int deleteCustomerAccount(String customerAccountId, String employeeId) {
        return customerAccountRepository.deleteCustomerAccount(customerAccountId, employeeId, LocalDateTime.now());
    }

    /**
     * 客户端编辑会员银行账户
     *
     * @param accountSaveRequest
     */
    @Transactional
    public int updateCustomerAccount(CustomerAccountSaveRequest accountSaveRequest, String customerId) {
        return customerAccountRepository.updateCustomerAccount(accountSaveRequest, customerId, LocalDateTime.now());
    }

    /**
     * 客户端删除会员银行账户
     *
     * @param customerAccountId
     */
    @Transactional
    public int deleteCustomerAccountById(String customerAccountId, String customerId) {
        return customerAccountRepository.deleteCustomerAccountById(customerAccountId, customerId, LocalDateTime.now());
    }
}
