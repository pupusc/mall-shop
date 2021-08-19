package com.wanmi.sbc.message.appmessage.service;

import com.wanmi.sbc.message.api.request.appmessage.AppMessageAddRequest;
import com.wanmi.sbc.message.bean.enums.MessageType;
import com.wanmi.sbc.message.bean.enums.ReadFlag;
import com.wanmi.sbc.message.messagesend.repository.MessageSendRepository;
import com.wanmi.sbc.message.messagesendnode.repository.MessageSendNodeRepository;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.message.appmessage.repository.AppMessageRepository;
import com.wanmi.sbc.message.appmessage.model.root.AppMessage;
import com.wanmi.sbc.message.api.request.appmessage.AppMessageQueryRequest;
import com.wanmi.sbc.message.bean.vo.AppMessageVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>App站内信消息发送表业务逻辑</p>
 * @author xuyunpeng
 * @date 2020-01-06 10:53:00
 */
@Service("AppMessageService")
public class AppMessageService {
	@Autowired
	private AppMessageRepository appMessageRepository;

	@Autowired
	private MessageSendRepository messageSendRepository;

	@Autowired
	private MessageSendNodeRepository messageSendNodeRepository;

	/**
	 * 新增App站内信消息发送表
	 * @author xuyunpeng
	 */
	@Transactional
	public Integer addBatch(AppMessageAddRequest request) {
		List<String> customerIds = request.getCustomerIds();
		List<AppMessage> appMessageList = customerIds.stream().map(id -> {
			AppMessage appMessage = KsBeanUtil.convert(request.getAppMessageVO(), AppMessage.class);
			appMessage.setIsRead(ReadFlag.NO);
			appMessage.setDelFlag(DeleteFlag.NO);
			appMessage.setCustomerId(id);
			return appMessage;
		}).collect(Collectors.toList());
		appMessageRepository.saveAll(appMessageList);

		if(request.getAppMessageVO().getMessageType() == MessageType.Preferential){
			//消息任务增加发送数
			messageSendRepository.addSendSum(appMessageList.size(), request.getAppMessageVO().getJoinId());
		}else{
			//指定通知节点增加发送数
			messageSendNodeRepository.addSendSum(request.getAppMessageVO().getJoinId());
		}
		return appMessageList.size();
	}

	/**
	 * 修改App站内信消息发送表
	 * @author xuyunpeng
	 */
	@Transactional
	public AppMessage modify(AppMessage entity) {
		appMessageRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除App站内信消息发送表
	 * @author xuyunpeng
	 */
	@Transactional
	public void deleteById(String id, String customerId) {
		appMessageRepository.deleteById(id, customerId);
	}

	/**
	 * 单个查询App站内信消息发送表
	 * @author xuyunpeng
	 */
	public AppMessage getOne(String id, String customerId){
		return appMessageRepository.findByAppMessageIdAndDelFlagAndCustomerId(id, DeleteFlag.NO, customerId)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "App站内信消息发送表不存在"));
	}

	/**
	 * 分页查询App站内信消息发送表
	 * @author xuyunpeng
	 */
	public Page<AppMessage> page(AppMessageQueryRequest queryReq){
		queryReq.setDelFlag(DeleteFlag.NO);
		queryReq.setSortColumn("sendTime");
		queryReq.setSortRole("desc");
		return appMessageRepository.findAll(
				AppMessageWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询App站内信消息发送表
	 * @author xuyunpeng
	 */
	public List<AppMessage> list(AppMessageQueryRequest queryReq){
		return appMessageRepository.findAll(AppMessageWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 查询未读消息数量
	 * @param customerId
	 * @param messageType
	 * @return
	 */
	public int getMessageCount(String customerId, MessageType messageType){
		return appMessageRepository.getMessageCount(customerId, messageType);
	}

	/**
	 * 将消息置为已读
	 * @param customerId
	 * @param messageId
	 */
	@Transactional
	public void setMessageRead(String customerId, String messageId, MessageType messageType, Long joinId){
		//消息置为已读
		appMessageRepository.setMessageRead(customerId, Lists.newArrayList(messageId));
		//增加该条消息的打开数
		switch (messageType){
			case Preferential:
				messageSendRepository.addOpenSum(joinId);
				break;
			case Notice:
				messageSendNodeRepository.addOpenSum(joinId);
				break;
			default:
				break;
		}

	}

	/**
	 * 将某个会员的所有未读消息置未已读
	 * @param customerId
	 */
	@Transactional
	public void setAllMessageRead(String customerId){
		List<String> appMessageIds = appMessageRepository.findAllAppMessage(customerId);
		appMessageRepository.setMessageRead(customerId, appMessageIds);
	}


	/**
	 * 将实体包装成VO
	 * @author xuyunpeng
	 */
	public AppMessageVO wrapperVo(AppMessage appMessage) {
		if (appMessage != null){
			AppMessageVO appMessageVO = KsBeanUtil.convert(appMessage, AppMessageVO.class);
			return appMessageVO;
		}
		return null;
	}
}

