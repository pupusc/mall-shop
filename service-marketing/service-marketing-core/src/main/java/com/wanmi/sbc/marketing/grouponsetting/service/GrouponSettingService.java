package com.wanmi.sbc.marketing.grouponsetting.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.marketing.grouponsetting.repository.GrouponSettingRepository;
import com.wanmi.sbc.marketing.grouponsetting.model.root.GrouponSetting;
import com.wanmi.sbc.marketing.api.request.grouponsetting.GrouponSettingQueryRequest;
import com.wanmi.sbc.marketing.bean.vo.GrouponSettingVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>拼团活动信息表业务逻辑</p>
 * @author groupon
 * @date 2019-05-15 14:19:49
 */
@Service("GrouponSettingService")
public class GrouponSettingService {
	@Autowired
	private GrouponSettingRepository grouponSettingRepository;
	
	/** 
	 * 新增拼团活动信息表
	 * @author groupon
	 */
	@Transactional
	public GrouponSetting add(GrouponSetting entity) {
		grouponSettingRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改拼团活动信息表
	 * @author groupon
	 */
	@Transactional
	public GrouponSetting modify(GrouponSetting entity) {
		grouponSettingRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除拼团活动信息表
	 * @author groupon
	 */
	@Transactional
	public void deleteById(String id) {
		grouponSettingRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除拼团活动信息表
	 * @author groupon
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		grouponSettingRepository.deleteAll(ids.stream().map(id -> {
			GrouponSetting entity = new GrouponSetting();
			entity.setId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询拼团活动信息表
	 * @author groupon
	 */
	public GrouponSetting getById(String id){
		return grouponSettingRepository.findById(id).get();
	}
	
	/** 
	 * 分页查询拼团活动信息表
	 * @author groupon
	 */
	public List<GrouponSetting> getSetting(){
		return grouponSettingRepository.findAll();
	}

	/**
	 * 列表查询拼团活动信息表
	 * @author groupon
	 */
	public List<GrouponSetting> list(GrouponSettingQueryRequest queryReq){
		return grouponSettingRepository.findAll(GrouponSettingWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author groupon
	 */
	public GrouponSettingVO wrapperVo(GrouponSetting grouponSetting) {
		if (grouponSetting != null){
			GrouponSettingVO grouponSettingVO=new GrouponSettingVO();
			KsBeanUtil.copyPropertiesThird(grouponSetting,grouponSettingVO);
			return grouponSettingVO;
		}
		return null;
	}

	@Transactional
	public void saveAudit(DefaultFlag flag){
		GrouponSetting grouponSetting;
		List<GrouponSetting> grouponSettings = grouponSettingRepository.findAll();
		if(CollectionUtils.isEmpty(grouponSettings)){
			grouponSetting = new GrouponSetting();
		}else{
			grouponSetting = grouponSettings.get(0);
		}
		grouponSetting.setGoodsAuditFlag(flag);
		grouponSettingRepository.save(grouponSetting);
	}


	@Transactional
	public void savePoster(String poster){
		GrouponSetting grouponSetting;
		List<GrouponSetting> grouponSettings = grouponSettingRepository.findAll();
		if(CollectionUtils.isEmpty(grouponSettings)){
			grouponSetting = new GrouponSetting();
		}else{
			grouponSetting = grouponSettings.get(0);
		}
		grouponSetting.setAdvert(poster);
		grouponSettingRepository.save(grouponSetting);
	}

	@Transactional
	public void saveRule(String rule){
		GrouponSetting grouponSetting;
		List<GrouponSetting> grouponSettings = grouponSettingRepository.findAll();
		if(CollectionUtils.isEmpty(grouponSettings)){
			grouponSetting = new GrouponSetting();
		}else{
			grouponSetting = grouponSettings.get(0);
		}
		grouponSetting.setRule(rule);
		grouponSettingRepository.save(grouponSetting);
	}

	/**
	 * 获取拼团商品审核状态
	 */
	public DefaultFlag getGoodsAuditFlag() {
		GrouponSetting grouponSetting = grouponSettingRepository.findAll().get(0);
		if (Objects.nonNull(grouponSetting)) {
			return grouponSetting.getGoodsAuditFlag();
		} else {
			return null;
		}
	}
}
