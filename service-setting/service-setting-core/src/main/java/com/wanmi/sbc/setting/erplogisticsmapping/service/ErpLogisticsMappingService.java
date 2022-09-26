package com.wanmi.sbc.setting.erplogisticsmapping.service;

import com.wanmi.sbc.setting.bean.vo.ErpLogisticsMappingVO;
import com.wanmi.sbc.setting.erplogisticsmapping.model.root.ErpLogisticsMapping;
import com.wanmi.sbc.setting.erplogisticsmapping.repository.ErpLogisticsMappingRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.util.List;

/**
 * <p>erp系统物流编码映射业务逻辑</p>
 * @author weiwenhao
 * @date 2021-04-03 13:38:51
 */
@Service("ErpLogisticsMappingService")
public class ErpLogisticsMappingService {
	@Autowired
	private ErpLogisticsMappingRepository erpLogisticsMappingRepository;
	
	/** 
	 * 新增erp系统物流编码映射
	 * @author weiwenhao
	 */
	@Transactional
	public ErpLogisticsMapping add(ErpLogisticsMapping entity) {
		erpLogisticsMappingRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改erp系统物流编码映射
	 * @author weiwenhao
	 */
	@Transactional
	public ErpLogisticsMapping modify(ErpLogisticsMapping entity) {
		erpLogisticsMappingRepository.save(entity);
		return entity;
	}


	/** 
	 * 单个查询erp系统物流编码映射
	 * @author weiwenhao
	 */
	public ErpLogisticsMapping getByErpLogisticsCode(String erpLogisticsCode){
		return erpLogisticsMappingRepository.findByErpLogisticsCode(erpLogisticsCode);
	}


	/**
	 * 单个查询wm系统物流编码映射
	 * @author weiwenhao
	 */
	public ErpLogisticsMapping getWmLogisticsCode(String wmLogisticsCode) {
		List<ErpLogisticsMapping> byWmLogisticsCode = erpLogisticsMappingRepository.findByWmLogisticsCode(wmLogisticsCode);
		if (CollectionUtils.isEmpty(byWmLogisticsCode)) {
			return null;
		}
		return byWmLogisticsCode.get(0);
	}

	/**
	 * 将实体包装成VO
	 * @author weiwenhao
	 */
	public ErpLogisticsMappingVO wrapperVo(ErpLogisticsMapping erpLogisticsMapping) {
		if (erpLogisticsMapping != null){
			ErpLogisticsMappingVO erpLogisticsMappingVO=new ErpLogisticsMappingVO();
			KsBeanUtil.copyPropertiesThird(erpLogisticsMapping,erpLogisticsMappingVO);
			return erpLogisticsMappingVO;
		}
		return null;
	}
	

}
