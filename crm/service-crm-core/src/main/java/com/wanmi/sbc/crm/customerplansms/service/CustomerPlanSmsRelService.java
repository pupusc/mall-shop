package com.wanmi.sbc.crm.customerplansms.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.bean.vo.CustomerPlanSmsVO;
import com.wanmi.sbc.crm.customerplansms.model.root.CustomerPlanSmsRel;
import com.wanmi.sbc.crm.customerplansms.repository.CustomerPlanSmsRelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>运营计划与短信关联业务逻辑</p>
 * @author dyt
 * @date 2020-01-10 11:12:50
 */
@Service("CustomerPlanSmsRelService")
public class CustomerPlanSmsRelService {
	@Autowired
	private CustomerPlanSmsRelRepository customerPlanSmsRelRepository;

	/**
	 * 新增运营计划与短信关联
	 * @author dyt
	 */
	@Transactional
	public void add(CustomerPlanSmsRel entity) {
		customerPlanSmsRelRepository.save(entity);
	}

	/**
	 * 修改运营计划与短信关联
	 * @author dyt
	 */
	@Transactional
	public void modify(CustomerPlanSmsRel entity) {
        deleteByPlanId(entity.getPlanId());
        add(entity);
	}

	/**
	 * 根据计划id删除运营计划与短信关联
	 * @author dyt
	 */
	@Transactional
	public void deleteByPlanId(Long planId) {
		customerPlanSmsRelRepository.deleteByPlanId(planId);
	}

	/**
	 * 根据计划id查询运营计划与短信关联
	 * @author dyt
	 */
    public CustomerPlanSmsRel findByPlanId(Long planId) {
        return customerPlanSmsRelRepository.findByPlanId(planId);
    }

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public CustomerPlanSmsVO wrapperVo(CustomerPlanSmsRel customerPlanSmsRel) {
		if (customerPlanSmsRel != null){
			return KsBeanUtil.convert(customerPlanSmsRel, CustomerPlanSmsVO.class);
		}
		return null;
	}
}

