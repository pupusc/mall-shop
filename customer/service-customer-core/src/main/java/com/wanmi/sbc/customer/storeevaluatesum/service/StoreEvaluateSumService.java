package com.wanmi.sbc.customer.storeevaluatesum.service;

import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.request.storeevaluatesum.StoreEvaluateSumQueryRequest;
import com.wanmi.sbc.customer.bean.vo.StoreEvaluateSumVO;
import com.wanmi.sbc.customer.storeevaluatesum.model.root.StoreEvaluateSum;
import com.wanmi.sbc.customer.storeevaluatesum.repository.StoreEvaluateSumRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺评价业务逻辑</p>
 * @author liutao
 * @date 2019-02-23 10:59:09
 */
@Service("StoreEvaluateSumService")
public class StoreEvaluateSumService {
	@Autowired
	private StoreEvaluateSumRepository storeEvaluateSumRepository;
	
	/** 
	 * 新增店铺评价
	 * @author liutao
	 */
	@Transactional
	public StoreEvaluateSum add(StoreEvaluateSum entity) {
		storeEvaluateSumRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改店铺评价
	 * @author liutao
	 */
	@Transactional
	public StoreEvaluateSum modify(StoreEvaluateSum entity) {
		storeEvaluateSumRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除店铺评价
	 * @author liutao
	 */
	@Transactional
	public void deleteById(Long id) {
		storeEvaluateSumRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除店铺评价
	 * @author liutao
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		storeEvaluateSumRepository.deleteAll(ids.stream().map(id -> {
			StoreEvaluateSum entity = new StoreEvaluateSum();
			entity.setSumId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询店铺评价
	 * @author liutao
	 */
	public StoreEvaluateSum getById(Long id){
		return storeEvaluateSumRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询店铺评价
	 * @author liutao
	 */
	public Page<StoreEvaluateSum> page(StoreEvaluateSumQueryRequest queryReq){
		return storeEvaluateSumRepository.findAll(
				StoreEvaluateSumWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询店铺评价
	 * @author liutao
	 */
	public List<StoreEvaluateSum> list(StoreEvaluateSumQueryRequest queryReq){
		return storeEvaluateSumRepository.findAll(StoreEvaluateSumWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author liutao
	 */
	public StoreEvaluateSumVO wrapperVo(StoreEvaluateSum storeEvaluateSum) {
		if (storeEvaluateSum != null){
			StoreEvaluateSumVO storeEvaluateSumVO=new StoreEvaluateSumVO();
			KsBeanUtil.copyPropertiesThird(storeEvaluateSum,storeEvaluateSumVO);
			return storeEvaluateSumVO;
		}
		return null;
	}

	/**
	 * 删除全部
	 */
	@Transactional
	public void deleteAll(){
		storeEvaluateSumRepository.deleteAll();
	}

	/**
	 * 根据店铺id查询店铺评价信息 30 90 180的
	 *
	 * @param storeEvaluateSumQueryRequest
	 * @return
	 */
	@Transactional
	public StoreEvaluateSum getByStoreId(StoreEvaluateSumQueryRequest storeEvaluateSumQueryRequest){
		return storeEvaluateSumRepository.findOne(StoreEvaluateSumWhereCriteriaBuilder.build(storeEvaluateSumQueryRequest)).orElse(null);
	}

	/**
	 * 批量增加
	 *
	 * @param storeEvaluateSumList
	 */
	@Transactional
	public void addList(List<StoreEvaluateSum> storeEvaluateSumList){
		if (CollectionUtils.isEmpty(storeEvaluateSumList)){
			return;
		}
		storeEvaluateSumList.stream().forEach(storeEvaluateSum -> {
			this.add(storeEvaluateSum);
		});
	}
}
