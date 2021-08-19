package com.wanmi.sbc.message.pushsend.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.message.api.request.pushsend.PushSendAddRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendModifyRequest;
import com.wanmi.sbc.message.api.request.pushsend.PushSendQueryRequest;
import com.wanmi.sbc.message.bean.constant.PushErrorCode;
import com.wanmi.sbc.message.bean.enums.MethodType;
import com.wanmi.sbc.message.bean.enums.PushPlatform;
import com.wanmi.sbc.message.bean.enums.PushStatus;
import com.wanmi.sbc.message.bean.vo.PushSendVO;
import com.wanmi.sbc.message.pushUtil.PushEntry;
import com.wanmi.sbc.message.pushUtil.PushService;
import com.wanmi.sbc.message.pushUtil.root.PushResultEntry;
import com.wanmi.sbc.message.pushUtil.root.QueryResultEntry;
import com.wanmi.sbc.message.pushdetail.repository.PushDetailRepository;
import com.wanmi.sbc.message.pushsend.model.root.PushSend;
import com.wanmi.sbc.message.pushsend.repository.PushSendRepository;
import com.wanmi.sbc.message.umengtoken.model.root.UmengToken;
import com.wanmi.sbc.message.umengtoken.repository.UmengTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>会员推送信息业务逻辑</p>
 * @author Bob
 * @date 2020-01-08 17:15:32
 */
@Slf4j
@Service("PushSendService")
public class PushSendService {
	@Autowired
	private PushSendRepository pushSendRepository;
	@Autowired
	private UmengTokenRepository umengTokenRepository;
	@Autowired
	private PushService pushService;
	@Autowired
	private PushDetailRepository pushDetailRepository;

