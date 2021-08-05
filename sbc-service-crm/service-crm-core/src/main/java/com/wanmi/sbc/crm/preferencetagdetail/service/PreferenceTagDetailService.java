package com.wanmi.sbc.crm.preferencetagdetail.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.crm.preferencetagdetail.repository.PreferenceTagDetailRepository;
import com.wanmi.sbc.crm.preferencetagdetail.model.root.PreferenceTagDetail;
import com.wanmi.sbc.crm.api.request.preferencetagdetail.PreferenceTagDetailQueryRequest;
import com.wanmi.sbc.crm.bean.vo.PreferenceTagDetailVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>偏好标签明细业务逻辑</p>
 * @author dyt
 * @date 2020-03-11 14:58:07
 */
@Service("PreferenceTagDetailService")
public class PreferenceTagDetailService {
	@Autowired
	private PreferenceTagDetailRepository preferenceTagDetailRepository;

	/**
	 * 新增偏好标签明细
	 * @author dyt
	 */
	@Transactional
	public PreferenceTagDetail add(PreferenceTagDetail entity) {
		preferenceTagDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 修改偏好标签明细
	 * @author dyt
	 */
	@Transactional
	public PreferenceTagDetail modify(PreferenceTagDetail entity) {
		preferenceTagDetailRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除偏好标签明细
	 * @author dyt
	 */
	@Transactional
	public void deleteById(Long id) {
		preferenceTagDetailRepository.deleteById(id);
	}

	/**
	 * 批量删除偏好标签明细
	 * @author dyt
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		preferenceTagDetailRepository.deleteByIdIn(ids);
	}

	/**
	 * 单个查询偏好标签明细
	 * @author dyt
	 */
	public PreferenceTagDetail getOne(Long id){
		return preferenceTagDetailRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "偏好标签明细不存在"));
	}

	/**
	 * 分页查询偏好标签明细
	 * @author dyt
	 */
	public Page<PreferenceTagDetail> page(PreferenceTagDetailQueryRequest queryReq){
		return preferenceTagDetailRepository.findAll(
				PreferenceTagDetailWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询偏好标签明细
	 * @author dyt
	 */
	public List<PreferenceTagDetail> list(PreferenceTagDetailQueryRequest queryReq){
		return preferenceTagDetailRepository.findAll(PreferenceTagDetailWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author dyt
	 */
	public PreferenceTagDetailVO wrapperVo(PreferenceTagDetail preferenceTagDetail) {
		if (preferenceTagDetail != null){
			PreferenceTagDetailVO preferenceTagDetailVO = KsBeanUtil.convert(preferenceTagDetail, PreferenceTagDetailVO.class);
			return preferenceTagDetailVO;
		}
		return null;
	}

	/**
	 * 列表查询偏好标签明细
	 * @author dyt
	 */
	public int countAllByIds(PreferenceTagDetailQueryRequest queryReq){
		return preferenceTagDetailRepository.countAllByIdIn(queryReq.getIdList());
	}
}

