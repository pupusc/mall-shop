package com.wanmi.sbc.account.funds.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsAmountRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailAddRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailModifyRequest;
import com.wanmi.sbc.account.api.request.funds.CustomerFundsDetailPageRequest;
import com.wanmi.sbc.account.bean.enums.FundsStatus;
import com.wanmi.sbc.account.bean.enums.FundsType;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsDetail;
import com.wanmi.sbc.account.funds.repository.CustomerFundsDetailRepository;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 会员资金明细-服务层
 *
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:28
 * @version: 1.0
 */
@Service
public class CustomerFundsDetailService {

    @Autowired
    private CustomerFundsDetailRepository customerFundsDetailRepository;

    /**
     * 会员资金明细分页查询
     *
     * @param request
     * @return
     */
    public Page<CustomerFundsDetail> page(CustomerFundsDetailPageRequest request) {
        return customerFundsDetailRepository.findAll(buildSpecification(request), request.getPageRequest());
    }

    /**
     * 计算收支金额
     * @param request
     * @return
     */
    public BigDecimal getBalanceChange(CustomerFundsAmountRequest request){
        List<FundsType> fundsTypeList = new ArrayList<>();
        if (request.getTabType() == 2) {
            fundsTypeList =  Arrays.asList(FundsType.COMMISSION_WITHDRAWAL, FundsType.BALANCE_PAY);
        }else if(request.getTabType() == 1){
            fundsTypeList =  Arrays.asList(FundsType.INVITE_NEW_AWARDS,
                    FundsType.DISTRIBUTION_COMMISSION,
                    FundsType.COMMISSION_COMMISSION,
                    FundsType.BALANCE_PAY_REFUND);
        }
        return customerFundsDetailRepository.getBalanceChange(request.getCustomerId(), fundsTypeList, request.getStartTime(), request.getEndTime());
    }

    /**
     * 新增会员资金明细
     *
     * @param customerFundsDetailAddRequest
     * @return
     */
    @Transactional
    @GlobalTransactional
    public CustomerFundsDetail add(CustomerFundsDetailAddRequest customerFundsDetailAddRequest) {
        CustomerFundsDetail customerFundsDetail = new CustomerFundsDetail();
        KsBeanUtil.copyPropertiesThird(customerFundsDetailAddRequest, customerFundsDetail);
        return customerFundsDetailRepository.save(customerFundsDetail);
    }

    /**
     * 根据对应条件更新账户明细表数据信息
     *
     * @param customerFundsDetailModifyRequest
     * @return
     */
    public CustomerFundsDetail modifyCustomerFundsDetail(CustomerFundsDetailModifyRequest customerFundsDetailModifyRequest) {
        CustomerFundsDetailPageRequest request = new CustomerFundsDetailPageRequest();
        request.setCustomerId(customerFundsDetailModifyRequest.getCustomerId());
        request.setDrawCashId(customerFundsDetailModifyRequest.getDrawCashId());
        request.setTabType(customerFundsDetailModifyRequest.getTabType());
        CustomerFundsDetail customerFundsDetail = customerFundsDetailRepository.findOne(buildSpecification(request)).orElse(new CustomerFundsDetail());
        customerFundsDetail.setBusinessId(customerFundsDetailModifyRequest.getBusinessId());
        customerFundsDetail.setFundsStatus(customerFundsDetailModifyRequest.getFundsStatus());
        if (customerFundsDetail.getFundsType() == FundsType.COMMISSION_WITHDRAWAL && customerFundsDetail.getFundsStatus() == FundsStatus.YES) {
            customerFundsDetail.setAccountBalance(customerFundsDetail.getAccountBalance().subtract(customerFundsDetail.getReceiptPaymentAmount()));
        }
        return customerFundsDetailRepository.save(customerFundsDetail);
    }

    /**
     * 封装会员资金明细查询条件
     *
     * @param request
     * @return
     */
    private Specification<CustomerFundsDetail> buildSpecification(final CustomerFundsDetailPageRequest request) {
        return (Root<CustomerFundsDetail> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {

            List<Predicate> predicates = new ArrayList<>();


            /**
             * 平台端-余额明细分页列表，切换Tab
             */
            if (CollectionUtils.isNotEmpty(request.getFundsTypeList())) {
                Path path = root.get("fundsType");
                CriteriaBuilder.In in = cb.in(path);
                for (Integer fundsType : request.getFundsTypeList()) {
                    in.value(fundsType);
                }
                predicates.add(in);
            }
            // 批量查询-明细主键List
            if (org.apache.commons.collections4.CollectionUtils.isNotEmpty(request.getCustomerFundsDetailIdList())) {
                predicates.add(root.get("customerFundsDetailId").in(request.getCustomerFundsDetailIdList()));
            }

            //会员ID
            if (StringUtils.isNotBlank(request.getCustomerId())) {
                predicates.add(cb.equal(root.get("customerId"), request.getCustomerId()));
            }

            //资金类型
            if (Objects.nonNull(request.getFundsType()) && !FundsType.ALL.equals(request.getFundsType())) {
                predicates.add(cb.equal(root.get("fundsType"), request.getFundsType().toValue()));
            }

            //资金类型
            if (Objects.nonNull(request.getFundsStatus()) && FundsStatus.YES.equals(request.getFundsStatus())) {
                predicates.add(cb.equal(root.get("fundsStatus"), request.getFundsStatus().toValue()));
            }

            //业务编号
            if (StringUtils.isNotBlank(request.getBusinessId())) {
                predicates.add(cb.equal(root.get("businessId"), request.getBusinessId()));
            }

            //佣金提现id
            if (StringUtils.isNotBlank(request.getDrawCashId())) {
                predicates.add(cb.equal(root.get("drawCashId"), request.getDrawCashId()));
            }

            //入账开始时间
            if (StringUtils.isNotBlank(request.getStartTime())) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createTime"), DateUtil.parseDay(request.getStartTime())));
            }

            //入账结束时间
            if (StringUtils.isNotBlank(request.getEndTime())) {
                predicates.add(cb.lessThan(root.get("createTime"), DateUtil.parseDay(request.getEndTime()).plusDays(1)));
            }

            return cb.and(predicates.toArray(new Predicate[]{}));
        };
    }
}
