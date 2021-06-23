package com.wanmi.sbc.setting.push.service;

import com.wanmi.sbc.setting.api.request.push.AppPushConfigQueryRequest;
import com.wanmi.sbc.setting.push.model.root.AppPushConfig;
import com.wanmi.sbc.setting.push.repository.AppPushConfigRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.bean.vo.AppPushConfigVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;
import java.util.Objects;

/**
 * <p>消息推送业务逻辑</p>
 * @author chenyufei
 * @date 2019-05-10 14:39:59
 */
@Service("AppPushConfigService")
public class AppPushConfigService {

	@Autowired
	private AppPushConfigRepository appPushConfigRepository;
	
	/** 
	 * 新增消息推送
	 * @author chenyufei
	 */
	@Transactional
	public AppPushConfig add(AppPushConfig entity) {
		appPushConfigRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改消息推送
	 * @author chenyufei
	 */
	@Transactional
	public AppPushConfig modify(AppPushConfig entity) {
		appPushConfigRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 单个查询消息推送
	 * @author chenyufei
	 */
	public AppPushConfig getById(Long id){
		return appPushConfigRepository.findById(id).get();
	}
	
	/** 
	 * 列表查询消息推送
	 * @author chenyufei
	 */
	public List<AppPushConfig> list(AppPushConfigQueryRequest queryReq){
		Sort sort = queryReq.getSort();
		if(Objects.nonNull(sort)) {
			return appPushConfigRepository.findAll(AppPushConfigWhereCriteriaBuilder.build(queryReq), sort);
		}else {
			return appPushConfigRepository.findAll(AppPushConfigWhereCriteriaBuilder.build(queryReq));
		}
	}

	/**
	 * 将实体包装成VO
	 * @author chenyufei
	 */
	public AppPushConfigVO wrapperVo(AppPushConfig appPushConfig) {
		if (appPushConfig != null){
			AppPushConfigVO appPushConfigVO=new AppPushConfigVO();
			KsBeanUtil.copyPropertiesThird(appPushConfig,appPushConfigVO);
			return appPushConfigVO;
		}
		return null;
	}
}
