package com.wanmi.sbc.message.pushcustomerenable.service;

import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.request.pushcustomerenable.PushCustomerEnableQueryRequest;
import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.message.bean.vo.PushCustomerEnableVO;
import com.wanmi.sbc.message.pushcustomerenable.model.root.PushCustomerEnable;
import com.wanmi.sbc.message.pushcustomerenable.repository.PushCustomerEnableRepository;
import com.wanmi.sbc.message.pushsendnode.repository.PushSendNodeRepository;
import com.wanmi.sbc.message.pushsendnode.service.PushSendNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>会员推送通知开关业务逻辑</p>
 * @author Bob
 * @date 2020-01-07 15:31:47
 */
@Service("PushCustomerEnableService")
public class PushCustomerEnableService {
	@Autowired
	private PushCustomerEnableRepository pushCustomerEnableRepository;

	@Autowired
	private PushSendNodeRepository pushSendNodeRepository;

	/**
	 * 新增会员推送通知开关
	 * @author Bob
	 */
	@Transactional
	public PushCustomerEnable add(PushCustomerEnable entity) {
		pushCustomerEnableRepository.save(entity);
		return entity;
	}

	/**
	 * 修改会员推送通知开关
	 * @author Bob
	 */
	@Transactional
	public PushCustomerEnable modify(PushCustomerEnable entity) {
		Optional<PushCustomerEnable> pushCustomerEnableOptional = pushCustomerEnableRepository.queryByCustomerId(entity.getCustomerId());
		if(pushCustomerEnableOptional.isPresent()){
			PushCustomerEnable pushCustomerEnable = pushCustomerEnableOptional.get();
			if(entity.getReturnOrderProgressRate() != null){
				pushCustomerEnable.setReturnOrderProgressRate(entity.getReturnOrderProgressRate());
			}
			if(entity.getAccountSecurity() != null){
				pushCustomerEnable.setAccountSecurity(entity.getAccountSecurity());
			}
			if(entity.getAccountAssets() != null){
				pushCustomerEnable.setAccountAssets(entity.getAccountAssets());
			}
			if(entity.getEnableStatus() != null){
				pushCustomerEnable.setEnableStatus(entity.getEnableStatus());
			}
			if(entity.getDistribution() != null){
				pushCustomerEnable.setDistribution(entity.getDistribution());
			}
			if(entity.getOrderProgressRate() != null){
				pushCustomerEnable.setOrderProgressRate(entity.getOrderProgressRate());
			}
			pushCustomerEnableRepository.save(pushCustomerEnable);
		}

		return entity;
	}

	/**
	 * 单个删除会员推送通知开关
	 * @author Bob
	 */
	@Transactional
	public void deleteById(String id) {
		pushCustomerEnableRepository.deleteById(id);
	}

	/**
	 * 单个查询会员推送通知开关
	 * @author Bob
	 */
	public PushCustomerEnable getOne(String id){
		PushCustomerEnable pushCustomerEnable = pushCustomerEnableRepository.findById(id).orElse(null);
		if(Objects.nonNull(pushCustomerEnable)){
			//各个分类通知开关在boss端开关都关闭的情况下，不展示该分类通知开关
			int accountSecurityNum = pushSendNodeRepository.queryOpenNum(NodeType.ACCOUNT_SECURITY.toValue());
			int orderProgressNum = pushSendNodeRepository.queryOpenNum(NodeType.ORDER_PROGRESS_RATE.toValue());
			int accountAssetsNum = pushSendNodeRepository.queryOpenNum(NodeType.ACCOUNT_ASSETS.toValue());
			int returnNum = pushSendNodeRepository.queryOpenNum(NodeType.RETURN_ORDER_PROGRESS_RATE.toValue());
			int distributionNum = pushSendNodeRepository.queryOpenNum(NodeType.DISTRIBUTION.toValue());
			if(accountAssetsNum == 0){
				pushCustomerEnable.setAccountAssets(null);
			}
			if(accountSecurityNum == 0){
				pushCustomerEnable.setAccountSecurity(null);
			}
			if(orderProgressNum == 0){
				pushCustomerEnable.setOrderProgressRate(null);
			}
			if(returnNum == 0){
				pushCustomerEnable.setReturnOrderProgressRate(null);
			}
			if(distributionNum == 0){
				pushCustomerEnable.setDistribution(null);
			}
		}

		return pushCustomerEnable;
	}

	/**
	 * 分页查询会员推送通知开关
	 * @author Bob
	 */
	public Page<PushCustomerEnable> page(PushCustomerEnableQueryRequest queryReq){
		return pushCustomerEnableRepository.findAll(
				PushCustomerEnableWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询会员推送通知开关
	 * @author Bob
	 */
	public List<PushCustomerEnable> list(PushCustomerEnableQueryRequest queryReq){
		return pushCustomerEnableRepository.findAll(PushCustomerEnableWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author Bob
	 */
	public PushCustomerEnableVO wrapperVo(PushCustomerEnable pushCustomerEnable) {
		if (pushCustomerEnable != null){
			PushCustomerEnableVO pushCustomerEnableVO = KsBeanUtil.convert(pushCustomerEnable, PushCustomerEnableVO.class);
			return pushCustomerEnableVO;
		}
		return null;
	}
}

