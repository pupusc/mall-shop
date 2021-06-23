package com.wanmi.sbc.customer.storesharerecord.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.storesharerecord.repository.StoreShareRecordRepository;
import com.wanmi.sbc.customer.storesharerecord.model.root.StoreShareRecord;
import com.wanmi.sbc.customer.api.request.storesharerecord.StoreShareRecordQueryRequest;
import com.wanmi.sbc.customer.bean.vo.StoreShareRecordVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>商城分享业务逻辑</p>
 * @author zhangwenchang
 * @date 2020-03-06 13:48:42
 */
@Service("StoreShareRecordService")
public class StoreShareRecordService {
	@Autowired
	private StoreShareRecordRepository storeShareRecordRepository;
	
	/** 
	 * 新增商城分享
	 * @author zhangwenchang
	 */
	@Transactional
	public StoreShareRecord add(StoreShareRecord entity) {
		storeShareRecordRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改商城分享
	 * @author zhangwenchang
	 */
	@Transactional
	public StoreShareRecord modify(StoreShareRecord entity) {
		storeShareRecordRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除商城分享
	 * @author zhangwenchang
	 */
	@Transactional
	public void deleteById(Long id) {
		storeShareRecordRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除商城分享
	 * @author zhangwenchang
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> storeShareRecordRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询商城分享
	 * @author zhangwenchang
	 */
	public StoreShareRecord getById(Long id){
		return storeShareRecordRepository.getOne(id);
	}
	
	/** 
	 * 分页查询商城分享
	 * @author zhangwenchang
	 */
	public Page<StoreShareRecord> page(StoreShareRecordQueryRequest queryReq){
		return storeShareRecordRepository.findAll(
				StoreShareRecordWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询商城分享
	 * @author zhangwenchang
	 */
	public List<StoreShareRecord> list(StoreShareRecordQueryRequest queryReq){
		return storeShareRecordRepository.findAll(
				StoreShareRecordWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author zhangwenchang
	 */
	public StoreShareRecordVO wrapperVo(StoreShareRecord storeShareRecord) {
		if (storeShareRecord != null){
			StoreShareRecordVO storeShareRecordVO=new StoreShareRecordVO();
			KsBeanUtil.copyPropertiesThird(storeShareRecord,storeShareRecordVO);
			return storeShareRecordVO;
		}
		return null;
	}
}
