package com.wanmi.sbc.message.messagesend.service;

import com.wanmi.sbc.message.api.request.appmessage.MessageSendCustomerScopeQueryRequest;
import com.wanmi.sbc.message.messagesend.model.root.MessageSendCustomerScope;
import com.wanmi.sbc.message.messagesend.repository.MessageSendCustomerScopeRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.message.bean.vo.MessageSendCustomerScopeVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.util.List;

/**
 * <p>站内信消息会员关联表业务逻辑</p>
 * @author xuyunpeng
 * @date 2020-01-06 11:16:04
 */
@Service("MessageSendCustomerScopeService")
public class MessageSendCustomerScopeService {
	@Autowired
	private MessageSendCustomerScopeRepository messageSendCustomerScopeRepository;

	/**
	 * 根据messageId删除关联数据
	 * @author xuyunpeng
	 */
	@Transactional
	public void deleteByMessageId(Long messageId) {
		messageSendCustomerScopeRepository.deleteByMessageId(messageId);
	}

	/**
	 * 单个查询站内信消息会员关联表
	 * @author xuyunpeng
	 */
	public List<MessageSendCustomerScope> getByMessageId(Long messageId){
		return messageSendCustomerScopeRepository.findByMessageId(messageId);
	}

	/**
	 * 列表查询站内信消息会员关联表
	 * @author xuyunpeng
	 */
	public List<MessageSendCustomerScope> list(MessageSendCustomerScopeQueryRequest queryReq){
		return messageSendCustomerScopeRepository.findAll(MessageSendCustomerScopeWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author xuyunpeng
	 */
	public MessageSendCustomerScopeVO wrapperVo(MessageSendCustomerScope messageSendCustomerScope) {
		if (messageSendCustomerScope != null){
			MessageSendCustomerScopeVO messageSendCustomerScopeVO = KsBeanUtil.convert(messageSendCustomerScope, MessageSendCustomerScopeVO.class);
			return messageSendCustomerScopeVO;
		}
		return null;
	}
}

