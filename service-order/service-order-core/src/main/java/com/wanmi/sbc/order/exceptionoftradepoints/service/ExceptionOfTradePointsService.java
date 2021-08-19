package com.wanmi.sbc.order.exceptionoftradepoints.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.order.exceptionoftradepoints.repository.ExceptionOfTradePointsRepository;
import com.wanmi.sbc.order.exceptionoftradepoints.model.root.ExceptionOfTradePoints;
import com.wanmi.sbc.order.api.request.exceptionoftradepoints.ExceptionOfTradePointsQueryRequest;
import com.wanmi.sbc.order.bean.vo.ExceptionOfTradePointsVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;
import java.util.List;

/**
 * <p>积分订单抵扣异常表业务逻辑</p>
 * @author caofang
 * @date 2021-03-10 18:54:25
 */
@Service("ExceptionOfTradePointsService")
public class ExceptionOfTradePointsService {
	@Autowired
	private ExceptionOfTradePointsRepository exceptionOfTradePointsRepository;

	/**
	 * 新增积分订单抵扣异常表
	 * @author caofang
	 */
	@Transactional
	public ExceptionOfTradePoints add(ExceptionOfTradePoints entity) {
		exceptionOfTradePointsRepository.save(entity);
		return entity;
	}

	/**
	 * 修改积分订单抵扣异常表
	 * @author caofang
	 */
	@Transactional
	public ExceptionOfTradePoints modify(ExceptionOfTradePoints entity) {
		exceptionOfTradePointsRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除积分订单抵扣异常表
	 * @author caofang
	 */
	@Transactional
	public void deleteById(ExceptionOfTradePoints entity) {
		exceptionOfTradePointsRepository.save(entity);
	}

	/**
	 * 批量删除积分订单抵扣异常表
	 * @author caofang
	 */
	@Transactional
	public void deleteByIdList(List<ExceptionOfTradePoints> infos) {
		exceptionOfTradePointsRepository.saveAll(infos);
	}

	/**
	 * 单个查询积分订单抵扣异常表
	 * @author caofang
	 */
	public ExceptionOfTradePoints getOne(String id){
		return exceptionOfTradePointsRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "积分订单抵扣异常表不存在"));
	}

	/**
	 * 单个查询积分订单抵扣异常表
	 * @author caofang
	 */
	public ExceptionOfTradePoints getByTradeId(String tradeId){
		return exceptionOfTradePointsRepository.findByTradeIdAndDelFlag(tradeId, DeleteFlag.NO).orElse(null);
	}

	/**
	 * 分页查询积分订单抵扣异常表
	 * @author caofang
	 */
	public Page<ExceptionOfTradePoints> page(ExceptionOfTradePointsQueryRequest queryReq){
		return exceptionOfTradePointsRepository.findAll(
				ExceptionOfTradePointsWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询积分订单抵扣异常表
	 * @author caofang
	 */
	public List<ExceptionOfTradePoints> list(ExceptionOfTradePointsQueryRequest queryReq){
		return exceptionOfTradePointsRepository.findAll(ExceptionOfTradePointsWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author caofang
	 */
	public ExceptionOfTradePointsVO wrapperVo(ExceptionOfTradePoints exceptionOfTradePoints) {
		if (exceptionOfTradePoints != null){
			ExceptionOfTradePointsVO exceptionOfTradePointsVO = KsBeanUtil.convert(exceptionOfTradePoints, ExceptionOfTradePointsVO.class);
			return exceptionOfTradePointsVO;
		}
		return null;
	}
}

