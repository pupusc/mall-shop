package com.wanmi.sbc.setting.weibologinset.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.weibologinset.repository.WeiboLoginSetRepository;
import com.wanmi.sbc.setting.weibologinset.model.root.WeiboLoginSet;
import com.wanmi.sbc.setting.api.request.weibologinset.WeiboLoginSetQueryRequest;
import com.wanmi.sbc.setting.bean.vo.WeiboLoginSetVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>微信登录配置业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:17:06
 */
@Service("WeiboLoginSetService")
public class WeiboLoginSetService {
	@Autowired
	private WeiboLoginSetRepository weiboLoginSetRepository;
	
	/** 
	 * 新增微信登录配置
	 * @author lq
	 */
	@Transactional
	public WeiboLoginSet add(WeiboLoginSet entity) {
		weiboLoginSetRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改微信登录配置
	 * @author lq
	 */
	@Transactional
	public WeiboLoginSet modify(WeiboLoginSet entity) {
		weiboLoginSetRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除微信登录配置
	 * @author lq
	 */
	@Transactional
	public void deleteById(String id) {
		weiboLoginSetRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除微信登录配置
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		ids.forEach(id -> weiboLoginSetRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询微信登录配置
	 * @author lq
	 */
	public WeiboLoginSet getById(String id){
		return weiboLoginSetRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询微信登录配置
	 * @author lq
	 */
	public Page<WeiboLoginSet> page(WeiboLoginSetQueryRequest queryReq){
		return weiboLoginSetRepository.findAll(
				WeiboLoginSetWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询微信登录配置
	 * @author lq
	 */
	public List<WeiboLoginSet> list(WeiboLoginSetQueryRequest queryReq){
		return weiboLoginSetRepository.findAll(
				WeiboLoginSetWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public WeiboLoginSetVO wrapperVo(WeiboLoginSet weiboLoginSet) {
		if (weiboLoginSet != null){
			WeiboLoginSetVO weiboLoginSetVO=new WeiboLoginSetVO();
			KsBeanUtil.copyPropertiesThird(weiboLoginSet,weiboLoginSetVO);
			return weiboLoginSetVO;
		}
		return null;
	}
}
