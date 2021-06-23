package com.wanmi.sbc.setting.storeexpresscompanyrela.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaListRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.storeexpresscompanyrela.repository.StoreExpressCompanyRelaRepository;
import com.wanmi.sbc.setting.storeexpresscompanyrela.model.root.StoreExpressCompanyRela;
import com.wanmi.sbc.setting.api.request.storeexpresscompanyrela.StoreExpressCompanyRelaQueryRequest;
import com.wanmi.sbc.setting.bean.vo.StoreExpressCompanyRelaVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>店铺快递公司关联表业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:12:13
 */
@Service("StoreExpressCompanyRelaService")
public class StoreExpressCompanyRelaService {
	@Autowired
	private StoreExpressCompanyRelaRepository storeExpressCompanyRelaRepository;
	
	/** 
	 * 新增店铺快递公司关联表
	 * @author lq
	 */
	@Transactional
	public StoreExpressCompanyRela add(StoreExpressCompanyRela entity) {
		storeExpressCompanyRelaRepository.saveAndFlush(entity);
		return entity;
	}
	
	/** 
	 * 修改店铺快递公司关联表
	 * @author lq
	 */
	@Transactional
	public StoreExpressCompanyRela modify(StoreExpressCompanyRela entity) {
		storeExpressCompanyRelaRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除店铺快递公司关联表
	 * @author lq
	 */
	@Transactional
	public void deleteById(Long id) {
		storeExpressCompanyRelaRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除店铺快递公司关联表
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		ids.forEach(id -> storeExpressCompanyRelaRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询店铺快递公司关联表
	 * @author lq
	 */
	public StoreExpressCompanyRela getById(Long id){
		return storeExpressCompanyRelaRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询店铺快递公司关联表
	 * @author lq
	 */
	public Page<StoreExpressCompanyRela> page(StoreExpressCompanyRelaQueryRequest queryReq){
		return storeExpressCompanyRelaRepository.findAll(
				StoreExpressCompanyRelaWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询店铺快递公司关联表
	 * @author lq
	 */
	public List<StoreExpressCompanyRela> list(StoreExpressCompanyRelaListRequest queryReq){
		return storeExpressCompanyRelaRepository.findStoreExpressCompanyRelasByStoreId(queryReq.getStoreId());
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public StoreExpressCompanyRelaVO wrapperVo(StoreExpressCompanyRela storeExpressCompanyRela) {
		if (storeExpressCompanyRela != null){
			StoreExpressCompanyRelaVO storeExpressCompanyRelaVO=new StoreExpressCompanyRelaVO();
			KsBeanUtil.copyPropertiesThird(storeExpressCompanyRela,storeExpressCompanyRelaVO);
			storeExpressCompanyRelaVO.setExpressCompany(KsBeanUtil.convert(storeExpressCompanyRela.getExpressCompany(), ExpressCompanyVO.class));
			return storeExpressCompanyRelaVO;
		}
		return null;
	}
}
