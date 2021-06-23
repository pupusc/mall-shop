package com.wanmi.sbc.customer.invoice.service;

import com.wanmi.sbc.common.base.BaseQueryResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.request.customer.CustomerDetailListByConditionRequest;
import com.wanmi.sbc.customer.bean.enums.CheckState;
import com.wanmi.sbc.customer.bean.enums.InvalidFlag;
import com.wanmi.sbc.customer.detail.model.root.CustomerDetail;
import com.wanmi.sbc.customer.invoice.model.entity.CustomerInvoiceQueryRequest;
import com.wanmi.sbc.customer.invoice.model.entity.CustomerInvoiceSaveRequest;
import com.wanmi.sbc.customer.invoice.model.entity.InvoiceBatchRequest;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoice;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoiceInfoResponse;
import com.wanmi.sbc.customer.invoice.model.root.CustomerInvoiceResponse;
import com.wanmi.sbc.customer.invoice.repository.CustomerInvoiceRepository;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.setting.api.provider.AuditProvider;
import com.wanmi.sbc.setting.api.provider.AuditQueryProvider;
import com.wanmi.sbc.setting.api.request.InvoiceConfigModifyRequest;
import com.wanmi.sbc.setting.api.response.InvoiceConfigGetResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 会员增专票信息服务层
 * Created by CHENLI on 2017/4/21.
 */
@Service
@Transactional(readOnly = true, timeout = 10)
public class CustomerInvoiceService {

    @Autowired
    private CustomerInvoiceRepository customerInvoiceRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuditProvider auditProvider;

    @Autowired
    private AuditQueryProvider auditQueryProvider;

    /**
     * 分页查询会员增专票信息
     *
     * @param queryRequest queryRequest
     * @return BaseQueryResponse<CustomerInvoiceResponse>
     */
    public BaseQueryResponse<CustomerInvoiceResponse> page(CustomerInvoiceQueryRequest queryRequest) {
        BaseQueryResponse<CustomerInvoiceResponse> baseQueryResponse = new BaseQueryResponse<>();
        List<CustomerDetail> customerDetailList = new ArrayList<>();
        //根据条件查询客户ID
        if ((Objects.nonNull(queryRequest.getCustomerName()) && StringUtils.isNotBlank(queryRequest.getCustomerName()
                .trim())) || StringUtils.isNotBlank(queryRequest.getEmployeeId()) || CollectionUtils.isNotEmpty(queryRequest.getEmployeeIds())) {
            CustomerDetailListByConditionRequest detailQueryRequest = new CustomerDetailListByConditionRequest();

            detailQueryRequest.setCustomerName(queryRequest.getCustomerName());
            detailQueryRequest.setEmployeeIds(queryRequest.getEmployeeIds());
            customerDetailList = customerService.findDetailByCondition(detailQueryRequest).orElse(null);

            if (CollectionUtils.isEmpty(customerDetailList)) {
                baseQueryResponse.setData(Collections.emptyList());
                baseQueryResponse.setTotal(0L);
                baseQueryResponse.setPageSize(queryRequest.getPageSize());
                return baseQueryResponse;
            } else {
                queryRequest.setCustomerIds(customerDetailList.stream().map(CustomerDetail::getCustomerId).collect
                        (Collectors.toList()));
            }
        }
        //查询增专资质信息
        Page<CustomerInvoice> customerInvoices = customerInvoiceRepository.findAll(queryRequest.getWhereCriteria(),
                queryRequest.getPageRequest());
        if (Objects.isNull(customerInvoices) || CollectionUtils.isEmpty(customerInvoices.getContent())) {
            baseQueryResponse.setData(Collections.emptyList());
            baseQueryResponse.setTotal(0L);
            baseQueryResponse.setPageSize(queryRequest.getPageSize());
            return baseQueryResponse;
        }
        //查询客户信息
        if (CollectionUtils.isEmpty(customerDetailList)) {
            List<CustomerInvoice> customerInvoiceList = customerInvoices.getContent();
            CustomerDetailListByConditionRequest detailQueryRequest = new CustomerDetailListByConditionRequest();
            detailQueryRequest.setCustomerIds(customerInvoiceList.stream().map(CustomerInvoice::getCustomerId)
                    .collect(Collectors.toList()));
            customerDetailList = customerService.findDetailByCondition(detailQueryRequest).orElse(null);
        }

        //拼接返回的数据
        List<CustomerDetail> finalCustomerDetailList = customerDetailList;
        List<CustomerInvoiceResponse> invoiceResponses = customerInvoices.getContent().parallelStream().map
                (customerInvoice -> {
            CustomerInvoiceResponse invoiceResponse = new CustomerInvoiceResponse();
            BeanUtils.copyProperties(customerInvoice, invoiceResponse);
            return invoiceResponse;
        }).collect(Collectors.toList());

        IteratorUtils.zip(invoiceResponses, finalCustomerDetailList
                , (customerInvoiceResponse, customerDetail) -> customerInvoiceResponse.getCustomerId().equals
                        (customerDetail.getCustomerId())
                , (customerInvoiceResponse1, customerDetail1) -> {
                    customerInvoiceResponse1.setCustomerName(customerDetail1.getCustomerName());
                });

        baseQueryResponse.setData(invoiceResponses);
        baseQueryResponse.setTotal(customerInvoices.getTotalElements());
        baseQueryResponse.setPageSize(customerInvoices.getSize());
        return baseQueryResponse;
    }

