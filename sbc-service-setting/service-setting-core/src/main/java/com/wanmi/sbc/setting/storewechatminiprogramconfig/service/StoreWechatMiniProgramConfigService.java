package com.wanmi.sbc.setting.storewechatminiprogramconfig.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.repository.StoreWechatMiniProgramConfigRepository;
import com.wanmi.sbc.setting.storewechatminiprogramconfig.model.root.StoreWechatMiniProgramConfig;
import com.wanmi.sbc.setting.api.request.storewechatminiprogramconfig.StoreWechatMiniProgramConfigQueryRequest;
import com.wanmi.sbc.setting.bean.vo.StoreWechatMiniProgramConfigVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;
import java.util.Optional;

/**
 * <p>门店微信小程序配置业务逻辑</p>
 * @author tangLian
 * @date 2020-01-16 11:47:15
 */
@Service("StoreWechatMiniProgramConfigService")
public class StoreWechatMiniProgramConfigService {
	@Autowired
	private StoreWechatMiniProgramConfigRepository storeWechatMiniProgramConfigRepository;
	
	/** 
	 * 新增门店微信小程序配置
	 * @author tangLian
	 */
	@Transactional
	public StoreWechatMiniProgramConfig add(StoreWechatMiniProgramConfig entity) {
		storeWechatMiniProgramConfigRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改门店微信小程序配置
	 * @author tangLian
	 */
	@Transactional
	public StoreWechatMiniProgramConfig modify(StoreWechatMiniProgramConfig entity) {
		storeWechatMiniProgramConfigRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除门店微信小程序配置
	 * @author tangLian
	 */
	@Transactional
	public void deleteById(String id) {
		storeWechatMiniProgramConfigRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除门店微信小程序配置
	 * @author tangLian
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		storeWechatMiniProgramConfigRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询门店微信小程序配置
	 * @author tangLian
	 */
	public StoreWechatMiniProgramConfig getById(String id){
		Optional<StoreWechatMiniProgramConfig> optional = storeWechatMiniProgramConfigRepository.findById(id);
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}

	/**
	 * 门店id查询门店微信小程序配置
	 * @author tangLian
	 */
	public StoreWechatMiniProgramConfig getByStoreId(Long storeId){
		Optional<StoreWechatMiniProgramConfig> optional = storeWechatMiniProgramConfigRepository.findByStoreIdAndDelFlag(storeId, DeleteFlag.NO);
		if(optional.isPresent()){
			return optional.get();
		}
		return null;
	}
	
	/** 
	 * 分页查询门店微信小程序配置
	 * @author tangLian
	 */
	public Page<StoreWechatMiniProgramConfig> page(StoreWechatMiniProgramConfigQueryRequest queryReq){
		return storeWechatMiniProgramConfigRepository.findAll(
				StoreWechatMiniProgramConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询门店微信小程序配置
	 * @author tangLian
	 */
	public List<StoreWechatMiniProgramConfig> list(StoreWechatMiniProgramConfigQueryRequest queryReq){
		return storeWechatMiniProgramConfigRepository.findAll(
				StoreWechatMiniProgramConfigWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author tangLian
	 */
	public StoreWechatMiniProgramConfigVO wrapperVo(StoreWechatMiniProgramConfig storeWechatMiniProgramConfig) {
		if (storeWechatMiniProgramConfig != null){
			StoreWechatMiniProgramConfigVO storeWechatMiniProgramConfigVO=new StoreWechatMiniProgramConfigVO();
			KsBeanUtil.copyPropertiesThird(storeWechatMiniProgramConfig,storeWechatMiniProgramConfigVO);
			return storeWechatMiniProgramConfigVO;
		}
		return null;
	}
}
