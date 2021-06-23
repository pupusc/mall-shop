package com.wanmi.sbc.customer.paidcardrightsrel.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.paidcardrightsrel.repository.PaidCardRightsRelRepository;
import com.wanmi.sbc.customer.paidcardrightsrel.model.root.PaidCardRightsRel;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelQueryRequest;
import com.wanmi.sbc.customer.bean.vo.PaidCardRightsRelVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>付费会员业务逻辑</p>
 * @author xuhai
 * @date 2021-01-29 14:04:00
 */
@Service("PaidCardRightsRelService")
public class PaidCardRightsRelService {
	@Autowired
	private PaidCardRightsRelRepository paidCardRightsRelRepository;
	
	/** 
	 * 新增付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardRightsRel add(PaidCardRightsRel entity) {
		paidCardRightsRelRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardRightsRel modify(PaidCardRightsRel entity) {
		paidCardRightsRelRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteById(String id) {
		paidCardRightsRelRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		ids.forEach(id -> paidCardRightsRelRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询付费会员
	 * @author xuhai
	 */
	public PaidCardRightsRel getById(String id){
		return paidCardRightsRelRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询付费会员
	 * @author xuhai
	 */
	public Page<PaidCardRightsRel> page(PaidCardRightsRelQueryRequest queryReq){
		return paidCardRightsRelRepository.findAll(
				PaidCardRightsRelWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询付费会员
	 * @author xuhai
	 */
	public List<PaidCardRightsRel> list(PaidCardRightsRelQueryRequest queryReq){
		return paidCardRightsRelRepository.findAll(
				PaidCardRightsRelWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author xuhai
	 */
	public PaidCardRightsRelVO wrapperVo(PaidCardRightsRel paidCardRightsRel) {
		if (paidCardRightsRel != null){
			PaidCardRightsRelVO paidCardRightsRelVO=new PaidCardRightsRelVO();
			KsBeanUtil.copyPropertiesThird(paidCardRightsRel,paidCardRightsRelVO);
			return paidCardRightsRelVO;
		}
		return null;
	}
}
