package com.wanmi.sbc.setting.businessconfig.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.businessconfig.repository.BusinessConfigRepository;
import com.wanmi.sbc.setting.businessconfig.model.root.BusinessConfig;
import com.wanmi.sbc.setting.api.request.businessconfig.BusinessConfigQueryRequest;
import com.wanmi.sbc.setting.bean.vo.BusinessConfigVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>招商页设置业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:09:10
 */
@Service("BusinessConfigService")
public class BusinessConfigService {
	@Autowired
	private BusinessConfigRepository businessConfigRepository;
	
	/** 
	 * 新增招商页设置
	 * @author lq
	 */
	@Transactional
	public BusinessConfig add(BusinessConfig entity) {
		businessConfigRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改招商页设置
	 * @author lq
	 */
	@Transactional
	public BusinessConfig modify(BusinessConfig entity) {
		businessConfigRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除招商页设置
	 * @author lq
	 */
	@Transactional
	public void deleteById(Integer id) {
		businessConfigRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除招商页设置
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<Integer> ids) {
		ids.forEach(id -> businessConfigRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询招商页设置
	 * @author lq
	 */
	public BusinessConfig getById(Integer id){
		return businessConfigRepository.findById(id).orElse(null);
	}

	/**
	 * 无参查找招商页设置
	 * @return
	 */
	public BusinessConfig getInfo(){
	   List<BusinessConfig> businessConfigs = businessConfigRepository.findAll();
	   if(CollectionUtils.isNotEmpty(businessConfigs)){
	   	  return businessConfigs.get(0);
	   }
	   return  businessConfigRepository.saveAndFlush(new BusinessConfig());
	}

	
	/** 
	 * 分页查询招商页设置
	 * @author lq
	 */
	public Page<BusinessConfig> page(BusinessConfigQueryRequest queryReq){
		return businessConfigRepository.findAll(
				BusinessConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询招商页设置
	 * @author lq
	 */
	public List<BusinessConfig> list(BusinessConfigQueryRequest queryReq){
		return businessConfigRepository.findAll(
				BusinessConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public BusinessConfigVO wrapperVo(BusinessConfig businessConfig) {
		if (businessConfig != null){
			BusinessConfigVO businessConfigVO=new BusinessConfigVO();
			KsBeanUtil.copyPropertiesThird(businessConfig,businessConfigVO);
			return businessConfigVO;
		}
		return null;
	}
}