    /**
     * 根据会员ID查询客户的增专票信息
     *
     * @param customerId
     * @return
     */
    public Optional<CustomerInvoice> findByCustomerIdAndDelFlag(String customerId) {
        return customerInvoiceRepository.findByCustomerId(customerId);
    }


    /**
     * 根据会员ID查询客户已通过审核增专票信息
     *
     * @param customerId
     * @return
     */
    public Optional<CustomerInvoice> findByCustomerIdAndCheckState(String customerId) {
        return customerInvoiceRepository.findByCustomerIdAndCheckState(customerId, CheckState.CHECKED);
    }

    /**
     * 通过客户ID查询未被删除的客户增专票信息，不管状态
     *
     * @param customerId
     * @return
     */
    public Optional<CustomerInvoice> findCustomerInvoiceByCustomerId(String customerId) {
        return customerInvoiceRepository.findByCustomerIdAndDelFlag(customerId, DeleteFlag.NO);
    }

    /**
     * 通过客户ID查询是否有赠品资质
     *
     * @param customerId
     * @return
     */
    public CustomerInvoiceInfoResponse findByCustomerId(String customerId) {
        CustomerInvoiceInfoResponse infoResponse = new CustomerInvoiceInfoResponse();
        CustomerInvoiceResponse customerInvoiceResponse = new CustomerInvoiceResponse();
        customerInvoiceRepository.findByCustomerId(customerId).ifPresent(customerInvoice -> {
            infoResponse.setFlag(Boolean.TRUE);
            BeanUtils.copyProperties(customerInvoice, customerInvoiceResponse);
        });
        infoResponse.setCustomerInvoiceResponse(customerInvoiceResponse);

        return infoResponse;
    }

    /**
     * 根据增专票ID查询客户的增专票信息
     *
     * @param customerInvoiceId
     * @return
     */
    public Optional<CustomerInvoice> findOne(Long customerInvoiceId) {
        return customerInvoiceRepository.findByCustomerInvoiceIdAndDelFlag(customerInvoiceId, DeleteFlag.NO);
    }

    /**
     * 保存客户的增专票信息
     * boss端新增增票信息，增票状态都是已审核
     * 客户端新增增票信息，增票状态都是待审核
     *
     * @param request
     * @return
     */
    @Transactional
    public Optional<CustomerInvoice> saveCustomerInvoice(CustomerInvoiceSaveRequest request, String employeeId) {
        CustomerInvoice customerInvoice = new CustomerInvoice();
        if (Objects.isNull(request.getCustomerId())) {
            throw new SbcRuntimeException(CustomerErrorCode.CUSTOMER_NOT_EXIST);
        }

        BeanUtils.copyProperties(request, customerInvoice);
        customerInvoice.setCheckState(CheckState.CHECKED);//默认状态为已审核

        //如果操作人是客户自己，则是客户端添加增专票信息
        if (StringUtils.isEmpty(employeeId)) {
            //如果开启审核开关，状态设为待审核
            if (Constants.yes.equals(queryInvoiceConfig().getStatus())) {
                customerInvoice.setCheckState(CheckState.WAIT_CHECK);
            }
            customerInvoice.setCreatePerson(request.getCustomerId());
        } else {
            //boss端添加增专票信息
            customerInvoice.setCreatePerson(employeeId);
            // 用户申请处于待审核状态。boss可直接覆盖用户申请数据
            customerInvoiceRepository.deleteCustomerInvoiceByCustomerIds(Collections.singletonList(request.getCustomerId()));
        }
        customerInvoice.setInvalidFlag(InvalidFlag.NO);
        customerInvoice.setDelFlag(DeleteFlag.NO);
        customerInvoice.setCreateTime(LocalDateTime.now());
        return Optional.ofNullable(customerInvoiceRepository.save(customerInvoice));
    }

