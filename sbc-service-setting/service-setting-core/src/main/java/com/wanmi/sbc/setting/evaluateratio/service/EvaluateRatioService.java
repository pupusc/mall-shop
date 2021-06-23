package com.wanmi.sbc.setting.evaluateratio.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.evaluateratio.repository.EvaluateRatioRepository;
import com.wanmi.sbc.setting.evaluateratio.model.root.EvaluateRatio;
import com.wanmi.sbc.setting.api.request.evaluateratio.EvaluateRatioQueryRequest;
import com.wanmi.sbc.setting.bean.vo.EvaluateRatioVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>商品评价系数设置业务逻辑</p>
 * @author liutao
 * @date 2019-02-27 15:53:40
 */
@Service("EvaluateRatioService")
public class EvaluateRatioService {
	@Autowired
	private EvaluateRatioRepository evaluateRatioRepository;
	
	/** 
	 * 新增商品评价系数设置
	 * @author liutao
	 */
	@Transactional
	public EvaluateRatio add(EvaluateRatio entity) {
		evaluateRatioRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改商品评价系数设置
	 * @author liutao
	 */
	@Transactional
	public EvaluateRatio modify(EvaluateRatio entity) {
		evaluateRatioRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除商品评价系数设置
	 * @author liutao
	 */
	@Transactional
	public void deleteById(String id) {
		evaluateRatioRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除商品评价系数设置
	 * @author liutao
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		evaluateRatioRepository.deleteAll(ids.stream().map(id -> {
			EvaluateRatio entity = new EvaluateRatio();
			entity.setRatioId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询商品评价系数设置
	 * @author liutao
	 */
	public EvaluateRatio getById(String id){
		return evaluateRatioRepository.findById(id).get();
	}
	
	/** 
	 * 分页查询商品评价系数设置
	 * @author liutao
	 */
	public Page<EvaluateRatio> page(EvaluateRatioQueryRequest queryReq){
		return evaluateRatioRepository.findAll(
				EvaluateRatioWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询商品评价系数设置
	 * @author liutao
	 */
	public List<EvaluateRatio> list(EvaluateRatioQueryRequest queryReq){
		return evaluateRatioRepository.findAll(EvaluateRatioWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author liutao
	 */
	public EvaluateRatioVO wrapperVo(EvaluateRatio evaluateRatio) {
		if (evaluateRatio != null){
			EvaluateRatioVO evaluateRatioVO=new EvaluateRatioVO();
			KsBeanUtil.copyPropertiesThird(evaluateRatio,evaluateRatioVO);
			return evaluateRatioVO;
		}
		return null;
	}

	/**
	 * 获取一条配置数据（有且有且一条）
	 *
	 * @return
	 */
	public EvaluateRatio findOne(){
		List<EvaluateRatio> evaluateRatioList = evaluateRatioRepository.findAll();
		if (CollectionUtils.isEmpty(evaluateRatioList)){
			return null;
		}

		return evaluateRatioList.stream().findFirst().get();
	}
}
