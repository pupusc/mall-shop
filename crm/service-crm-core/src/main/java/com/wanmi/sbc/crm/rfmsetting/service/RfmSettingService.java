package com.wanmi.sbc.crm.rfmsetting.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.crm.api.request.rfmsetting.RfmSettingQueryRequest;
import com.wanmi.sbc.crm.bean.enums.RFMType;
import com.wanmi.sbc.crm.bean.vo.RfmSettingVO;
import com.wanmi.sbc.crm.rfmsetting.model.root.RfmSetting;
import com.wanmi.sbc.crm.rfmsetting.repository.RfmSettingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>rfm参数配置业务逻辑</p>
 * @author zhanglingke
 * @date 2019-10-14 14:33:42
 */
@Service("RfmSettingService")
public class RfmSettingService {
	@Autowired
	private RfmSettingRepository rfmSettingRepository;
	
	/** 
	 * 新增rfm参数配置
	 * @author zhanglingke
	 */
	@Transactional
	public RfmSetting add(RfmSetting entity) {
		rfmSettingRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改rfm参数配置
	 * @author zhanglingke
	 */
	@Transactional
	public RfmSetting modify(RfmSetting entity) {
		rfmSettingRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除rfm参数配置
	 * @author zhanglingke
	 */
	@Transactional
	public void deleteById(Long id) {
		rfmSettingRepository.deleteByBeanId(id);
	}
	
	/** 
	 * 批量删除rfm参数配置
	 * @author zhanglingke
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		rfmSettingRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询rfm参数配置
	 * @author zhanglingke
	 */
	public RfmSetting getById(Long id){
		return rfmSettingRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询rfm参数配置
	 * @author zhanglingke
	 */
	public Page<RfmSetting> page(RfmSettingQueryRequest queryReq){
		return rfmSettingRepository.findAll(
				RfmSettingWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询rfm参数配置
	 * @author zhanglingke
	 */
	public List<RfmSetting> list(RfmSettingQueryRequest queryReq){
		return rfmSettingRepository.findAll(
				RfmSettingWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author zhanglingke
	 */
	public RfmSettingVO wrapperVo(RfmSetting rfmSetting) {
		if (rfmSetting != null){
			RfmSettingVO rfmSettingVO=new RfmSettingVO();
			KsBeanUtil.copyPropertiesThird(rfmSetting,rfmSettingVO);
			return rfmSettingVO;
		}
		return null;
	}

	/**
	 * 获取该模型下的最大参数配置
	 * @param type
	 * @return
	 */
	public RfmSetting getMaxParamSetting(RFMType type){
		return rfmSettingRepository.getMaxParamSetting(type.toValue());
	}

	/**
	 * 获取该模型下的最大分配置
	 * @param type
	 * @return
	 */
	public RfmSetting getMaxScoreSetting(RFMType type){
		return rfmSettingRepository.getMaxScoreSetting(type.toValue());
	}

	/**
	 * 批量插入RFM模型
	 * @param rParse
	 */
    @Transactional(rollbackFor = Exception.class)
	public void addAll(List<RfmSetting> rParse) {
		rfmSettingRepository.saveAll(rParse);
	}

	/**
	 * 批量插入RFM模型
	 * @param rParse
	 */
	@Transactional(rollbackFor = Exception.class)
	public void allocation(List<RfmSetting> rParse) {
        rfmSettingRepository.deleteAll();
		this.addAll(rParse);
	}
}
