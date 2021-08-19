package com.wanmi.sbc.setting.umengpushconfig.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.api.request.umengpushconfig.UmengPushConfigQueryRequest;
import com.wanmi.sbc.setting.bean.vo.UmengPushConfigVO;
import com.wanmi.sbc.setting.umengpushconfig.model.root.UmengPushConfig;
import com.wanmi.sbc.setting.umengpushconfig.repository.UmengPushConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>友盟push接口配置业务逻辑</p>
 * @author bob
 * @date 2020-01-07 10:34:04
 */
@Service("UmengPushConfigService")
public class UmengPushConfigService {
	@Autowired
	private UmengPushConfigRepository umengPushConfigRepository;
	
	/** 
	 * 新增友盟push接口配置
	 * @author bob
	 */
	@Transactional
	public UmengPushConfig add(UmengPushConfig entity) {
		Optional<UmengPushConfig> config = getById(-1);
		entity.setId(-1);
		if (config.isPresent()){
			entity = modify(entity);
		} else {
			entity = umengPushConfigRepository.save(entity);
		}
		return entity;
	}
	
	/** 
	 * 修改友盟push接口配置
	 * @author bob
	 */
	@Transactional
	public UmengPushConfig modify(UmengPushConfig entity) {
		umengPushConfigRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除友盟push接口配置
	 * @author bob
	 */
	@Transactional
	public void deleteById(Integer id) {
		umengPushConfigRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除友盟push接口配置
	 * @author bob
	 */
	@Transactional
	public void deleteByIdList(List<Integer> ids) {
		ids.forEach(id -> umengPushConfigRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询友盟push接口配置
	 * @author bob
	 */
	public Optional<UmengPushConfig> getById(Integer id){
		return umengPushConfigRepository.findById(id);
	}
	
	/** 
	 * 分页查询友盟push接口配置
	 * @author bob
	 */
	public Page<UmengPushConfig> page(UmengPushConfigQueryRequest queryReq){
		return umengPushConfigRepository.findAll(
				UmengPushConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询友盟push接口配置
	 * @author bob
	 */
	public List<UmengPushConfig> list(UmengPushConfigQueryRequest queryReq){
		return umengPushConfigRepository.findAll(
				UmengPushConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author bob
	 */
	public UmengPushConfigVO wrapperVo(UmengPushConfig umengPushConfig) {
		if (umengPushConfig != null){
			UmengPushConfigVO umengPushConfigVO=new UmengPushConfigVO();
			KsBeanUtil.copyPropertiesThird(umengPushConfig,umengPushConfigVO);
			return umengPushConfigVO;
		}
		return null;
	}
}
