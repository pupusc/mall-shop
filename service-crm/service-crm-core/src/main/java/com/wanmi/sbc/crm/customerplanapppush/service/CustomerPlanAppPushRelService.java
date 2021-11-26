package com.wanmi.sbc.crm.customerplanapppush.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanAppPushVO;
import com.wanmi.sbc.crm.customerplanapppush.model.root.CustomerPlanAppPushRel;
import com.wanmi.sbc.crm.customerplanapppush.repository.CustomerPlanAppPushRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>运营计划App通知业务逻辑</p>
 * @author dyt
 * @date 2020-01-10 11:14:29
 */
@Service("CustomerPlanAppPushRelService")
public class CustomerPlanAppPushRelService {
	@Autowired
	private CustomerPlanAppPushRelRepository customerPlanAppPushRelRepository;

	/**
	 * 新增运营计划App通知
	 * @author dyt
	 */
	@Transactional
	public void add(CustomerPlanAppPushRel entity) {
		customerPlanAppPushRelRepository.save(entity);
	}

	/**
	 * 修改运营计划App通知
	 * @author dyt
	 */
	@Transactional
	public void modify(CustomerPlanAppPushRel entity) {
	    deleteByPlanId(entity.getPlanId());
        add(entity);
	}

	/**
	 * 根据计划id删除运营计划App通知
	 * @author dyt
	 */
	@Transactional
	public void deleteByPlanId(Long planId) {
		customerPlanAppPushRelRepository.deleteByPlanId(planId);
	}

	/**
	 * 根据计划id查询运营计划App通知
	 * @author dyt
	 */
	public CustomerPlanAppPushRel findByPlanId(Long planId){
		return customerPlanAppPushRelRepository.findByPlanId(planId);
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public CustomerPlanAppPushVO wrapperVo(CustomerPlanAppPushRel customerPlanAppPushRel) {
		if (customerPlanAppPushRel != null){
			return KsBeanUtil.convert(customerPlanAppPushRel, CustomerPlanAppPushVO.class);
		}
		return null;
	}
}

