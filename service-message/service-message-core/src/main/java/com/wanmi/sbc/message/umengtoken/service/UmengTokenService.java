package com.wanmi.sbc.message.umengtoken.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.request.umengtoken.UmengTokenQueryRequest;
import com.wanmi.sbc.message.bean.vo.UmengTokenVO;
import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import com.wanmi.sbc.message.pushcustomerenable.repository.PushCustomerEnableRepository;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import com.wanmi.sbc.message.umengtoken.repository.UmengTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>友盟推送设备与会员关系业务逻辑</p>
 * @author bob
 * @date 2020-01-06 11:36:26
 */
@Service("UmengTokenService")
public class UmengTokenService {
	@Autowired
	private UmengTokenRepository umengTokenRepository;

	@Autowired
	private PushCustomerEnableRepository pushCustomerEnableRepository;
	
	/** 
	 * 新增友盟推送设备与会员关系
	 * @author bob
	 */
	@Transactional
	public UmengToken add(UmengToken entity) {
		//初始化用户push开关
		Optional<PushCustomerEnable> pushCustomerEnableOptional = pushCustomerEnableRepository.queryByCustomerId(entity.getCustomerId());
		if(!pushCustomerEnableOptional.isPresent()){
			PushCustomerEnable pushCustomerEnable = new PushCustomerEnable();
			pushCustomerEnable.setAccountAssets(1);
			pushCustomerEnable.setAccountSecurity(1);
			pushCustomerEnable.setDistribution(1);
			pushCustomerEnable.setOrderProgressRate(1);
			pushCustomerEnable.setEnableStatus(1);
			pushCustomerEnable.setReturnOrderProgressRate(1);
			pushCustomerEnable.setDelFlag(DeleteFlag.NO);
			pushCustomerEnable.setCustomerId(entity.getCustomerId());
			pushCustomerEnableRepository.save(pushCustomerEnable);
		}

		Optional<UmengToken> umengToken = umengTokenRepository.queryByCustomerId(entity.getCustomerId());
		if (umengToken.isPresent()){
			UmengToken token = umengToken.get();
			entity.setId(token.getId());
		}
		return umengTokenRepository.save(entity);
	}
	
	/** 
	 * 修改友盟推送设备与会员关系
	 * @author bob
	 */
	@Transactional
	public UmengToken modify(UmengToken entity) {
		umengTokenRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除友盟推送设备与会员关系
	 * @author bob
	 */
	@Transactional
	public void deleteById(Long id) {
		umengTokenRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除友盟推送设备与会员关系
	 * @author bob
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> umengTokenRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询友盟推送设备与会员关系
	 * @author bob
	 */
	public Optional<UmengToken> getById(Long id){
		return umengTokenRepository.findById(id);
	}
	
	/** 
	 * 分页查询友盟推送设备与会员关系
	 * @author bob
	 */
	public Page<UmengToken> page(UmengTokenQueryRequest queryReq){
		return umengTokenRepository.findAll(
				UmengTokenWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询友盟推送设备与会员关系
	 * @author bob
	 */
	public List<UmengToken> list(UmengTokenQueryRequest queryReq){
		return umengTokenRepository.findAll(
				UmengTokenWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author bob
	 */
	public UmengTokenVO wrapperVo(UmengToken umengToken) {
		if (umengToken != null){
			UmengTokenVO umengTokenVO=new UmengTokenVO();
			KsBeanUtil.copyPropertiesThird(umengToken,umengTokenVO);
			return umengTokenVO;
		}
		return null;
	}
}
