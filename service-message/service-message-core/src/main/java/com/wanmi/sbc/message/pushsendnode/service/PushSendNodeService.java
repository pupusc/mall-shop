package com.wanmi.sbc.message.pushsendnode.service;

import com.wanmi.sbc.common.base.BaseQueryRequest;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeQueryRequest;
import com.wanmi.sbc.message.api.request.pushsendnode.PushSendNodeRequest;
import com.wanmi.sbc.message.bean.constant.PushErrorCode;
import com.wanmi.sbc.message.bean.enums.MethodType;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import com.wanmi.sbc.message.bean.vo.PushSendNodeVO;
import com.wanmi.sbc.message.pushUtil.PushEntry;
import com.wanmi.sbc.message.pushUtil.PushService;
import com.wanmi.sbc.message.pushUtil.root.PushResultEntry;
import com.wanmi.sbc.message.pushUtil.root.QueryResultEntry;
import com.wanmi.sbc.message.pushdetail.model.root.PushDetail;
import com.wanmi.sbc.message.pushdetail.repository.PushDetailRepository;
import com.wanmi.sbc.message.pushsendnode.model.root.PushSendNode;
import com.wanmi.sbc.message.pushsendnode.repository.PushSendNodeRepository;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import com.wanmi.sbc.message.umengtoken.repository.UmengTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * <p>会员推送通知节点业务逻辑</p>
 * @author Bob
 * @date 2020-01-13 10:47:41
 */
@Service("PushSendNodeService")
@Slf4j
public class PushSendNodeService {
	@Autowired
	private PushSendNodeRepository pushSendNodeRepository;

	@Autowired
	private PushService pushService;

	@Autowired
	private UmengTokenRepository umengTokenRepository;

	@Autowired
	private PushDetailRepository pushDetailRepository;

