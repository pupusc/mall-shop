package com.wanmi.sbc.message.messagesend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendAddRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendModifyRequest;
import com.wanmi.sbc.message.api.request.messagesend.MessageSendQueryRequest;
import com.wanmi.sbc.message.bean.constant.MessageErrorCode;
import com.wanmi.sbc.message.bean.enums.SendType;
import com.wanmi.sbc.message.bean.vo.MessageSendVO;
import com.wanmi.sbc.message.messagesend.model.root.MessageSend;
import com.wanmi.sbc.message.messagesend.model.root.MessageSendCustomerScope;
import com.wanmi.sbc.message.messagesend.repository.MessageSendCustomerScopeRepository;
import com.wanmi.sbc.message.messagesend.repository.MessageSendRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>站内信任务表业务逻辑</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:12:11
 */
@Service("MessageSendService")
public class MessageSendService {
	@Autowired
	private MessageSendRepository messageSendRepository;

	@Autowired
	private MessageSendCustomerScopeRepository messageSendCustomerScopeRepository;

	/**
	 * 新增站内信任务表
	 * @author xuyunpeng
	 */
	@GlobalTransactional
	@Transactional
	public MessageSend add(MessageSendAddRequest messageSendAddRequest) {
		//校验定时发送时间
		if(SendType.DELAY.equals(messageSendAddRequest.getSendTimeType())
				&& messageSendAddRequest.getSendTime().isBefore(LocalDateTime.now())){
			throw new SbcRuntimeException(MessageErrorCode.ERROR_DATR);
		}
		if(SendType.NOW.equals(messageSendAddRequest.getSendTimeType())){
			messageSendAddRequest.setSendTime(LocalDateTime.now());
		}
		MessageSend entity = KsBeanUtil.convert(messageSendAddRequest, MessageSend.class);
		entity.setDelFlag(DeleteFlag.NO);
		entity.setCreateTime(LocalDateTime.now());
		entity.setOpenSum(0);
		entity.setSendSum(0);
		entity = messageSendRepository.save(entity);
		//保存相关接收人
		addScope(messageSendAddRequest.getJoinIds(), entity.getMessageId());
		return entity;
	}

	/**
	 * 修改站内信任务表
	 * @author xuyunpeng
	 */
	@GlobalTransactional
	@Transactional
	public MessageSend modify(MessageSendModifyRequest messageSendModifyRequest) {

		//校验定时发送时间
		if(SendType.DELAY.equals(messageSendModifyRequest.getSendTimeType())
				&& messageSendModifyRequest.getSendTime().isBefore(LocalDateTime.now())){
			throw new SbcRuntimeException(MessageErrorCode.ERROR_DATR);
		}
		if(SendType.NOW.equals(messageSendModifyRequest.getSendTimeType())){
			messageSendModifyRequest.setSendTime(LocalDateTime.now());
		}
		MessageSend messageSend;
		if(messageSendModifyRequest.getPushId() == null){
			messageSend = this.getOne(messageSendModifyRequest.getMessageId());
		}else{
			messageSend = this.findByPashId(messageSendModifyRequest.getPushId());
		}

		if(LocalDateTime.now().isAfter(messageSend.getSendTime())){
			throw new SbcRuntimeException(MessageErrorCode.TASK_IS_END);
		}

		messageSend.setContent(messageSendModifyRequest.getContent());
		messageSend.setName(messageSendModifyRequest.getName());
		messageSend.setTitle(messageSendModifyRequest.getTitle());
		messageSend.setSendTime(messageSendModifyRequest.getSendTime());
		messageSend.setSendTimeType(messageSendModifyRequest.getSendTimeType());
		messageSend.setSendType(messageSendModifyRequest.getSendType());
		messageSend.setImgUrl(messageSendModifyRequest.getImgUrl());
		messageSend.setUpdatePerson(messageSendModifyRequest.getUpdatePerson());
		messageSend.setUpdateTime(LocalDateTime.now());
		messageSend.setRouteParams(messageSendModifyRequest.getRouteParams());
		messageSend.setPcRouteParams(messageSendModifyRequest.getPcRouteParams());
		//保存任务
		MessageSend entity = messageSendRepository.save(messageSend);

		//删除原有接收人数据
		messageSendCustomerScopeRepository.deleteByMessageId(entity.getMessageId());

		//保存新的接收人
		addScope(messageSendModifyRequest.getJoinIds(), entity.getMessageId());

		return entity;
	}

	/**
	 * 保存接收人
	 * @param ids
	 * @param messageId
	 */
	private void addScope(List<String> ids, Long messageId){
		List<MessageSendCustomerScope> scopeList;
		if(CollectionUtils.isNotEmpty(ids)){
			scopeList = ids.stream().map(joinId -> {
				MessageSendCustomerScope scope = new MessageSendCustomerScope();
				scope.setJoinId(joinId);
				scope.setMessageId(messageId);
				return scope;
			}).collect(Collectors.toList());
			messageSendCustomerScopeRepository.saveAll(scopeList);
		}
	}

	/**
	 * 单个删除站内信任务表
	 * @author xuyunpeng
	 */
	@GlobalTransactional
	@Transactional
	public void deleteById(Long messageId) {
		messageSendRepository.deleteById(messageId);
	}

	/**
	 * 单个查询站内信任务表
	 * @author xuyunpeng
	 */
	public MessageSend getOne(Long id){
		return messageSendRepository.findByMessageIdAndDelFlag(id, DeleteFlag.NO)
		.orElse(null);
	}

	/**
	 * 分页查询站内信任务表
	 * @author xuyunpeng
	 */
	public Page<MessageSend> page(MessageSendQueryRequest queryReq){
		queryReq.setSortColumn("createTime");
		queryReq.setSortRole("desc");
		queryReq.setDelFlag(DeleteFlag.NO);
		return messageSendRepository.findAll(
				MessageSendWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询站内信任务表
	 * @author xuyunpeng
	 */
	public List<MessageSend> list(MessageSendQueryRequest queryReq){
		return messageSendRepository.findAll(MessageSendWhereCriteriaBuilder.build(queryReq));
	}

	public MessageSend findByPashId(String pushId){
		return messageSendRepository.findByPushId(pushId);
	}

	@GlobalTransactional
	@Transactional
	public void deleteByPushId(String pushId){
		messageSendRepository.deleteByPushId(pushId);
	}

	/**
	 * 将实体包装成VO
	 * @author xuyunpeng
	 */
	public MessageSendVO wrapperVo(MessageSend messageSend) {
		if (messageSend != null){
			MessageSendVO messageSendVO = KsBeanUtil.convert(messageSend, MessageSendVO.class);
			return messageSendVO;
		}
		return null;
	}
}

