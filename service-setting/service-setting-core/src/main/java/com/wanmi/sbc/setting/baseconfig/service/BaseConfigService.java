package com.wanmi.sbc.setting.baseconfig.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.baseconfig.repository.BaseConfigRepository;
import com.wanmi.sbc.setting.baseconfig.model.root.BaseConfig;
import com.wanmi.sbc.setting.api.request.baseconfig.BaseConfigQueryRequest;
import com.wanmi.sbc.setting.bean.vo.BaseConfigVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>基本设置业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:08:31
 */
@Service("BaseConfigService")
public class BaseConfigService {
	@Autowired
	private BaseConfigRepository baseConfigRepository;
	
	/** 
	 * 新增基本设置
	 * @author lq
	 */
	@Transactional
	public BaseConfig add(BaseConfig entity) {
		baseConfigRepository.save(entity);
		return entity;
	}

	/** 
	 * 修改基本设置
	 * @author lq
	 */
	@Transactional
	public BaseConfig modify(BaseConfig entity) {
		baseConfigRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除基本设置
	 * @author lq
	 */
	@Transactional
	public void deleteById(Integer id) {
		baseConfigRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除基本设置
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<Integer> ids) {
		ids.forEach(id -> baseConfigRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询基本设置
	 * @author lq
	 */
	public BaseConfig getById(Integer id){
		return baseConfigRepository.findById(id).orElse(null);
	}

	public String queryBossLogo(){
		return baseConfigRepository.queryBossLogo();
	}
	
	/** 
	 * 分页查询基本设置
	 * @author lq
	 */
	public Page<BaseConfig> page(BaseConfigQueryRequest queryReq){
		return baseConfigRepository.findAll(
				BaseConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询基本设置
	 * @author lq
	 */
	public List<BaseConfig> list(BaseConfigQueryRequest queryReq){
		return baseConfigRepository.findAll(
				BaseConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public BaseConfigVO wrapperVo(BaseConfig baseConfig) {
		if (baseConfig != null){
			BaseConfigVO baseConfigVO=new BaseConfigVO();
			KsBeanUtil.copyPropertiesThird(baseConfig,baseConfigVO);
			return baseConfigVO;
		}
		return null;
	}


}
