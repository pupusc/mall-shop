package com.wanmi.sbc.crm.customertag.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.customertag.CustomerTagQueryRequest;
import com.wanmi.sbc.crm.bean.vo.CustomerTagVO;
import com.wanmi.sbc.crm.customertag.model.root.CustomerTag;
import com.wanmi.sbc.crm.customertag.repository.CustomerTagRepository;
import com.wanmi.sbc.crm.customertagrel.service.CustomerTagRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>会员标签业务逻辑</p>
 * @author zhanglingke
 * @date 2019-10-14 11:19:11
 */
@Service("CustomerTagService")
public class CustomerTagService {
	@Autowired
	private CustomerTagRepository customerTagRepository;
	@Autowired
	private CustomerTagRelService customerTagRelService;
	
	/** 
	 * 新增会员标签
	 * @author zhanglingke
	 */
	@Transactional
	public CustomerTag add(CustomerTag entity) {
		customerTagRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改会员标签
	 * @author zhanglingke
	 */
	@Transactional
	public CustomerTag modify(CustomerTag entity) {
		customerTagRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除会员标签
	 * @author zhanglingke
	 */
	@Transactional
	public void deleteById(Long id) {
		customerTagRepository.deleteByBeanId(id);
		customerTagRelService.deleteByTagId(id);
	}
	
	/** 
	 * 批量删除会员标签
	 * @author zhanglingke
	 *//*
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		customerTagRepository.deleteByIdList(ids);
	}
	*/
	/** 
	 * 单个查询会员标签
	 * @author zhanglingke
	 */
	public CustomerTag getById(Long id){
		return customerTagRepository.findById(id).orElse(null);
	}

	/**
	 * 通过名称单个查询为删除会员标签
	 * @author zhanglingke
	 */
	public Optional<CustomerTag> getByName(String name){
		return customerTagRepository.findByNameAndDelFlag(name, DeleteFlag.NO);
	}

	/** 
	 * 分页查询会员标签
	 * @author zhanglingke
	 */
	public Page<CustomerTag> page(CustomerTagQueryRequest queryReq){
		return customerTagRepository.findAll(
				CustomerTagWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询会员标签
	 * @author zhanglingke
	 */
	public List<CustomerTag> list(CustomerTagQueryRequest queryReq){
		return customerTagRepository.findAll(
				CustomerTagWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	public int findCountByIdListAndDelFlag(List<Long> ids){
		return customerTagRepository.findCountByIdListAndDelFlag(ids,DeleteFlag.NO);
	}

	/**
	 * 将实体包装成VO
	 * @author zhanglingke
	 */
	public CustomerTagVO wrapperVo(CustomerTag customerTag) {
		if (customerTag != null){
			CustomerTagVO customerTagVO=new CustomerTagVO();
			KsBeanUtil.copyPropertiesThird(customerTag,customerTagVO);
			return customerTagVO;
		}
		return null;
	}
}
