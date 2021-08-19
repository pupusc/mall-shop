package com.wanmi.sbc.customer.fdpaidcast.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.fdpaidcast.repository.FdPaidCastRepository;
import com.wanmi.sbc.customer.fdpaidcast.model.root.FdPaidCast;
import com.wanmi.sbc.customer.api.request.fdpaidcast.FdPaidCastQueryRequest;
import com.wanmi.sbc.customer.bean.vo.FdPaidCastVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.List;

/**
 * <p>樊登付费类型 映射商城付费类型业务逻辑</p>
 * @author tzx
 * @date 2021-01-29 14:13:37
 */
@Service("FdPaidCastService")
public class FdPaidCastService {
	@Autowired
	private FdPaidCastRepository fdPaidCastRepository;

	/**
	 * 新增樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	@Transactional
	public FdPaidCast add(FdPaidCast entity) {
		fdPaidCastRepository.save(entity);
		return entity;
	}

	/**
	 * 修改樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	@Transactional
	public FdPaidCast modify(FdPaidCast entity) {
		fdPaidCastRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	@Transactional
	public void deleteById(FdPaidCast entity) {
		fdPaidCastRepository.save(entity);
	}

	/**
	 * 批量删除樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	@Transactional
	public void deleteByIdList(List<FdPaidCast> infos) {
		fdPaidCastRepository.saveAll(infos);
	}

	/**
	 * 单个查询樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	public FdPaidCast getOne(Long id){
		return fdPaidCastRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "樊登付费类型 映射商城付费类型不存在"));
	}

	/**
	 * 分页查询樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	public Page<FdPaidCast> page(FdPaidCastQueryRequest queryReq){
		return fdPaidCastRepository.findAll(
				FdPaidCastWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询樊登付费类型 映射商城付费类型
	 * @author tzx
	 */
	public List<FdPaidCast> list(FdPaidCastQueryRequest queryReq){
		return fdPaidCastRepository.findAll(FdPaidCastWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author tzx
	 */
	public FdPaidCastVO wrapperVo(FdPaidCast fdPaidCast) {
		if (fdPaidCast != null){
			FdPaidCastVO fdPaidCastVO = KsBeanUtil.convert(fdPaidCast, FdPaidCastVO.class);
			return fdPaidCastVO;
		}
		return null;
	}
}

