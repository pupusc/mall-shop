package com.wanmi.sbc.crm.customerplancoupon.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanCouponVO;
import com.wanmi.sbc.crm.customerplancoupon.model.root.CustomerPlanCouponRel;
import com.wanmi.sbc.crm.customerplancoupon.repository.CustomerPlanCouponRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>运营计划与优惠券关联业务逻辑</p>
 * @author dyt
 * @date 2020-01-08 14:11:18
 */
@Service("CustomerPlanCouponRelService")
public class CustomerPlanCouponRelService {

	@Autowired
	private CustomerPlanCouponRelRepository customerPlanCouponRelRepository;

	/**
	 * 根据计划新增优惠券信息
	 * @author dyt
	 */
	@Transactional
	public void add(List<CustomerPlanCouponRel> entity) {
		customerPlanCouponRelRepository.saveAll(entity);
	}

	/**
	 * 根据计划更新优惠券信息
	 * @author dyt
	 */
	@Transactional
    public void modify(List<CustomerPlanCouponRel> entity) {
        entity.stream().map(CustomerPlanCouponRel::getPlanId).distinct()
                .forEach(id -> customerPlanCouponRelRepository.deleteByPlanId(id));
        customerPlanCouponRelRepository.saveAll(entity);
    }

    /**
     * 根据计划删除优惠券信息
     * @author dyt
     */
    @Transactional
    public void deleteByPlanId(Long planId) {
        customerPlanCouponRelRepository.deleteByPlanId(planId);
    }


	/**
	 * 根据计划查询优惠券
	 * @author dyt
	 */
	public List<CustomerPlanCouponRel> listByPlanIds(List<Long> planIds){
		return customerPlanCouponRelRepository.findAllByPlanIdIn(planIds);
	}

    /**
     * 将实体包装成VO
     * @author dyt
     */
    public CustomerPlanCouponVO wrapperVo(CustomerPlanCouponRel customerPlanCouponRel) {
        if (customerPlanCouponRel != null){
            return KsBeanUtil.convert(customerPlanCouponRel, CustomerPlanCouponVO.class);
        }
        return null;
    }
}

