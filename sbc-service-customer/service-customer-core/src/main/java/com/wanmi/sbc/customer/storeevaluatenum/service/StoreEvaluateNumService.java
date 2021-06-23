package com.wanmi.sbc.customer.storeevaluatenum.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumListRequest;
import com.wanmi.sbc.customer.api.request.storeevaluatenum.StoreEvaluateNumQueryRequest;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateNumVO;
import com.wanmi.sbc.customer.storeevaluatenum.model.root.StoreEvaluateNum;
import com.wanmi.sbc.customer.storeevaluatenum.repository.StoreEvaluateNumRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺统计评分等级人数统计业务逻辑</p>
 * @author liutao
 * @date 2019-03-04 10:55:28
 */
@Service("StoreEvaluateNumService")
public class StoreEvaluateNumService {
	@Autowired
	private StoreEvaluateNumRepository storeEvaluateNumRepository;
	
	/** 
	 * 新增店铺统计评分等级人数统计
	 * @author liutao
	 */
	@Transactional
	public StoreEvaluateNum add(StoreEvaluateNum entity) {
		storeEvaluateNumRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改店铺统计评分等级人数统计
	 * @author liutao
	 */
	@Transactional
	public StoreEvaluateNum modify(StoreEvaluateNum entity) {
		storeEvaluateNumRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除店铺统计评分等级人数统计
	 * @author liutao
	 */
	@Transactional
	public void deleteById(String id) {
		storeEvaluateNumRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除店铺统计评分等级人数统计
	 * @author liutao
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		storeEvaluateNumRepository.deleteAll(ids.stream().map(id -> {
			StoreEvaluateNum entity = new StoreEvaluateNum();
			entity.setNumId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询店铺统计评分等级人数统计
	 * @author liutao
	 */
	public StoreEvaluateNum getById(String id){
		return storeEvaluateNumRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询店铺统计评分等级人数统计
	 * @author liutao
	 */
	public Page<StoreEvaluateNum> page(StoreEvaluateNumQueryRequest queryReq){
		return storeEvaluateNumRepository.findAll(
				StoreEvaluateNumWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询店铺统计评分等级人数统计
	 * @author liutao
	 */
	public List<StoreEvaluateNum> list(StoreEvaluateNumQueryRequest queryReq){
		return storeEvaluateNumRepository.findAll(StoreEvaluateNumWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author liutao
	 */
	public StoreEvaluateNumVO wrapperVo(StoreEvaluateNum storeEvaluateNum) {
		if (storeEvaluateNum != null){
			StoreEvaluateNumVO storeEvaluateNumVO=new StoreEvaluateNumVO();
			KsBeanUtil.copyPropertiesThird(storeEvaluateNum,storeEvaluateNumVO);
			return storeEvaluateNumVO;
		}
		return null;
	}

	/**
	 * 删除全部
	 */
	@Transactional
	public void deleteAll(){
		storeEvaluateNumRepository.deleteAll();
	}

	/**
	 * 批量增加
	 * @param storeEvaluateNumList
	 */
	@Transactional
	public void addList(List<StoreEvaluateNum> storeEvaluateNumList){
		if (CollectionUtils.isEmpty(storeEvaluateNumList)){
			return;
		}
		storeEvaluateNumList.stream().forEach(storeEvaluateNum -> {
			this.add(storeEvaluateNum);
		});
	}

	/**
	 * 根据店铺id和统计周期查询
	 * @param listRequest
	 * @return
	 */
	public List<StoreEvaluateNum> listByStoreIdAndScoreCycle(StoreEvaluateNumListRequest listRequest){
		return storeEvaluateNumRepository.findByStoreIdAndScoreCycle(listRequest.getStoreId(),listRequest.getScoreCycle());
	}

}
