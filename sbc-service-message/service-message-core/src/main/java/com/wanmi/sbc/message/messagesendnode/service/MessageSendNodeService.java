package com.wanmi.sbc.message.messagesendnode.service;

import com.wanmi.sbc.message.bean.enums.NodeType;
import com.wanmi.sbc.message.bean.enums.SwitchFlag;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.message.messagesendnode.repository.MessageSendNodeRepository;
import com.wanmi.sbc.message.messagesendnode.model.root.MessageSendNode;
import com.wanmi.sbc.message.api.request.messagesendnode.MessageSendNodeQueryRequest;
import com.wanmi.sbc.message.bean.vo.MessageSendNodeVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.enums.DeleteFlag;

import java.time.LocalDateTime;

/**
 * <p>站内信通知节点表业务逻辑</p>
 * @author xuyunpeng
 * @date 2020-01-09 11:45:53
 */
@Service("MessageSendNodeService")
public class MessageSendNodeService {
	@Autowired
	private MessageSendNodeRepository messageSendNodeRepository;

	/**
	 * 新增站内信通知节点表
	 * @author xuyunpeng
	 */
	@Transactional
	public MessageSendNode add(MessageSendNode entity) {
		messageSendNodeRepository.save(entity);
		return entity;
	}

	/**
	 * 修改站内信通知节点表
	 * @author xuyunpeng
	 */
	@Transactional
	public MessageSendNode modify(MessageSendNode entity) {
		MessageSendNode node = this.getOne(entity.getId());
		node.setNodeName(entity.getNodeName());
		node.setNodeTitle(entity.getNodeTitle());
		node.setNodeContent(entity.getNodeContent());
		node.setUpdatePerson(entity.getUpdatePerson());
		node.setUpdateTime(LocalDateTime.now());
		entity = messageSendNodeRepository.save(node);
		return entity;
	}

	/**
	 * 单个查询站内信通知节点表
	 * @author xuyunpeng
	 */
	public MessageSendNode getOne(Long id){
		return messageSendNodeRepository.findByIdAndDelFlag(id, DeleteFlag.NO)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "站内信通知节点不存在"));
	}

	/**
	 * 分页查询站内信通知节点表
	 * @author xuyunpeng
	 */
	public Page<MessageSendNode> page(MessageSendNodeQueryRequest queryReq){
		return messageSendNodeRepository.findAll(
				MessageSendNodeWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 开/关节点
	 * @param id
	 */
	@Transactional
	public void updateStatus(Long id){
		MessageSendNode node = this.getOne(id);
		messageSendNodeRepository.updateStatus(
				SwitchFlag.CLOSE.equals(node.getStatus())
				? SwitchFlag.OPEN
				: SwitchFlag.CLOSE, node.getId());
	}

	public MessageSendNode findByNodeTypeAndCode(NodeType nodeType, String nodeCode){
		return messageSendNodeRepository.findByNodeTypeAndNodeCode(nodeType, nodeCode);
	}

	/**
	 * 将实体包装成VO
	 * @author xuyunpeng
	 */
	public MessageSendNodeVO wrapperVo(MessageSendNode messageSendNode) {
		if (messageSendNode != null){
			MessageSendNodeVO messageSendNodeVO = KsBeanUtil.convert(messageSendNode, MessageSendNodeVO.class);
			return messageSendNodeVO;
		}
		return null;
	}
}

