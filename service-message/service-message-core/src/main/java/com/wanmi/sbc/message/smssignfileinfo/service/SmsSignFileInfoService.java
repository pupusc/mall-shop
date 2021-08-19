package com.wanmi.sbc.message.smssignfileinfo.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.message.smssignfileinfo.repository.SmsSignFileInfoRepository;
import com.wanmi.sbc.message.smssignfileinfo.model.root.SmsSignFileInfo;
import com.wanmi.sbc.message.api.request.smssignfileinfo.SmsSignFileInfoQueryRequest;
import com.wanmi.sbc.message.bean.vo.SmsSignFileInfoVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>短信签名文件信息业务逻辑</p>
 * @author lvzhenwei
 * @date 2019-12-04 14:19:35
 */
@Service("SmsSignFileInfoService")
public class SmsSignFileInfoService {
	@Autowired
	private SmsSignFileInfoRepository smsSignFileInfoRepository;
	
	/** 
	 * 新增短信签名文件信息
	 * @author lvzhenwei
	 */
	@Transactional
	public SmsSignFileInfo add(SmsSignFileInfo entity) {
		smsSignFileInfoRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改短信签名文件信息
	 * @author lvzhenwei
	 */
	@Transactional
	public SmsSignFileInfo modify(SmsSignFileInfo entity) {
		smsSignFileInfoRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除短信签名文件信息
	 * @author lvzhenwei
	 */
	@Transactional
	public void deleteById(Long id) {
		smsSignFileInfoRepository.deleteByBeanId(id);
	}
	
	/** 
	 * 批量删除短信签名文件信息
	 * @author lvzhenwei
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		smsSignFileInfoRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询短信签名文件信息
	 * @author lvzhenwei
	 */
	public SmsSignFileInfo getById(Long id){
		return smsSignFileInfoRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询短信签名文件信息
	 * @author lvzhenwei
	 */
	public Page<SmsSignFileInfo> page(SmsSignFileInfoQueryRequest queryReq){
		return smsSignFileInfoRepository.findAll(
				SmsSignFileInfoWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询短信签名文件信息
	 * @author lvzhenwei
	 */
	public List<SmsSignFileInfo> list(SmsSignFileInfoQueryRequest queryReq){
		return smsSignFileInfoRepository.findAll(
				SmsSignFileInfoWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author lvzhenwei
	 */
	public SmsSignFileInfoVO wrapperVo(SmsSignFileInfo smsSignFileInfo) {
		if (smsSignFileInfo != null){
			SmsSignFileInfoVO smsSignFileInfoVO=new SmsSignFileInfoVO();
			KsBeanUtil.copyPropertiesThird(smsSignFileInfo,smsSignFileInfoVO);
			return smsSignFileInfoVO;
		}
		return null;
	}
}
