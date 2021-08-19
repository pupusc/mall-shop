package com.wanmi.sbc.customer.storeevaluate.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateQueryRequest;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateVO;
import com.wanmi.sbc.customer.storeevaluate.model.root.StoreEvaluate;
import com.wanmi.sbc.customer.storeevaluate.repository.StoreEvaluateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺评价业务逻辑</p>
 * @author liutao
 * @date 2019-02-26 10:23:32
 */
@Service("StoreEvaluateService")
public class StoreEvaluateService {
	@Autowired
	private StoreEvaluateRepository storeEvaluateRepository;
	
	/** 
	 * 新增店铺评价
	 * @author liutao
	 */
	@GlobalTransactional
	@Transactional(rollbackFor = Exception.class)
	public StoreEvaluate add(StoreEvaluate entity) {
		storeEvaluateRepository.save(entity);
		return entity;
	}

	/**
	 * 批量增加店铺评价
	 * @param storeEvaluateList
	 */
	@GlobalTransactional
	@Transactional
	public void addList(List<StoreEvaluate> storeEvaluateList){
		storeEvaluateRepository.saveAll(storeEvaluateList);
	}
	
	/** 
	 * 修改店铺评价
	 * @author liutao
	 */
	@Transactional
	public StoreEvaluate modify(StoreEvaluate entity) {
		storeEvaluateRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除店铺评价
	 * @author liutao
	 */
	@Transactional
	public void deleteById(String id) {
		storeEvaluateRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除店铺评价
	 * @author liutao
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		storeEvaluateRepository.deleteAll(ids.stream().map(id -> {
			StoreEvaluate entity = new StoreEvaluate();
			entity.setEvaluateId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询店铺评价
	 * @author liutao
	 */
	public StoreEvaluate getById(String id){
		return storeEvaluateRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询店铺评价
	 * @author liutao
	 */
	public Page<StoreEvaluate> page(StoreEvaluateQueryRequest queryReq){
		return storeEvaluateRepository.findAll(
				StoreEvaluateWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询店铺评价
	 * @author liutao
	 */
	public List<StoreEvaluate> list(StoreEvaluateQueryRequest queryReq){
		return storeEvaluateRepository.findAll(StoreEvaluateWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author liutao
	 */
	public StoreEvaluateVO wrapperVo(StoreEvaluate storeEvaluate) {
		if (storeEvaluate != null){
			StoreEvaluateVO storeEvaluateVO=new StoreEvaluateVO();
			KsBeanUtil.copyPropertiesThird(storeEvaluate,storeEvaluateVO);
			return storeEvaluateVO;
		}
		return null;
	}
}
