package com.wanmi.sbc.setting.hovernavmobile.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.setting.bean.vo.HoverNavMobileVO;
import com.wanmi.sbc.setting.hovernavmobile.model.root.HoverNavMobile;
import com.wanmi.sbc.setting.hovernavmobile.repository.HoverNavMobileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>移动端悬浮导航栏业务逻辑</p>
 * @author dyt
 * @date 2020-04-29 14:28:21
 */
@Service("HoverNavMobileService")
public class HoverNavMobileService {
	@Autowired
	private HoverNavMobileRepository hoverNavMobileRepository;

	/**
	 * 修改移动端悬浮导航栏
	 * @author dyt
	 */
	@Transactional
	public HoverNavMobile modify(HoverNavMobile entity) {
		hoverNavMobileRepository.save(entity);
		return entity;
	}

	/**
	 * 单个查询移动端悬浮导航栏
	 * @author dyt
	 */
	@Transactional
	public HoverNavMobile getOne(Long storeId){
		HoverNavMobile mobile = hoverNavMobileRepository.findById(storeId).orElse(null);
		if(mobile == null){
			mobile = new HoverNavMobile();
			mobile.setStoreId(storeId);
			hoverNavMobileRepository.save(mobile);
			return mobile;
		}
		return mobile;
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public HoverNavMobileVO wrapperVo(HoverNavMobile hoverNavMobile) {
		if (hoverNavMobile != null){
			return KsBeanUtil.convert(hoverNavMobile, HoverNavMobileVO.class);
		}
		return null;
	}
}

