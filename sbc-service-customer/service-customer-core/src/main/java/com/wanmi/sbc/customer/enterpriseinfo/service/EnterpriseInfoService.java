package com.wanmi.sbc.customer.enterpriseinfo.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.enterpriseinfo.repository.EnterpriseInfoRepository;
import com.wanmi.sbc.customer.enterpriseinfo.model.root.EnterpriseInfo;
import com.wanmi.sbc.customer.api.request.enterpriseinfo.EnterpriseInfoQueryRequest;
import com.wanmi.sbc.customer.bean.vo.EnterpriseInfoVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>企业信息表业务逻辑</p>
 * @author TangLian
 * @date 2020-03-02 19:05:06
 */
@Service("EnterpriseInfoService")
public class EnterpriseInfoService {
	@Autowired
	private EnterpriseInfoRepository enterpriseInfoRepository;

	/**
	 * 新增企业信息表
	 * @author TangLian
	 */
	@Transactional
	public EnterpriseInfo add(EnterpriseInfo entity) {
		entity.setDelFlag(DeleteFlag.NO);
		entity.setCreateTime(LocalDateTime.now());
		enterpriseInfoRepository.save(entity);
		return entity;
	}

	/**
	 * 修改企业信息表
	 * @author TangLian
	 */
	@Transactional
	public EnterpriseInfo modify(EnterpriseInfo entity) {
		entity.setUpdateTime(LocalDateTime.now());
		enterpriseInfoRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除企业信息表
	 * @author TangLian
	 */
	@Transactional
	public void deleteById(EnterpriseInfo entity) {
		enterpriseInfoRepository.save(entity);
	}

	/**
	 * 批量删除企业信息表
	 * @author TangLian
	 */
	@Transactional
	public void deleteByIdList(List<EnterpriseInfo> infos) {
		enterpriseInfoRepository.saveAll(infos);
	}

	/**
	 * 单个查询企业信息表
	 * @author TangLian
	 */
	public EnterpriseInfo getOne(String id){
		return enterpriseInfoRepository.findByEnterpriseIdAndDelFlag(id, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "企业信息表不存在"));
	}

	/**
	 * 分页查询企业信息表
	 * @author TangLian
	 */
	public Page<EnterpriseInfo> page(EnterpriseInfoQueryRequest queryReq){
		return enterpriseInfoRepository.findAll(
				EnterpriseInfoWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询企业信息表
	 * @author TangLian
	 */
	public List<EnterpriseInfo> list(EnterpriseInfoQueryRequest queryReq){
		return enterpriseInfoRepository.findAll(EnterpriseInfoWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 根据会员idList批量查询企业信息表
	 * @author zhongjichuan
	 */
	public List<EnterpriseInfo> listByCustomerIds(List<String> customerIdList){
		return enterpriseInfoRepository.findAllByCustomerIdInAndDelFlag(customerIdList, DeleteFlag.NO);
	}

	/**
	 * 将实体包装成VO
	 * @author TangLian
	 */
	public EnterpriseInfoVO wrapperVo(EnterpriseInfo enterpriseInfo) {
		if (enterpriseInfo != null){
			EnterpriseInfoVO enterpriseInfoVO = KsBeanUtil.convert(enterpriseInfo, EnterpriseInfoVO.class);
			return enterpriseInfoVO;
		}
		return null;
	}

	/**
	 * 用户id查询企业信息
	 * @param customerId
	 * @return
	 */
	public EnterpriseInfo getByCustomerId(String customerId) {
		return enterpriseInfoRepository.findByCustomerIdAndDelFlag(customerId,DeleteFlag.NO).orElse(null);
	}
}

