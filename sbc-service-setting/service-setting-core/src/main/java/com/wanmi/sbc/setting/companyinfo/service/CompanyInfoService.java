package com.wanmi.sbc.setting.companyinfo.service;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.companyinfo.repository.CompanyInfoRepository;
import com.wanmi.sbc.setting.companyinfo.model.root.CompanyInfo;
import com.wanmi.sbc.setting.api.response.companyinfo.CompanyInfoRopResponse;
import com.wanmi.sbc.setting.api.request.companyinfo.CompanyInfoQueryRequest;
import com.wanmi.sbc.setting.bean.vo.CompanyInfoVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>公司信息业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:09:36
 */
@Service("CompanyInfoService")
public class CompanyInfoService {
	@Autowired
	private CompanyInfoRepository companyInfoRepository;
	
	/** 
	 * 新增公司信息
	 * @author lq
	 */
	@Transactional
	public CompanyInfo add(CompanyInfo entity) {
		companyInfoRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改公司信息
	 * @author lq
	 */
	@Transactional
	public CompanyInfo modify(CompanyInfo entity) {
		companyInfoRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除公司信息
	 * @author lq
	 */
	@Transactional
	public void deleteById(Long id) {
		companyInfoRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除公司信息
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		companyInfoRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询公司信息
	 * @author lq
	 */
	public CompanyInfo getById(Long id){
		return companyInfoRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询公司信息
	 * @author lq
	 */
	public Page<CompanyInfo> page(CompanyInfoQueryRequest queryReq){
		return companyInfoRepository.findAll(
				CompanyInfoWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询公司信息
	 * @author lq
	 */
	public List<CompanyInfo> list(CompanyInfoQueryRequest queryReq){
		return companyInfoRepository.findAll(
				CompanyInfoWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}


	/**
	 * 不带参查询
	 * @return
	 */
	public List<CompanyInfo> findCompanyInfos(){
		List<CompanyInfo> companyInfos = companyInfoRepository.findAll();
		//如果没有，创建一条
		if(CollectionUtils.isEmpty(companyInfos)){
			CompanyInfo companyInfo = new CompanyInfo();
			companyInfo.setCreateTime(LocalDateTime.now());
			return Collections.singletonList(companyInfoRepository.save(companyInfo));
		}
		return companyInfos;
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public CompanyInfoVO wrapperVo(CompanyInfo companyInfo) {
		if (companyInfo != null){
			CompanyInfoVO companyInfoVO=new CompanyInfoVO();
			KsBeanUtil.copyPropertiesThird(companyInfo,companyInfoVO);
			return companyInfoVO;
		}
		return null;
	}


	/**
	 * 将VO包装成 response对象
	 * @param companyInfoVO
	 * @return
	 */
	public CompanyInfoRopResponse wrappCompanyInfoRopResponse(CompanyInfoVO companyInfoVO){
		if (companyInfoVO != null){
			CompanyInfoRopResponse companyInfoRopResponse=new CompanyInfoRopResponse();
			KsBeanUtil.copyPropertiesThird(companyInfoVO,companyInfoRopResponse);
			return companyInfoRopResponse;
		}
		return null;
	}
}
