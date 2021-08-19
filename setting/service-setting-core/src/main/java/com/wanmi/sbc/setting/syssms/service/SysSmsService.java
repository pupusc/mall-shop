package com.wanmi.sbc.setting.syssms.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.setting.api.response.syssms.SmsSupplierRopResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.syssms.repository.SysSmsRepository;
import com.wanmi.sbc.setting.syssms.model.root.SysSms;
import com.wanmi.sbc.setting.api.request.syssms.SysSmsQueryRequest;
import com.wanmi.sbc.setting.bean.vo.SysSmsVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>系统短信配置业务逻辑</p>
 * @author lq
 * @date 2019-11-05 16:13:47
 */
@Service("SysSmsService")
public class SysSmsService {
	@Autowired
	private SysSmsRepository sysSmsRepository;
	
	/** 
	 * 新增系统短信配置
	 * @author lq
	 */
	@Transactional
	public SysSms add(SysSms entity) {
		sysSmsRepository.saveAndFlush(entity);
		return entity;
	}
	
	/** 
	 * 修改系统短信配置
	 * @author lq
	 */
	@Transactional
	public SysSms modify(SysSms entity) {
		sysSmsRepository.saveAndFlush(entity);
		return entity;
	}

	/**
	 * 单个删除系统短信配置
	 * @author lq
	 */
	@Transactional
	public void deleteById(String id) {
		sysSmsRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除系统短信配置
	 * @author lq
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		ids.forEach(id -> sysSmsRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询系统短信配置
	 * @author lq
	 */
	public SysSms getById(String id){
		return sysSmsRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询系统短信配置
	 * @author lq
	 */
	public Page<SysSms> page(SysSmsQueryRequest queryReq){
		return sysSmsRepository.findAll(
				SysSmsWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询系统短信配置
	 * @author lq
	 */
	public List<SysSms> list(){
		return sysSmsRepository.findAll();
	}

	/**
	 * 将实体包装成VO
	 * @author lq
	 */
	public SysSmsVO wrapperVo(SysSms sysSms) {
		if (sysSms != null){
			SysSmsVO sysSmsVO=new SysSmsVO();
			KsBeanUtil.copyPropertiesThird(sysSms,sysSmsVO);
			return sysSmsVO;
		}
		return null;
	}

	public SmsSupplierRopResponse wrapperSmsSupplierRopResponse(SysSmsVO sysSmsVO) {
		SmsSupplierRopResponse response = new SmsSupplierRopResponse();
		if (sysSmsVO != null){
		  return  response.builder()
				  .id(sysSmsVO.getSmsId())
				  .account(sysSmsVO.getSmsName())
				  .name(sysSmsVO.getSmsProvider())
				  .password(sysSmsVO.getSmsPass())
				  .siteAddress(sysSmsVO.getSmsAddress())
				  .interfaceUrl(sysSmsVO.getSmsUrl())
				  .smsContent(sysSmsVO.getSmsContent())
				  .template(sysSmsVO.getSmsGateway())
				  .status(sysSmsVO.getIsOpen()==null?"0":sysSmsVO.getIsOpen().toString())
				  .createTime(sysSmsVO.getCreateTime())
				  .build();
		}
		return response;
	}
}