	/**
	 * 新增会员推送信息
	 * @author Bob
	 */
	@GlobalTransactional
	@Transactional
	public PushSend add(PushSendAddRequest pushSendAddRequest) {
		PushSend entity = KsBeanUtil.convert(pushSendAddRequest, PushSend.class);
		entity = pushSendRepository.save(entity);

		List<PushResultEntry> resultEntries = this.pushCommon(entity, pushSendAddRequest.getCustomers());
		for (PushResultEntry resultEntry : resultEntries){
			if (PushPlatform.IOS.equals(resultEntry.getPlatform())){
				if ("SUCCESS".equals(resultEntry.getRet())){
					entity.setIosTaskId(resultEntry.getTaskId());
				} else {
					log.error("PushSendService.add::友盟iOS发送接口失败");
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"iOS消息发送"});
				}
			} else if (PushPlatform.ANDROID.equals(resultEntry.getPlatform())) {
				if ("SUCCESS".equals(resultEntry.getRet())){
					entity.setAndroidTaskId(resultEntry.getTaskId());
				} else {
					log.error("PushSendService.add::友盟android发送接口失败");
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"android消息发送"});
				}
			}
		}
		return pushSendRepository.save(entity);
	}

	private List<PushResultEntry> pushCommon(PushSend entity, List<String> customerIdList){

		this.cancel(entity);

		Integer recipient = entity.getMsgRecipient();
		List<String> iosTokenList = new ArrayList<>();
		List<String> androidTokenList = new ArrayList<>();
		Map<PushPlatform, List<UmengToken>> umengTokenList = new HashMap<>();
		if (recipient == 0){
			umengTokenList =
					umengTokenRepository.findAll().stream().collect(Collectors.groupingBy(UmengToken::getPlatform));
		} else {
			umengTokenList =
					umengTokenRepository.queryByCustomerIdIn(customerIdList).map(umengTokens -> umengTokens.stream()
							.collect(Collectors.groupingBy(UmengToken::getPlatform))).orElseGet(HashMap::new);
		}

		if (umengTokenList.get(PushPlatform.IOS) != null ){
			iosTokenList =
					umengTokenList.get(PushPlatform.IOS).stream().map(UmengToken::getDevlceToken).collect(Collectors.toList());
		} else if (Objects.nonNull(entity.getIosTaskId())) {
			pushDetailRepository.deleteById(entity.getIosTaskId());
			entity.setIosTaskId(null);
		}

		if (umengTokenList.get(PushPlatform.ANDROID) != null){
			androidTokenList =
					umengTokenList.get(PushPlatform.ANDROID).stream().map(UmengToken::getDevlceToken).collect(Collectors.toList());
		} else if (Objects.nonNull(entity.getAndroidTaskId())) {
			pushDetailRepository.deleteById(entity.getAndroidTaskId());
			entity.setAndroidTaskId(null);
		}

		JSONObject params = new JSONObject();
		if (StringUtils.isNotBlank(entity.getMsgRouter())){
			String router = entity.getMsgRouter().replaceAll("'", "\"");
			JSONObject jsonObject = JSONObject.parseObject(router);
			String link = jsonObject.getString("linkKey");
			JSONObject info = jsonObject.getJSONObject("info");
			switch (link){
				case "goodsList":
					String skuId = info.getString("skuId");
					params.put("type", 0);
					params.put("skuId", skuId);
					break;
				case "categoryList":
					JSONArray selectedKeys = info.getJSONArray("selectedKeys");
					String pathNames = info.getString("pathName");
					String[] names =  pathNames.split(",");
					String cateId = selectedKeys.getString(selectedKeys.size()-1);
					String cateName = names[names.length-1];
					params.put("type", 3);
					params.put("cateId", cateId);
					params.put("cateName", cateName);
					break;
				case "storeList":
					String storeId = info.getString("storeId");
					params.put("type", 4);
					params.put("storeId", storeId);
					break;
				case "promotionList":
					params.put("type", 5);
					String cateKey = info.getString("cateKey");
					if ("groupon".equals(cateKey)){
						String goodsInfoId = info.getString("goodsInfoId");
						params.put("node", 0);
						params.put("skuId", goodsInfoId);
					} else if ("full".equals(cateKey)){
						String marketingId = info.getString("marketingId");
						params.put("node", 2);
						params.put("mid", marketingId);
					} else if ("flash".equals(cateKey)){
						params.put("node", 1);
						String goodsInfoId = info.getString("goodsInfoId");
						params.put("skuId", goodsInfoId);
					} else if ("onePrice".equals(cateKey)) {
						String marketingId = info.getString("marketingId");
						params.put("node", 2);
						params.put("mid", marketingId);
					} else if ("halfPrice".equals(cateKey)) {
						String marketingId = info.getString("marketingId");
						params.put("node", 2);
						params.put("mid", marketingId);
					} else if ("comBuy".equals(cateKey)) {
						String goodsInfoId = info.getString("goodsInfoId");
						params.put("node", 3);
						params.put("skuId", goodsInfoId);
					} else if ("preOrder".equals(cateKey)) {
						String goodsInfoId = info.getString("goodsInfoId");
						params.put("node", 1);
						params.put("skuId", goodsInfoId);
					} else if ("preSell".equals(cateKey)) {
						String goodsInfoId = info.getString("goodsInfoId");
						params.put("node", 1);
						params.put("skuId", goodsInfoId);
					}
					break;
				case "userpageList":
					params.put("type", 12);
					String appPath = info.getString("appPath");
					params.put("router", appPath);
					break;
				case "pageList":
					params.put("type",6);
					String pageType = info.getString("pageType");
					String pageCode = info.getString("pageCode");
					params.put("pageType", pageType);
					params.put("pageCode", pageCode);
					break;
			}
		}

		entity.setExpectedSendCount(iosTokenList.size() + androidTokenList.size());
		PushEntry pushEntry = new PushEntry();
		pushEntry.setImage(entity.getMsgImg());
//		pushEntry.setOutBizNo(entity.getId().toString());
		pushEntry.setRouter(params.toJSONString());
		pushEntry.setText(entity.getMsgContext());
		pushEntry.setTicker("通知");
		pushEntry.setTitle(entity.getMsgTitle());
		if (Objects.nonNull(entity.getPushTime())){
			pushEntry.setSendTime(entity.getPushTime());
		}
		pushEntry.setIosTokenList(iosTokenList);
		pushEntry.setAndroidTokenList(androidTokenList);
		return pushService.push(pushEntry);
	}

	/**
	 * @Description: 友盟任务取消
	 * @param entity
	 * @Date: 2020/1/12 15:08
	 */
	private void cancel(PushSend entity){
		if (StringUtils.isNotBlank(entity.getIosTaskId())){
			QueryResultEntry resultEntry = pushService.queryOrCancel(entity.getIosTaskId(), PushPlatform.IOS, MethodType.QUERY);
			if ("FAIL".equals(resultEntry.getRet())){
				log.error("PushSendService.cancel::友盟iOS查询接口失败");
				throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"iOS查询"});
			}

			if (resultEntry.getStatus() == PushStatus.CANCEL.toValue())
				return;
			if (resultEntry.getStatus() == PushStatus.QUEUE.toValue() || resultEntry.getStatus() == PushStatus.SEND.toValue()){
				QueryResultEntry queryResultEntry = pushService.queryOrCancel(entity.getIosTaskId(), PushPlatform.IOS
						, MethodType.CANCEL);
				if ("FAIL".equals(queryResultEntry.getRet())){
					log.error("PushSendService.cancel::友盟iOS撤销接口失败");
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"iOS撤销"});
				}
			} else {
				throw new SbcRuntimeException(PushErrorCode.UMENG_NOT_CANCEL);
			}
		}

		if (StringUtils.isNotBlank(entity.getAndroidTaskId())){
			QueryResultEntry resultEntry = pushService.queryOrCancel(entity.getAndroidTaskId(), PushPlatform.ANDROID,
					MethodType.QUERY);
			if ("FAIL".equals(resultEntry.getRet())){
				log.error("PushSendService.cancel::友盟android查询接口失败");
				throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"android查询"});
			}

			if (resultEntry.getStatus() == PushStatus.CANCEL.toValue())
				return;
			if (resultEntry.getStatus() == PushStatus.QUEUE.toValue() || resultEntry.getStatus() == PushStatus.SEND.toValue()){
				QueryResultEntry queryResultEntry = pushService.queryOrCancel(entity.getAndroidTaskId(),
						PushPlatform.ANDROID, MethodType.CANCEL);
				if ("FAIL".equals(queryResultEntry.getRet())){
					log.error("PushSendService.cancel::友盟android撤销接口失败");
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"android撤销"});
				}
			} else {
				throw new SbcRuntimeException(PushErrorCode.UMENG_NOT_CANCEL);
			}
		}
	}

	/**
	 * 修改会员推送信息
	 * @author Bob
	 */
	@GlobalTransactional
	@Transactional
	public PushSend modify(PushSendModifyRequest pushSendModifyRequest) {
		PushSend entity = KsBeanUtil.convert(pushSendModifyRequest, PushSend.class);
		PushSend pushSend = pushSendRepository.getOne(entity.getId());

		if (pushSend.getPushTime() == null){
			throw new SbcRuntimeException(PushErrorCode.UMENG_NOT_CANCEL);
		}

		if (pushSend.getPushTime().isBefore(LocalDateTime.now())){
			throw new SbcRuntimeException(PushErrorCode.UMENG_NOT_CANCEL);
		}

		pushSend.setMsgName(entity.getMsgName());
		pushSend.setMsgTitle(entity.getMsgTitle());
		pushSend.setMsgContext(entity.getMsgContext());
		pushSend.setMsgImg(entity.getMsgImg());
		pushSend.setMsgRecipient(entity.getMsgRecipient());
		pushSend.setMsgRecipientDetail(entity.getMsgRecipientDetail());
		pushSend.setPushTime(entity.getPushTime());
		pushSend.setMsgRouter(entity.getMsgRouter());

		pushSend = pushSendRepository.save(pushSend);

		List<PushResultEntry> resultEntries = this.pushCommon(pushSend, pushSendModifyRequest.getCustomers());

		for (PushResultEntry resultEntry : resultEntries){
			if (PushPlatform.IOS.equals(resultEntry.getPlatform())){
				if ("SUCCESS".equals(resultEntry.getRet())){
					pushSend.setIosTaskId(resultEntry.getTaskId());
				} else {
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"iOS消息发送"});
				}
			} else if (PushPlatform.ANDROID.equals(resultEntry.getPlatform())) {
				if ("SUCCESS".equals(resultEntry.getRet())){
					pushSend.setAndroidTaskId(resultEntry.getTaskId());
				} else {
					throw new SbcRuntimeException(PushErrorCode.UMENG_RETURN_FAIL, new Object[]{"android消息发送"});
				}
			}
		}
		return pushSendRepository.save(pushSend);
	}

	/**
	 * 单个删除会员推送信息
	 * @author Bob
	 */
	@GlobalTransactional
	@Transactional
	public void deleteById(PushSend entity) {
		Optional<PushSend> pushSend = pushSendRepository.findById(entity.getId());
		PushSend send = pushSend.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "推送消息不存在"));
		if (send.getPushTime() != null){
			if (send.getPushTime().isAfter(LocalDateTime.now())){
				this.cancel(send);
			} else {
				throw new SbcRuntimeException(PushErrorCode.UMENG_NOT_CANCEL);
			}
		}

		if (send.getCreateTime().isAfter(LocalDateTime.now())){
			throw new SbcRuntimeException(PushErrorCode.UMENG_NOT_CANCEL);
		}
		pushSendRepository.deleteById(entity.getId());
	}

	/**
	 * 批量删除会员推送信息
	 * @author Bob
	 */
	@Transactional
	public void deleteByIdList(List<PushSend> infos) {
		pushSendRepository.saveAll(infos);
	}

	/**
	 * 单个查询会员推送信息
	 * @author Bob
	 */
	public PushSend getOne(Long id){
		return pushSendRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "会员推送信息不存在"));
	}

	/**
	 * 分页查询会员推送信息
	 * @author Bob
	 */
	public Page<PushSend> page(PushSendQueryRequest queryReq){
		return pushSendRepository.findAll(
				PushSendWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询会员推送信息
	 * @author Bob
	 */
	public List<PushSend> list(PushSendQueryRequest queryReq){
		return pushSendRepository.findAll(PushSendWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author Bob
	 */
	public PushSendVO wrapperVo(PushSend pushSend) {
		if (pushSend != null){
			PushSendVO pushSendVO = KsBeanUtil.convert(pushSend, PushSendVO.class);
			return pushSendVO;
		}
		return null;
	}
}

