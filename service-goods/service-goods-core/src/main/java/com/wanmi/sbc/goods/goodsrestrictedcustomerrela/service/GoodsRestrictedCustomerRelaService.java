package com.wanmi.sbc.goods.goodsrestrictedcustomerrela.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.repository.GoodsRestrictedCustomerRelaRepository;
import com.wanmi.sbc.goods.goodsrestrictedcustomerrela.model.root.GoodsRestrictedCustomerRela;
import com.wanmi.sbc.goods.api.request.goodsrestrictedcustomerrela.GoodsRestrictedCustomerRelaQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsRestrictedCustomerRelaVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.beans.Transient;
import java.util.List;

/**
 * <p>限售配置会员关系表业务逻辑</p>
 * @author baijz
 * @date 2020-04-08 11:32:28
 */
@Service("GoodsRestrictedCustomerRelaService")
public class GoodsRestrictedCustomerRelaService {
	@Autowired
	private GoodsRestrictedCustomerRelaRepository goodsRestrictedCustomerRelaRepository;
	
	/** 
	 * 新增限售配置会员关系表
	 * @author baijz
	 */
	@Transactional
	public GoodsRestrictedCustomerRela add(GoodsRestrictedCustomerRela entity) {
		goodsRestrictedCustomerRelaRepository.save(entity);
		return entity;
	}

	/**
	 * 批量新增限售配置会员关系表
	 * @author baijz
	 */
	@Transactional
	public List<GoodsRestrictedCustomerRela> addBatch(List<GoodsRestrictedCustomerRela> entityList) {
		goodsRestrictedCustomerRelaRepository.saveAll(entityList);
		return entityList;
	}

	
	/** 
	 * 修改限售配置会员关系表
	 * @author baijz
	 */
	@Transactional
	public GoodsRestrictedCustomerRela modify(GoodsRestrictedCustomerRela entity) {
		goodsRestrictedCustomerRelaRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除限售配置会员关系表
	 * @author baijz
	 */
	@Transactional
	public void deleteById(Long id) {
		goodsRestrictedCustomerRelaRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除限售配置会员关系表
	 * @author baijz
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> goodsRestrictedCustomerRelaRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询限售配置会员关系表
	 * @author baijz
	 */
	public GoodsRestrictedCustomerRela getById(Long id){
		return goodsRestrictedCustomerRelaRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询限售配置会员关系表
	 * @author baijz
	 */
	public Page<GoodsRestrictedCustomerRela> page(GoodsRestrictedCustomerRelaQueryRequest queryReq){
		return goodsRestrictedCustomerRelaRepository.findAll(
				GoodsRestrictedCustomerRelaWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询限售配置会员关系表
	 * @author baijz
	 */
	public List<GoodsRestrictedCustomerRela> list(GoodsRestrictedCustomerRelaQueryRequest queryReq){
		return goodsRestrictedCustomerRelaRepository.findAll(
				GoodsRestrictedCustomerRelaWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author baijz
	 */
	public GoodsRestrictedCustomerRelaVO wrapperVo(GoodsRestrictedCustomerRela goodsRestrictedCustomerRela) {
		if (goodsRestrictedCustomerRela != null){
			GoodsRestrictedCustomerRelaVO goodsRestrictedCustomerRelaVO=new GoodsRestrictedCustomerRelaVO();
			KsBeanUtil.copyPropertiesThird(goodsRestrictedCustomerRela,goodsRestrictedCustomerRelaVO);
			return goodsRestrictedCustomerRelaVO;
		}
		return null;
	}

	@Transactional(rollbackFor = Exception.class)
	public int deleteAllByRestrictedSaleIds(List<Long> restrictedSaleIds){
		return goodsRestrictedCustomerRelaRepository.deleteAllByRestrictedSaleIds(restrictedSaleIds);
	}
}
