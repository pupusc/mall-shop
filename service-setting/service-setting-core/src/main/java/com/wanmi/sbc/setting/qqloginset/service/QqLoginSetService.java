package com.wanmi.sbc.setting.qqloginset.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.qqloginset.repository.QqLoginSetRepository;
import com.wanmi.sbc.setting.qqloginset.model.root.QqLoginSet;
import com.wanmi.sbc.setting.api.request.qqloginset.QqLoginSetQueryRequest;
import com.wanmi.sbc.setting.bean.vo.QqLoginSetVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>qq登录信息业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:11:28
 */
@Service("QqLoginSetService")
public class QqLoginSetService {
	@Autowired
	private QqLoginSetRepository qqLoginSetRepository;
	
	/** 
	 * 新增qq登录信息
	 * @author lq
	 */
	@Transactional
	public QqLoginSet add(QqLoginSet entity) {
		qqLoginSetRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改qq登录信息
	 * @author lq
	 */
	@Transactional
	public QqLoginSet modify(QqLoginSet entity) {
		qqLoginSetRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除qq登录信息
	 * @author lq
	 */
	@Transactional
	public void deleteById(String id) {
		qqLoginSetRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除qq登录信息
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		ids.forEach(id -> qqLoginSetRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询qq登录信息
	 * @author lq
	 */
	public QqLoginSet getById(String id){
		return qqLoginSetRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询qq登录信息
	 * @author lq
	 */
	public Page<QqLoginSet> page(QqLoginSetQueryRequest queryReq){
		return qqLoginSetRepository.findAll(
				QqLoginSetWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询qq登录信息
	 * @author lq
	 */
	public List<QqLoginSet> list(QqLoginSetQueryRequest queryReq){
		return qqLoginSetRepository.findAll(
				QqLoginSetWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public QqLoginSetVO wrapperVo(QqLoginSet qqLoginSet) {
		if (qqLoginSet != null){
			QqLoginSetVO qqLoginSetVO=new QqLoginSetVO();
			KsBeanUtil.copyPropertiesThird(qqLoginSet,qqLoginSetVO);
			return qqLoginSetVO;
		}
		return null;
	}
}
