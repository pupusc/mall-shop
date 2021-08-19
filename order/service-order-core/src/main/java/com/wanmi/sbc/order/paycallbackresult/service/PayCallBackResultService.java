package com.wanmi.sbc.order.paycallbackresult.service;

import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.handler.aop.MasterRouteOnly;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.order.api.request.paycallbackresult.PayCallBackResultQueryRequest;
import com.wanmi.sbc.order.bean.constant.ConstantContent;
import com.wanmi.sbc.order.bean.enums.PayCallBackResultStatus;
import com.wanmi.sbc.order.bean.vo.PayCallBackResultVO;
import com.wanmi.sbc.order.paycallbackresult.model.root.PayCallBackResult;
import com.wanmi.sbc.order.paycallbackresult.repository.PayCallBackResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * <p>支付回调结果业务逻辑</p>
 * @author lvzhenwei
 * @date 2020-07-01 17:34:23
 */
@Service("PayCallBackResultService")
public class PayCallBackResultService {
	@Autowired
	private PayCallBackResultRepository payCallBackResultRepository;

	/**
	 * 新增支付回调结果
	 * @author lvzhenwei
	 */
	@Transactional
	public PayCallBackResult add(PayCallBackResult entity) {
		try {
			payCallBackResultRepository.save(entity);
		} catch (Exception e) {
			try{
				if(((SQLIntegrityConstraintViolationException) e.getCause().getCause()).getSQLState().equals(ConstantContent.UNI_KEY_ERROR_CODE)){
					throw new SbcRuntimeException(ErrorCodeConstant.PAY_CALL_BACK_RESULT_EXIT);
				}
			} catch (SbcRuntimeException ex){
				throw ex;
			} catch (Exception ep) {
				throw e;
			}
			throw e;
		}
		return entity;
	}

	/**
	 * 修改支付回调结果
	 * @author lvzhenwei
	 */
	@Transactional
	public PayCallBackResult modify(PayCallBackResult entity) {
		payCallBackResultRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除支付回调结果
	 * @author lvzhenwei
	 */
	@Transactional
	public void deleteById(Long id) {
		payCallBackResultRepository.deleteById(id);
	}

	/**
	 * 单个查询支付回调结果
	 * @author lvzhenwei
	 */
	public PayCallBackResult getOne(Long id){
		return payCallBackResultRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "支付回调结果不存在"));
	}

	/**
	 * 分页查询支付回调结果
	 * @author lvzhenwei
	 */
	@MasterRouteOnly
	public Page<PayCallBackResult> page(PayCallBackResultQueryRequest queryReq){
		return payCallBackResultRepository.findAll(
				PayCallBackResultWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询支付回调结果
	 * @author lvzhenwei
	 */
	@MasterRouteOnly
	public List<PayCallBackResult> list(PayCallBackResultQueryRequest queryReq){
		return payCallBackResultRepository.findAll(PayCallBackResultWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 根据businessId更新支付回调状态
	 * @param businessId
	 * @param resultStatus
	 * @return
	 */
	@Transactional
	public int updateStatus(String businessId, PayCallBackResultStatus resultStatus){
		if(resultStatus==PayCallBackResultStatus.FAILED){
			return payCallBackResultRepository.updateStatusFailedByBusinessId(businessId,resultStatus);
		} else {
			return payCallBackResultRepository.updateStatusSuccessByBusinessId(businessId,resultStatus);
		}
	}

	/**
	 * 将实体包装成VO
	 * @author lvzhenwei
	 */
	public PayCallBackResultVO wrapperVo(PayCallBackResult payCallBackResult) {
		if (payCallBackResult != null){
			PayCallBackResultVO payCallBackResultVO = KsBeanUtil.convert(payCallBackResult, PayCallBackResultVO.class);
			return payCallBackResultVO;
		}
		return null;
	}
}

