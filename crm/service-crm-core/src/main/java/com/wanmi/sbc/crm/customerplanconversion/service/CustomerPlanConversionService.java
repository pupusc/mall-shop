package com.wanmi.sbc.crm.customerplanconversion.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.customerplanconversion.CustomerPlanConversionQueryRequest;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanConversionVO;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanMapper;
import com.wanmi.sbc.crm.customerplan.mapper.CustomerPlanSendMapper;
import com.wanmi.sbc.crm.customerplanconversion.model.root.CustomerPlanConversion;
import com.wanmi.sbc.crm.customerplanconversion.repository.CustomerPlanConversionRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>运营计划转化效果业务逻辑</p>
 *
 * @author zhangwenchang
 * @date 2020-02-12 00:16:50
 */
@Service("CustomerPlanConversionService")
public class CustomerPlanConversionService {
    @Autowired
    private CustomerPlanConversionRepository customerPlanConversionRepository;

    @Autowired
    private CustomerPlanSendMapper customerPlanSendMapper;

    @Autowired
    private CustomerPlanMapper customerPlanMapper;

    /**
     * 新增运营计划转化效果
     *
     * @author zhangwenchang
     */
    @Transactional
    public CustomerPlanConversion add(CustomerPlanConversion entity) {
        customerPlanConversionRepository.save(entity);
        return entity;
    }

    /**
     * 修改运营计划转化效果
     *
     * @author zhangwenchang
     */
    @Transactional
    public CustomerPlanConversion modify(CustomerPlanConversion entity) {
        customerPlanConversionRepository.save(entity);
        return entity;
    }

    /**
     * 单个删除运营计划转化效果
     *
     * @author zhangwenchang
     */
    @Transactional
    public void deleteById(Long id) {
        customerPlanConversionRepository.deleteById(id);
    }

    /**
     * 批量删除运营计划转化效果
     *
     * @author zhangwenchang
     */
    @Transactional
    public void deleteByIdList(List<Long> ids) {
        ids.forEach(id -> customerPlanConversionRepository.deleteById(id));
    }

    /**
     * 单个查询运营计划转化效果
     *
     * @author zhangwenchang
     */
    public CustomerPlanConversion getById(Long id) {
        return customerPlanConversionRepository.getOne(id);
    }

    /**
     * 分页查询运营计划转化效果
     *
     * @author zhangwenchang
     */
    public Page<CustomerPlanConversion> page(CustomerPlanConversionQueryRequest queryReq) {
        return customerPlanConversionRepository.findAll(
                CustomerPlanConversionWhereCriteriaBuilder.build(queryReq),
                queryReq.getPageRequest());
    }

    /**
     * 列表查询运营计划转化效果
     *
     * @author zhangwenchang
     */
    public List<CustomerPlanConversion> list(CustomerPlanConversionQueryRequest queryReq) {
        return customerPlanConversionRepository.findAll(
                CustomerPlanConversionWhereCriteriaBuilder.build(queryReq),
                queryReq.getSort());
    }

    /**
     * 单个查询运营计划转化效果
     *
     * @author zhangwenchang
     */
    public CustomerPlanConversion getByPlanId(Long planId) {
        return customerPlanConversionRepository.findTopByPlanId(planId);
    }

    /**
     * 将实体包装成VO
     *
     * @author zhangwenchang
     */
    public CustomerPlanConversionVO wrapperVo(CustomerPlanConversion customerPlanConversion) {
        if (customerPlanConversion != null) {
            CustomerPlanConversionVO customerPlanConversionVO = new CustomerPlanConversionVO();
            KsBeanUtil.copyPropertiesThird(customerPlanConversion, customerPlanConversionVO);
            return customerPlanConversionVO;
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    public void generator() {
        List<Long> planIds = customerPlanMapper.selectPlanningIds();
        if (CollectionUtils.isNotEmpty(planIds)) {
            planIds.forEach(id -> {
                //访客数UV
                Long visitorsUvCount = customerPlanSendMapper.selectVisitorsUvCount(id);
                //下单人数
                Long orderPersonCount = customerPlanSendMapper.selectOrderPersonCount(id);
                //下单笔数
                Long orderCount = customerPlanSendMapper.selectOrderCount(id);
                //付款人数
                Long payPersonCount = customerPlanSendMapper.selectPayPersonCount(id);
                //付款笔数
                Long payCount = customerPlanSendMapper.selectPayCount(id);
                //付款金额
                BigDecimal totalPrice = customerPlanSendMapper.selectTotalPrice(id);
                //客单价
                BigDecimal unitPrice = BigDecimal.ZERO;
                if (payPersonCount.compareTo(0L) > 0) {
                    unitPrice = totalPrice.divide(BigDecimal.valueOf(payPersonCount), 2,
                            BigDecimal.ROUND_HALF_UP);
                }
                //覆盖人数
                Long coversCount = customerPlanSendMapper.selectCoversCount(id);
                //访客人数
                Long visitorsCount = customerPlanSendMapper.selectVisitorsCount(id);
                //访客人数/覆盖人数转换率
                Double coversVisitorsRate = 0d;
                if (coversCount.compareTo(0L) > 0) {
                    coversVisitorsRate = ((double) visitorsCount / (double) coversCount) * 100;
                    coversVisitorsRate = (double) Math.round(coversVisitorsRate * 100) / 100;
                }
                //付款人数/访客人数转换率
                Double payVisitorsRate = 0d;
                if (visitorsCount.compareTo(0L) > 0) {
                    payVisitorsRate = ((double) payPersonCount / (double) visitorsCount) * 100;
                    payVisitorsRate = (double) Math.round(payVisitorsRate * 100) / 100;
                }
                //付款人数/覆盖人数转换率
                Double payCoversRate = 0d;
                if (coversCount.compareTo(0L) > 0) {
                    payCoversRate = ((double) payPersonCount / (double) coversCount) * 100;
                    payCoversRate = (double) Math.round(payCoversRate * 100) / 100;
                }
                //删除原有的统计
                customerPlanConversionRepository.deleteByPlanId(id);
                //重新加入新的统计结果
                CustomerPlanConversion customerPlanConversion = new CustomerPlanConversion();
                customerPlanConversion.setPlanId(id);
                customerPlanConversion.setVisitorsUvCount(visitorsUvCount);
                customerPlanConversion.setOrderPersonCount(orderPersonCount);
                customerPlanConversion.setOrderCount(orderCount);
                customerPlanConversion.setPayPersonCount(payPersonCount);
                customerPlanConversion.setPayCount(payCount);
                customerPlanConversion.setTotalPrice(totalPrice);
                customerPlanConversion.setUnitPrice(unitPrice);
                customerPlanConversion.setCoversCount(coversCount);
                customerPlanConversion.setVisitorsCount(visitorsCount);
                customerPlanConversion.setCoversVisitorsRate(coversVisitorsRate);
                customerPlanConversion.setPayVisitorsRate(payVisitorsRate);
                customerPlanConversion.setPayCoversRate(payCoversRate);
                customerPlanConversion.setCreateTime(LocalDateTime.now());
                customerPlanConversionRepository.save(customerPlanConversion);
            });
        }
    }
}
