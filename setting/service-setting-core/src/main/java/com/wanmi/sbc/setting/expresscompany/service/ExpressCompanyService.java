package com.wanmi.sbc.setting.expresscompany.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.expresscompany.repository.ExpressCompanyRepository;
import com.wanmi.sbc.setting.expresscompany.model.root.ExpressCompany;
import com.wanmi.sbc.setting.api.request.expresscompany.ExpressCompanyQueryRequest;
import com.wanmi.sbc.setting.bean.vo.ExpressCompanyVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>物流公司业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:10:00
 */
@Service("ExpressCompanyService")
public class ExpressCompanyService {
	@Autowired
	private ExpressCompanyRepository expressCompanyRepository;
	
	/** 
	 * 新增物流公司
	 * @author lq
	 */
	@Transactional
	public ExpressCompany add(ExpressCompany entity) {
		int count = expressCompanyRepository.countExpressCompany();
		if(count>= Constants.S2B_EXPRESS_COMPANY_COUNT){
			throw new SbcRuntimeException(CommonErrorCode.EXPRESS_MAX_COUNT_ERROR, String.valueOf(Constants.S2B_EXPRESS_COMPANY_COUNT));
		}
		expressCompanyRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改物流公司
	 * @author lq
	 */
	@Transactional
	public ExpressCompany modify(ExpressCompany entity) {
		expressCompanyRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除物流公司
	 * @author lq
	 */
	@Transactional
	public void deleteById(Long id) {
		expressCompanyRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除物流公司
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		expressCompanyRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询物流公司
	 * @author lq
	 */
	public ExpressCompany getById(Long id){
		return expressCompanyRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询物流公司
	 * @author lq
	 */
	public Page<ExpressCompany> page(ExpressCompanyQueryRequest queryReq){
		return expressCompanyRepository.findAll(
				ExpressCompanyWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询物流公司(未删除的)
	 * @author lq
	 */
	public List<ExpressCompany> list(){
		return expressCompanyRepository.findExpressCompanyByDelFlag(DeleteFlag.NO);
	}

	public List<ExpressCompany>listByIds (List<Long> ids){
		return expressCompanyRepository.findAllById(ids);
	}

	/**
	 * 批量新增
	 * @param expressCompanies
	 * @return
	 */
	public List<ExpressCompany> addBatch(List<ExpressCompany> expressCompanies){
		return expressCompanyRepository.saveAll(expressCompanies);
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public ExpressCompanyVO wrapperVo(ExpressCompany expressCompany) {
		if (expressCompany != null){
			ExpressCompanyVO expressCompanyVO=new ExpressCompanyVO();
			KsBeanUtil.copyPropertiesThird(expressCompany,expressCompanyVO);
			return expressCompanyVO;
		}
		return null;
	}
}
