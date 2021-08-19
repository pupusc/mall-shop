package com.wanmi.sbc.customer.email.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerEmailErrorCode;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailAddRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailDeleteRequest;
import com.wanmi.sbc.customer.api.request.email.CustomerEmailModifyRequest;
import com.wanmi.sbc.customer.email.model.root.CustomerEmail;
import com.wanmi.sbc.customer.email.repository.CustomerEmailRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 客户邮箱配置服务类
 */
@Service
public class CustomerEmailService {

    @Autowired
    private CustomerEmailRepository customerEmailRepository;

    private static Integer MAX_CUSTOMER_EMAIL_COUNT = 5;

    /**
     * 根据客户ID查询客户的财务邮箱列表
     *
     * @param customerId
     * @return
     */
//    public List<CustomerEmailResponse> listCustomerEmailByCustomerId(String customerId) {
//        // 客户id不能为空
//        if (StringUtils.isBlank(customerId)) {
//            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
//        } else {
//            List<CustomerEmail> source = customerEmailRepository
//                    .findCustomerEmailsByCustomerIdAndDelFlagOrderByCreateTime(customerId, DeleteFlag.NO);
//            if (CollectionUtils.isEmpty(source)) {
//                return new ArrayList<>();
//            }
//            List<CustomerEmailResponse> target = source.stream().map((email) -> {
//                CustomerEmailResponse response = new CustomerEmailResponse();
//                KsBeanUtil.copyProperties(email, response);
//                return response;
//            }).collect(Collectors.toList());
//            return target;
//        }
//    }
    public List<CustomerEmail> listCustomerEmailByCustomerId(String customerId) {
        // 客户id不能为空
        if (StringUtils.isBlank(customerId)) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        } else {
            List<CustomerEmail> source = customerEmailRepository
                    .findCustomerEmailsByCustomerIdAndDelFlagOrderByCreateTime(customerId, DeleteFlag.NO);
            if (CollectionUtils.isEmpty(source)) {
                return new ArrayList<>();
            }

            return source;
        }
    }

    /**
     * 根据客户邮箱ID删除财务邮箱
     *
     * @param request
     */
    @Transactional
    public void deleteCustomerEmailByCustomerEmailId(CustomerEmailDeleteRequest request) {
        // 客户邮箱id不能为空
        if (StringUtils.isBlank(request.getCustomerEmailId())) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        customerEmailRepository.deleteCustomerEmailByCustomerEmailId(request.getCustomerEmailId(),
                request.getDelPerson(),
                request.getDelTime());
    }

    /**
     * 新增客户财务邮箱
     *
     * @param request
     */
    @Transactional
    public CustomerEmail addCustomerEmail(CustomerEmailAddRequest request) {
        if (StringUtils.isEmpty(request.getEmailAddress()) || request.getEmailAddress().length() > 32) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 根据客户id查询财务邮箱
        List<CustomerEmail> customerEmails = customerEmailRepository
                .findCustomerEmailsByCustomerIdAndDelFlagOrderByCreateTime(request.getCustomerId(), DeleteFlag.NO);
        if (CollectionUtils.isNotEmpty(customerEmails)) {
            // 客户财务邮箱数量不能超过5
            if (customerEmails.size() > MAX_CUSTOMER_EMAIL_COUNT) {
                throw new SbcRuntimeException(CustomerEmailErrorCode.CUSTOMER_EMAIL_ALREADY_MAX);
            }
            // 客户财务邮箱名称不能已经存在
            if (customerEmails.stream().anyMatch(customerEmail ->
                    StringUtils.equals(request.getEmailAddress(), customerEmail.getEmailAddress()))) {
                throw new SbcRuntimeException(CustomerEmailErrorCode.CUSTOMER_EMAIL_ADDRESS_EXIST);
            }
        }
        CustomerEmail customerEmail = new CustomerEmail();
        KsBeanUtil.copyProperties(request, customerEmail);
        customerEmail.setDelFlag(DeleteFlag.NO);
        customerEmail = customerEmailRepository.save(customerEmail);

        return customerEmail;
    }

    /**
     * 修改客户财务邮箱
     *
     * @param request
     * @return
     */
    @Transactional
    public CustomerEmail modifyCustomerEmail(CustomerEmailModifyRequest request) {
        if (request.getCustomerEmailId() == null || StringUtils.isEmpty(request.getEmailAddress())
                || request.getEmailAddress().length() > 32) {
            throw new SbcRuntimeException(CommonErrorCode.PARAMETER_ERROR);
        }
        // 根据邮箱id查询详情信息
        CustomerEmail customerEmail = customerEmailRepository.findCustomerEmailByCustomerEmailIdAndDelFlag(
                request.getCustomerEmailId(), DeleteFlag.NO);
        // 客户财务邮箱不存在
        if (Objects.isNull(customerEmail)) {
            throw new SbcRuntimeException(CustomerEmailErrorCode.CUSTOMER_EMAIL_NOT_EXIST);
        }
        // 除自身外, 客户财务邮箱名称不能重复
        if (Objects.nonNull(customerEmailRepository.findByEmailAddressNotSelf(
                request.getCustomerEmailId(), request.getCustomerId(), request.getEmailAddress()))) {
            throw new SbcRuntimeException(CustomerEmailErrorCode.CUSTOMER_EMAIL_ADDRESS_EXIST);
        }
        KsBeanUtil.copyProperties(request, customerEmail);
        customerEmail = customerEmailRepository.save(customerEmail);

        return customerEmail;
    }

}
