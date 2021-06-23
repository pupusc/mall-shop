package com.wanmi.sbc.order.customer.service;

import com.wanmi.sbc.account.api.provider.offline.OfflineQueryProvider;
import com.wanmi.sbc.account.api.request.offline.OfflineAccountListWithoutDeleteFlagByBankNoRequest;
import com.wanmi.sbc.account.bean.vo.OfflineAccountVO;
import com.wanmi.sbc.customer.api.provider.company.CompanyInfoQueryProvider;
import com.wanmi.sbc.customer.api.provider.detail.CustomerDetailQueryProvider;
import com.wanmi.sbc.customer.api.request.company.CompanyInfoByIdRequest;
import com.wanmi.sbc.customer.api.request.company.CompanyListRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByCustomerIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailByIdRequest;
import com.wanmi.sbc.customer.api.request.detail.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.customer.bean.vo.CustomerDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 客户信息支持服务
 * Created by daiyitian on 2017/4/20.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class CustomerCommonService {

    @Autowired
    private CustomerDetailQueryProvider customerDetailQueryProvider;

    @Autowired
    private CompanyInfoQueryProvider companyInfoQueryProvider;

    @Autowired
    private OfflineQueryProvider offlineQueryProvider;


    public List<CustomerDetailVO> listCustomerDetailByCondition(CustomerDetailListByConditionRequest request) {
        List<CustomerDetailVO> voList = customerDetailQueryProvider.listCustomerDetailByCondition(request).getContext()
                .getCustomerDetailVOList();
        return Objects.nonNull(voList) ? voList : Collections.EMPTY_LIST;
    }

    public CustomerDetailVO getCustomerDetailById(String customerDetailId) {
        return customerDetailQueryProvider.getCustomerDetailById(
                CustomerDetailByIdRequest.builder().customerDetailId(customerDetailId).build()).getContext();
    }

    public CustomerDetailVO getAnyCustomerDetailById(String customerDetailId) {
        return customerDetailQueryProvider.listCustomerDetailByCondition(
                CustomerDetailListByConditionRequest.builder().customerDetailId(customerDetailId).build()).getContext
                ().getCustomerDetailVOList().stream().findFirst().orElseGet(null);
    }


    public CustomerDetailVO getCustomerDetailByCustomerId(String customerId) {
        return customerDetailQueryProvider.getCustomerDetailByCustomerId(
                CustomerDetailByCustomerIdRequest.builder().customerId(customerId).build()).getContext();
    }

    public List<String> listCustomerIdsByCondition(CustomerDetailListByConditionRequest request) {
        return this.listCustomerDetailByCondition(request).stream().map(CustomerDetailVO::getCustomerId).collect
                (Collectors.toList());
    }

    public List<String> listCustomerDetailIdsByCondition(CustomerDetailListByConditionRequest request) {
        return this.listCustomerDetailByCondition(request).stream().map(CustomerDetailVO::getCustomerDetailId).collect
                (Collectors.toList());
    }

    public List<CompanyInfoVO> listCompanyInfoByCondition(CompanyListRequest request) {
        List<CompanyInfoVO> voList = companyInfoQueryProvider.listCompanyInfo(request).getContext()
                .getCompanyInfoVOList();
        return Objects.nonNull(voList) ? voList :  Collections.EMPTY_LIST;
    }

    public CompanyInfoVO getCompanyInfoById(Long companyInfoId) {
        return companyInfoQueryProvider.getCompanyInfoById(
                CompanyInfoByIdRequest.builder().companyInfoId(companyInfoId).build()).getContext();
    }

    public List<Long> listCompanyInfoIdsByCondition(CompanyListRequest request) {
        return this.listCompanyInfoByCondition(request).stream().map(CompanyInfoVO::getCompanyInfoId).collect
                (Collectors.toList());
    }

    public List<Long> listOfflineAccountIdsByBankNo(OfflineAccountListWithoutDeleteFlagByBankNoRequest request) {
        return this.listOfflineAccountByBankNo(request).stream().map(OfflineAccountVO::getAccountId).collect(Collectors
                .toList());
    }

    public List<OfflineAccountVO> listOfflineAccountByBankNo(OfflineAccountListWithoutDeleteFlagByBankNoRequest request) {
        return offlineQueryProvider.listWithOutDeleteFlagByBankNo(request).getContext().getVoList();

    }
}
