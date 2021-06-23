package com.wanmi.sbc.customer.paidcardbuyrecord.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.paidcardbuyrecord.repository.PaidCardBuyRecordRepository;
import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import com.wanmi.sbc.customer.api.request.paidcardbuyrecord.PaidCardBuyRecordQueryRequest;
import com.wanmi.sbc.customer.bean.vo.PaidCardBuyRecordVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>付费会员业务逻辑</p>
 * @author xuhai
 * @date 2021-01-29 14:03:58
 */
@Service("PaidCardBuyRecordService")
public class PaidCardBuyRecordService {
	@Autowired
	private PaidCardBuyRecordRepository paidCardBuyRecordRepository;
	
	/** 
	 * 新增付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardBuyRecord add(PaidCardBuyRecord entity) {
		paidCardBuyRecordRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardBuyRecord modify(PaidCardBuyRecord entity) {
		paidCardBuyRecordRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteById(String id) {
		paidCardBuyRecordRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		ids.forEach(id -> paidCardBuyRecordRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询付费会员
	 * @author xuhai
	 */
	public PaidCardBuyRecord getById(String id){
		return paidCardBuyRecordRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询付费会员
	 * @author xuhai
	 */
	public Page<PaidCardBuyRecord> page(PaidCardBuyRecordQueryRequest queryReq){
		return paidCardBuyRecordRepository.findAll(
				PaidCardBuyRecordWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询付费会员
	 * @author xuhai
	 */
	public List<PaidCardBuyRecord> list(PaidCardBuyRecordQueryRequest queryReq){
		return paidCardBuyRecordRepository.findAll(
				PaidCardBuyRecordWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author xuhai
	 */
	public PaidCardBuyRecordVO wrapperVo(PaidCardBuyRecord paidCardBuyRecord) {
		if (paidCardBuyRecord != null){
			PaidCardBuyRecordVO paidCardBuyRecordVO=new PaidCardBuyRecordVO();
			KsBeanUtil.copyPropertiesThird(paidCardBuyRecord,paidCardBuyRecordVO);
			return paidCardBuyRecordVO;
		}
		return null;
	}
}