    /**
     * 修改客户的增专票信息
     * boss端修改增票信息，增票状态都是已审核
     * 客户端修改增票信息，增票状态都是待审核
     *
     * @param request
     * @return
     */
    @Transactional
    public CustomerInvoice updateCustomerInvoice(CustomerInvoiceSaveRequest request, String employeeId) {
        CustomerInvoice customerInvoice = customerInvoiceRepository.findById(request.getCustomerInvoiceId()).orElse(null);
        if (Objects.isNull(customerInvoice)){
            return null;
        }
        KsBeanUtil.copyProperties(request, customerInvoice);

        customerInvoice.setCheckState(CheckState.CHECKED);//默认状态为已审核
        customerInvoice.setInvalidFlag(InvalidFlag.NO);// 修改后将作废状态取消
        //如果操作人是客户自己，则是客户端添加增专票信息
        if (StringUtils.isEmpty(employeeId)) {
            //如果开启审核开关，状态设为待审核
            if (Constants.yes.equals(queryInvoiceConfig().getStatus())) {
                customerInvoice.setCheckState(CheckState.WAIT_CHECK);
            }
            customerInvoice.setUpdatePerson(request.getCustomerId());
        } else {
            customerInvoice.setUpdatePerson(employeeId);
        }

        customerInvoice.setUpdateTime(LocalDateTime.now());
        return customerInvoiceRepository.save(customerInvoice);
    }

    /**
     * 单条 / 批量审核 增专票信息
     *
     * @param checkStatus
     * @param customerInvoiceIds
     */
    @Transactional
    public void checkCustomerInvoice(CheckState checkStatus, List<Long> customerInvoiceIds) {
        if (CollectionUtils.isEmpty(customerInvoiceIds) || checkStatus == null) {
            return;
        }
        customerInvoiceRepository.checkCustomerInvoice(checkStatus, customerInvoiceIds);
    }

    /**
     * 驳回增专票信息
     */
    @Transactional
    public void rejectInvoice(InvoiceBatchRequest invoiceBatchRequest) {
        customerInvoiceRepository.rejectInvoice(invoiceBatchRequest.getRejectReason(),
                invoiceBatchRequest.getCustomerInvoiceId(), CheckState.NOT_PASS);
    }

    /**
     * 作废 增专票信息
     *
     * @param customerInvoiceIds
     */
    @Transactional
    public void invalidCustomerInvoice(List<Long> customerInvoiceIds) {
        if (CollectionUtils.isEmpty(customerInvoiceIds)) {
            return;
        }
        customerInvoiceRepository.invalidCustomerInvoice(InvalidFlag.YES, customerInvoiceIds);
    }

    /**
     * 删除 增专票信息
     *
     * @param customerInvoiceIds
     */
    @Transactional
    public void deleteCustomerInvoice(List<Long> customerInvoiceIds) {
        if (CollectionUtils.isEmpty(customerInvoiceIds)) {
            return;
        }
        customerInvoiceRepository.deleteCustomerInvoicesByIds(customerInvoiceIds);
    }

    /**
     * 保存增专资质配置
     *
     * @param status status
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveInvoiceConfig(Integer status) {
//        configRepository.updateStatusByType(ConfigType.TICKETAUDIT.toValue(), status, LocalDateTime.now());
        InvoiceConfigModifyRequest request = new InvoiceConfigModifyRequest();
        request.setStatus(status);

        auditProvider.modifyInvoiceConfig(request);
    }

    /**
     * 查询
     *
     *
     * @return
     */
    private InvoiceConfigGetResponse queryInvoiceConfig() {
//        return configRepository.findByConfigTypeAndDelFlag(ConfigType.TICKETAUDIT.toValue(), DeleteFlag.NO);
        return auditQueryProvider.getInvoiceConfig().getContext();
    }

    /**
     * 根据纳税人识别号查询客户的增专票信息
     *
     * @param taxpayerNumber
     * @return
     */
    public Optional<CustomerInvoice> findByTaxpayerNumberAndDelFlag(String taxpayerNumber) {
        return customerInvoiceRepository.findByTaxpayerNumberAndDelFlag(taxpayerNumber, DeleteFlag.NO);
    }
}
