package com.wanmi.sbc.setting.operatedatalog.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.setting.operatedatalog.repository.OperateDataLogRepository;
import com.wanmi.sbc.setting.operatedatalog.model.root.OperateDataLog;
import com.wanmi.sbc.setting.api.request.operatedatalog.OperateDataLogQueryRequest;
import com.wanmi.sbc.setting.bean.vo.OperateDataLogVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import java.util.List;

/**
 * <p>系统日志业务逻辑</p>
 * @author guanfl
 * @date 2020-04-21 14:57:15
 */
@Service("OperateDataLogService")
public class OperateDataLogService {
	@Autowired
	private OperateDataLogRepository operateDataLogRepository;
	
	/** 
	 * 新增系统日志
	 * @author guanfl
	 */
	@Transactional
	public OperateDataLog add(OperateDataLog entity) {
		operateDataLogRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改系统日志
	 * @author guanfl
	 */
	@Transactional
	public OperateDataLog modify(OperateDataLog entity) {
		operateDataLogRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除系统日志
	 * @author guanfl
	 */
	@Transactional
	public void deleteById(Long id) {
		operateDataLogRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除系统日志
	 * @author guanfl
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		operateDataLogRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询系统日志
	 * @author guanfl
	 */
	public OperateDataLog getById(Long id){
		return operateDataLogRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询系统日志
	 * @author guanfl
	 */
	public Page<OperateDataLog> page(OperateDataLogQueryRequest queryReq){
		return operateDataLogRepository.findAll(
				OperateDataLogWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询系统日志
	 * @author guanfl
	 */
	public List<OperateDataLog> list(OperateDataLogQueryRequest queryReq){
		return operateDataLogRepository.findAll(
				OperateDataLogWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author guanfl
	 */
	public OperateDataLogVO wrapperVo(OperateDataLog operateDataLog) {
		if (operateDataLog != null){
			OperateDataLogVO operateDataLogVO=new OperateDataLogVO();
			KsBeanUtil.copyPropertiesThird(operateDataLog,operateDataLogVO);
			return operateDataLogVO;
		}
		return null;
	}

	@Transactional
    public void deleteByOperateId(String operateId) {
		operateDataLogRepository.deleteByOperateId(operateId);
    }

	@Transactional
	public void synDataLog(String supplierGoodsId, String providerGoodsId) {
		OperateDataLog operateDataLog = operateDataLogRepository.getByOperateId(providerGoodsId);
		OperateDataLog newOperateDataLog = new OperateDataLog();
		KsBeanUtil.copyPropertiesThird(operateDataLog,newOperateDataLog);
		newOperateDataLog.setId(operateDataLog.getId());
		newOperateDataLog.setOperateId(supplierGoodsId);
		operateDataLogRepository.save(newOperateDataLog);
	}
}