	/**
	 * 修改会员推送通知节点
	 * @author Bob
	 */
	@Transactional
	public PushSendNode modify(PushSendNode entity) {
		pushSendNodeRepository.modify(entity.getNodeTitle(), entity.getNodeContext(), entity.getId());
		return entity;
	}
	/**
	 * 单个查询会员推送通知节点
	 * @author Bob
	 */
	public PushSendNode getOne(Long id){
		return pushSendNodeRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "会员推送通知节点不存在"));
	}

	/**
	 * 分页查询会员推送通知节点
	 * @author Bob
	 */
	public Page<PushSendNode> page(PushSendNodeQueryRequest queryReq){
		return pushSendNodeRepository.findAll(
				PushSendNodeWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询会员推送通知节点
	 * @author Bob
	 */
	public List<PushSendNode> list(PushSendNodeQueryRequest queryReq){
		return pushSendNodeRepository.findAll(PushSendNodeWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 通知节点是否启用
	 * @author Bob
	 */
	@Transactional
	public void enabled(PushSendNode pushSendNode){
		pushSendNodeRepository.updateStatus(pushSendNode.getStatus(), pushSendNode.getId());
	}

	/**
	 * 将实体包装成VO
	 * @author Bob
	 */
	public PushSendNodeVO wrapperVo(PushSendNode pushSendNode) {
		if (pushSendNode != null){
			PushSendNodeVO pushSendNodeVO = KsBeanUtil.convert(pushSendNode, PushSendNodeVO.class);
			return pushSendNodeVO;
		}
		return null;
	}

	/**
	 * @Description: 通知节点推送接口
	 * @param request
	 * @Date: 2020/1/14 11:42
	 */
	@Transactional
	public void pushNode(PushSendNodeRequest request){
		Optional<UmengToken> token = umengTokenRepository.queryByCustomerId(request.getCustomerId());
		UmengToken umengToken = token.orElseGet(UmengToken::new);

		PushEntry pushEntry = new PushEntry();
		pushEntry.setOutBizNo(UUIDUtil.getUUID());
		pushEntry.setRouter(request.getRouter());
		pushEntry.setText(request.getContent());
		pushEntry.setTicker("通知");
		pushEntry.setTitle(request.getTitle());
		if (PushPlatform.IOS.equals(umengToken.getPlatform())){
			pushEntry.setIosTokenList(Collections.singletonList(umengToken.getDevlceToken()));
		} else if (PushPlatform.ANDROID.equals(umengToken.getPlatform())){
			pushEntry.setAndroidTokenList(Collections.singletonList(umengToken.getDevlceToken()));
		}
		PushDetail detail = new PushDetail();
		detail.setNodeId(request.getNodeId());
		detail.setSendStatus(PushStatus.SEND);
		List<PushResultEntry> resultEntries = pushService.push(pushEntry);
		for (PushResultEntry resultEntry : resultEntries){
			if (PushPlatform.IOS.equals(resultEntry.getPlatform())){
				if ("SUCCESS".equals(resultEntry.getRet())){
					detail.setTaskId(resultEntry.getTaskId());
					detail.setPlatform(PushPlatform.IOS);
				} else {
					log.error("PushSendNodeService.pushNode::iOS消息发送接口失败");
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"iOS消息发送"});
				}
			} else if (PushPlatform.ANDROID.equals(resultEntry.getPlatform())) {
				if ("SUCCESS".equals(resultEntry.getRet())){
					detail.setTaskId(resultEntry.getTaskId());
					detail.setPlatform(PushPlatform.ANDROID);
				} else {
					log.error("PushSendNodeService.pushNode::android消息发送接口失败");
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"android消息发送"});
				}
			}
		}
		if (CollectionUtils.isNotEmpty(resultEntries)){
			pushDetailRepository.save(detail);
		}
		pushSendNodeRepository.updateForCount(request.getNodeId());
	}

	/**
	 * 通知节点打开数等数据统计
	 */
	@Transactional
	public void taskForUpdate(){
		Integer pageNum = NumberUtils.INTEGER_ZERO;
		int pageSize = 50;

		// <节点ID，发送收到数>
		Map<Long, Integer> sendCountMap = new HashMap<>();
		// <节点ID，打开数>
		Map<Long, Integer> openCountMap = new HashMap<>();
		List<PushStatus> pushStatuses = Arrays.asList(PushStatus.SEND_FAIL, PushStatus.CANCEL, PushStatus.OVERDUE);
		while (true) {
			/**
			 * 1. 状态不等于3、4、5
			 * 2. 7天内
			 * 3. nodeID不为空
			 * 4. 打开数为空
			 */
			BaseQueryRequest queryRequest = new BaseQueryRequest();
			queryRequest.putSort("createTime", "asc");
			queryRequest.putSort("nodeId", "asc");
			queryRequest.putSort("taskId", "asc");

			List<PushDetail> pushDetails = pushDetailRepository.getPushDetails(pushStatuses,
					LocalDateTime.now().minusDays(7),
					PageRequest.of(pageNum, pageSize, queryRequest.getSort()));

			pushDetails.forEach(pushDetail -> {
				QueryResultEntry resultEntry = pushService.queryOrCancel(pushDetail.getTaskId(), pushDetail.getPlatform(),
						MethodType.QUERY);
				if ("FAIL".equals(resultEntry.getRet())) {
					log.error("PushSendNodeService.taskForUpdate::{}查询接口失败", pushDetail.getPlatform());
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL,
							new Object[]{pushDetail.getPlatform() + "查询"});
				}
				if (!PushStatus.SEND_END.equals(pushDetail.getSendStatus())) {
					if (sendCountMap.containsKey(pushDetail.getNodeId())) {
						sendCountMap.put(pushDetail.getNodeId(),
								sendCountMap.get(pushDetail.getNodeId()) + resultEntry.getSentCount());
					} else {
						sendCountMap.put(pushDetail.getNodeId(), 1);
					}
				}

				if (openCountMap.containsKey(pushDetail.getNodeId())) {
					openCountMap.put(pushDetail.getNodeId(),
							openCountMap.get(pushDetail.getNodeId()) + resultEntry.getOpenCount());
				} else {
					openCountMap.put(pushDetail.getNodeId(), 1);
				}
				pushDetail.setSendSum(resultEntry.getSentCount());
				pushDetail.setOpenSum(resultEntry.getOpenCount());
				pushDetail.setUpdateTime(LocalDateTime.now());
				pushDetail.setSendStatus(PushStatus.fromValue(resultEntry.getStatus()));
			});

			pushDetailRepository.saveAll(pushDetails);

			if (pushDetails.size() < pageSize){
				break;
			}

			pageNum++;
		}

		List<PushSendNode> pushSendNodes = pushSendNodeRepository.findAllById(openCountMap.keySet());
		pushSendNodes.forEach(pushSendNode -> {
			pushSendNode.setActuallySendCount(pushSendNode.getActuallySendCount() + sendCountMap.get(pushSendNode.getId()));
			pushSendNode.setOpenCount(pushSendNode.getOpenCount() + openCountMap.get(pushSendNode.getId()));
		});
		pushSendNodeRepository.saveAll(pushSendNodes);
	}

	public PushSendNode findByNodeTypeAndCode(Integer nodeType, String nodeCode){
 		 return pushSendNodeRepository.findByTypeAndCode(nodeType, nodeCode);
	}
}

