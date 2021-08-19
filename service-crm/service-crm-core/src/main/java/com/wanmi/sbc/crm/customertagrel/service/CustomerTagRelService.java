package com.wanmi.sbc.crm.customertagrel.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.crm.customertagrel.repository.CustomerTagRelRepository;
import com.wanmi.sbc.crm.customertagrel.model.root.CustomerTagRel;
import com.wanmi.sbc.crm.api.request.customertagrel.CustomerTagRelQueryRequest;
import com.wanmi.sbc.crm.bean.vo.CustomerTagRelVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>会员标签关联业务逻辑</p>
 * @author dyt
 * @date 2019-11-12 14:49:08
 */
@Service("CustomerTagRelService")
public class CustomerTagRelService {
	@Autowired
	private CustomerTagRelRepository customerTagRelRepository;
	
	/** 
	 * 新增会员标签关联
	 * @author dyt
	 */
	@Transactional
	public void add(List<CustomerTagRel> entity) {
	    List<CustomerTagRel> rels =
                this.list(CustomerTagRelQueryRequest.builder().customerIds(entity.stream()
                        .map(CustomerTagRel::getCustomerId).collect(Collectors.toList())).build());
	    if(CollectionUtils.isNotEmpty(rels)){
	        customerTagRelRepository.deleteAll(rels);
        }
		customerTagRelRepository.saveAll(entity);
	}
	
	/** 
	 * 修改会员标签关联
	 * @author dyt
	 */
	@Transactional
	public CustomerTagRel modify(CustomerTagRel entity) {
		customerTagRelRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除会员标签关联
	 * @author dyt
	 */
	@Transactional
	public void deleteById(Long id) {
		customerTagRelRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除会员标签关联
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> customerTagRelRepository.deleteById(id));
	}


	public void deleteByTagId(Long tagId){
		customerTagRelRepository.deleteByTagId(tagId);
	}

	@Transactional
	public void deleteByCustomerId(String customerId){
		customerTagRelRepository.deleteByCustomerId(customerId);
	}
	/** 
	 * 单个查询会员标签关联
	 * @author dyt
	 */
	public CustomerTagRel getById(Long id){
		return customerTagRelRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询会员标签关联
	 * @author dyt
	 */
	public Page<CustomerTagRel> page(CustomerTagRelQueryRequest queryReq){
		return customerTagRelRepository.findAll(
				CustomerTagRelWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询会员标签关联
	 * @author dyt
	 */
	public List<CustomerTagRel> list(CustomerTagRelQueryRequest queryReq){
		return customerTagRelRepository.findAll(
				CustomerTagRelWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public CustomerTagRelVO wrapperVo(CustomerTagRel customerTagRel) {
		if (customerTagRel != null){
			CustomerTagRelVO customerTagRelVO=new CustomerTagRelVO();
			KsBeanUtil.copyPropertiesThird(customerTagRel,customerTagRelVO);
			return customerTagRelVO;
		}
		return null;
	}
}
