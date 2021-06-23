package com.wanmi.sbc.customer.paidcardrule.service;

import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.dto.ERPTradeItemDTO;
import com.sbc.wanmi.erp.bean.dto.ERPTradePaymentDTO;
import com.sbc.wanmi.erp.bean.enums.ERPTradePayChannel;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.PaidCardConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.BusinessCodeGenUtils;
import com.wanmi.sbc.common.util.DateUtil;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.request.paidcard.PaidCardBuyRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleQueryRequest;
import com.wanmi.sbc.customer.api.response.paidcard.PaidCardBuyResponse;
import com.wanmi.sbc.customer.bean.dto.PaidCardERPPushDTO;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardRuleVO;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import com.wanmi.sbc.customer.paidcard.repository.PaidCardRepository;
import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import com.wanmi.sbc.customer.paidcardbuyrecord.repository.PaidCardBuyRecordRepository;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import com.wanmi.sbc.customer.paidcardcustomerrel.repository.PaidCardCustomerRelRepository;
import com.wanmi.sbc.customer.paidcardrule.model.root.PaidCardRule;
import com.wanmi.sbc.customer.paidcardrule.repository.PaidCardRuleRepository;
import com.wanmi.sbc.customer.util.PaidSmsSendUtil;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.request.PushTradeRequest;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.setting.bean.enums.AddrLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.domain.Page;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * <p>付费会员业务逻辑</p>
 * @author xuhai
 * @date 2021-01-29 14:04:01
 */
@Service("PaidCardRuleService")
public class PaidCardRuleService {
	@Autowired
	private PaidCardRuleRepository paidCardRuleRepository;

	@Autowired
	private PaidCardRepository paidCardRepository;

	@Autowired
	private PaidCardCustomerRelRepository paidCardCustomerRelRepository;

	@Autowired
	private PaidCardBuyRecordRepository paidCardBuyRecordRepository;

	@Autowired
	private BinderAwareChannelResolver resolver;

	@Autowired
	private BaseConfigQueryProvider baseConfigQueryProvider;

	@Autowired
	private GuanyierpProvider guanyierpProvider;

	
	/** 
	 * 新增付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardRule add(PaidCardRule entity) {
		paidCardRuleRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardRule modify(PaidCardRule entity) {
		paidCardRuleRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteById(String id) {
		paidCardRuleRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		ids.forEach(id -> paidCardRuleRepository.deleteById(id));
	}
	
	/** 
	 * 单个查询付费会员
	 * @author xuhai
	 */
	public PaidCardRule getById(String id){
		return paidCardRuleRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询付费会员
	 * @author xuhai
	 */
	public Page<PaidCardRule> page(PaidCardRuleQueryRequest queryReq){
		return paidCardRuleRepository.findAll(
				PaidCardRuleWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询付费会员
	 * @author xuhai
	 */
	public List<PaidCardRule> list(PaidCardRuleQueryRequest queryReq){
		return paidCardRuleRepository.findAll(
				PaidCardRuleWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author xuhai
	 */
	public PaidCardRuleVO wrapperVo(PaidCardRule paidCardRule) {
		if (paidCardRule != null){
			PaidCardRuleVO paidCardRuleVO=new PaidCardRuleVO();
			KsBeanUtil.copyPropertiesThird(paidCardRule,paidCardRuleVO);
			return paidCardRuleVO;
		}
		return null;
	}

	public PaidCardRuleVO getByIdWithPaidCardInfo(String id) {
		PaidCardRule paidCardRule = paidCardRuleRepository.findById(id).orElse(null);
		PaidCardRuleVO paidCardRuleVO = KsBeanUtil.convert(paidCardRule, PaidCardRuleVO.class);
		String paidCardId = paidCardRuleVO.getPaidCardId();
		PaidCard paidCard = this.paidCardRepository.findById(paidCardId).orElse(null);
		PaidCardVO paidCardVO = KsBeanUtil.convert(paidCard, PaidCardVO.class);
		paidCardRuleVO.setPaidCard(paidCardVO);
		return paidCardRuleVO;
	}

	@Transactional
	public BaseResponse<PaidCardBuyResponse> commit(PaidCardBuyRequest req) {
		String ruleId = req.getRuleId();
		PaidCardRule paidCardRule = this.paidCardRuleRepository.findById(ruleId).get();
		BigDecimal price = paidCardRule.getPrice();
		if(price.compareTo(BigDecimal.ZERO) == 1){
			//说明非零元支付 直接封装出参
			return BaseResponse.success(PaidCardBuyResponse.builder()
					.payFlag(PayFlagEnum.NEED_PAY)
					.price(price)
					.build());
		}
		// 处理0元支付购买付费会员逻辑
		BaseResponse<PaidCardBuyResponse> resp =
				this.dealZeroBusiness(paidCardRule,req.getCustomer());

		return resp;
	}

	@Autowired
	private PaidSmsSendUtil paidSmsSendUtil;

	/**
	 * 处理0元单的付费会员购买逻辑
	 * @param paidCardRule
	 * @param customer
	 * @return
	 */
	private BaseResponse<PaidCardBuyResponse> dealZeroBusiness(PaidCardRule paidCardRule, CustomerVO customer) {

		PaidCard paidCard = this.paidCardRepository.findById(paidCardRule.getPaidCardId()).get();

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beginTime = now;
		PaidCardRuleTypeEnum paidCardRuleType = paidCardRule.getType();
		PaidCardCustomerRel paidCardCustomerRel;
		if(paidCardRuleType.equals(PaidCardRuleTypeEnum.PAY)){
			//说明是新增
			// 1. 生成卡号
			String paidCardCode = BusinessCodeGenUtils.genPaidCardCode();
			paidCardCustomerRel = new PaidCardCustomerRel();
			paidCardCustomerRel.setId(UUIDUtil.getUUID());
			paidCardCustomerRel.setDelFlag(DeleteFlag.NO);
			paidCardCustomerRel.setCardNo(paidCardCode);
			paidCardCustomerRel.setCustomerId(customer.getCustomerId());
			paidCardCustomerRel.setPaidCardId(paidCardRule.getPaidCardId());
			paidCardCustomerRel.setPaidSource(1);
			paidCardCustomerRel.setBeginTime(now);
			LocalDateTime end = this.getEndTime(now,paidCardRule);
			paidCardCustomerRel.setEndTime(end);

		}else{
			//说明是续费
			// 查询出当前的实例
			paidCardCustomerRel =
					paidCardCustomerRelRepository.findInstanceByPaidCard(paidCardRule.getPaidCardId(),now,customer.getCustomerId());
			// 更新数据
			beginTime = paidCardCustomerRel.getEndTime();
			LocalDateTime end = this.getEndTime(paidCardCustomerRel.getEndTime(),paidCardRule);
			paidCardCustomerRel.setEndTime(end);
		}
		paidCardCustomerRel.setSendMsgFlag(Boolean.FALSE);
		paidCardCustomerRel.setSendExpireMsgFlag(Boolean.FALSE);
		paidCardCustomerRelRepository.save(paidCardCustomerRel);

		//生成购买记录
		PaidCardBuyRecord paidCardBuyRecord = new PaidCardBuyRecord();
		paidCardBuyRecord.setPayCode(BusinessCodeGenUtils.genPaidCardBuyRecordCode());
		paidCardBuyRecord.setCardNo(paidCardCustomerRel.getCardNo());
		paidCardBuyRecord.setBeginTime(beginTime);
		paidCardBuyRecord.setCreateTime(now);
		paidCardBuyRecord.setCustomerId(customer.getCustomerId());
		paidCardBuyRecord.setCustomerAccount(customer.getCustomerAccount());
		paidCardBuyRecord.setCustomerPaidcardId(paidCardCustomerRel.getId());
		paidCardBuyRecord.setCustomerName(customer.getCustomerDetail().getCustomerName());
		paidCardBuyRecord.setInvalidTime(paidCardCustomerRel.getEndTime());
		paidCardBuyRecord.setPaidCardId(paidCardCustomerRel.getPaidCardId());
		paidCardBuyRecord.setPaidCardName(paidCard.getName());
		paidCardBuyRecord.setRuleId(paidCardRule.getId());
		paidCardBuyRecord.setRuleName(paidCardRule.getName());
		paidCardBuyRecord.setPrice(paidCardRule.getPrice());
		if(paidCardRule.getType().equals(PaidCardRuleTypeEnum.PAY)){
			paidCardBuyRecord.setType(BuyTypeEnum.BUY);
		}else{
			paidCardBuyRecord.setType(BuyTypeEnum.REBUY);
		}
		paidCardBuyRecordRepository.save(paidCardBuyRecord);
		// 发送短信
		LocalDateTime endTime = paidCardCustomerRel.getEndTime();
		paidSmsSendUtil.send(PaidCardSmsTemplate.PAID_CARD_BUY_CODE, new String[]{
				customer.getCustomerDetail().getContactPhone()
				},

				new String[]{
						paidCard.getName()+paidCardRule.getName(),
						endTime.getYear()+"",
						endTime.getMonth().getValue()+"",
						endTime.getDayOfMonth()+""
				});
		BaseConfigRopResponse config = baseConfigQueryProvider.getBaseConfig().getContext();
		Map<String, Object> params4Message = new HashMap<>();
		Map<String, Object> routeParam = new HashMap<>();
		routeParam.put("type",12);
		params4Message.put("params",Arrays.asList(paidCard.getName() + paidCardRule.getName(),
				endTime.getYear()+"",
				endTime.getMonth().getValue()+"",
				endTime.getDayOfMonth()+"",
				config.getMobileWebsite()+ PaidCardConstant.PAID_CARD_CENTER_ADDRESS
				));
		params4Message.put("customerId", customer.getCustomerId());
		params4Message.put("routeParam", routeParam);
		params4Message.put("nodeType", 1);
		params4Message.put("nodeCode", PaidCardSmsTemplate.PAID_CARD_BUY_CODE.name());
		resolver.resolveDestination(MQConstant.PAID_CARD_MESSAGE_Q_NAME)
				.send(new GenericMessage<>(JSONObject.toJSONString(params4Message)));

		//将付费卡订单推送到ERP
		this.pushPaidCard(PaidCardERPPushDTO.builder()
				.account(customer.getCustomerAccount())
				.payTime(paidCardBuyRecord.getCreateTime())
				.payTypeCode(ERPTradePayChannel.other.toValue())
				.phone(customer.getCustomerDetail().getContactPhone())
				.platformCode(UUIDUtil.getUUID())
				.price(paidCardRule.getPrice().toString())
				//.shopCode("99999")
				.skuCode(paidCardRule.getErpSkuCode())
				.spuCode(paidCard.getErpSpuCode())
				.vipCode(customer.getCustomerAccount())
				.build());

		// 更新es
		Map<String, Object>params = new HashMap<>();
		params.put("customerId", paidCardBuyRecord.getCustomerId());
		Map<String, String> paidCardMap = new HashMap<>();
		paidCardMap.put("paidCardName", paidCardBuyRecord.getPaidCardName());
		paidCardMap.put("paidCardId", paidCardBuyRecord.getPaidCardId());
		params.put("esPaidCardList", paidCardMap);
		resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO)
				.send(new GenericMessage<>(JSONObject.toJSONString(params)));
		return BaseResponse.success(PaidCardBuyResponse.builder()
				.payFlag(PayFlagEnum.NO_PAY)
				.price(paidCardBuyRecord.getPrice())
				.build());
	}


	/**
	 * 生成结束时间
	 * @param now
	 * @param paidCardRule
	 * @return
	 */
	public LocalDateTime getEndTime(LocalDateTime now, PaidCardRule paidCardRule) {
		TimeUnitEnum timeUnit = paidCardRule.getTimeUnit();
		Integer timeVal = paidCardRule.getTimeVal();
		if(timeUnit.equals(TimeUnitEnum.DAY)){
			//说明是按天计算
			return now.plusDays(timeVal);
		}else if(timeUnit.equals(TimeUnitEnum.MONTH)){
			//说明是按月计算
			return now.plusMonths(timeVal);
		}else{
			//说明是按年计算
			return now.plusYears(timeVal);
		}

	}

	public void pushPaidCard(PaidCardERPPushDTO paidCardERPPushDTO) {
		ERPTradeItemDTO erpTradeItemDTO = ERPTradeItemDTO.builder()
				.skuCode(paidCardERPPushDTO.getSkuCode())
				.itemCode(paidCardERPPushDTO.getSpuCode())
				.price(paidCardERPPushDTO.getPrice())
				.qty(1)
				.refund(0)
				.build();
		List<ERPTradePaymentDTO> erpTradePaymentDTOList = new ArrayList<>();
		ERPTradePaymentDTO erpTradePaymentDTO = ERPTradePaymentDTO.builder()
				.account(paidCardERPPushDTO.getAccount())
				.paytime(Objects.nonNull(paidCardERPPushDTO.getPayTime()) ? paidCardERPPushDTO.getPayTime().toInstant(ZoneOffset.of("+8")).toEpochMilli():null)
				.payment(paidCardERPPushDTO.getPrice())
//				.payTypeCode(ERPTradePayChannel.other.toValue())
				.payTypeCode(paidCardERPPushDTO.getPayTypeCode())
				.build();
		erpTradePaymentDTOList.add(erpTradePaymentDTO);
		PushTradeRequest pushTradeRequest = PushTradeRequest.builder()
				.shopCode(paidCardERPPushDTO.getShopCode())//店铺代码
				.vipCode(paidCardERPPushDTO.getAccount())//会员代码
				.dealDatetime(DateUtil.format(LocalDateTime.now(),DateUtil.FMT_TIME_1))//下单时间
				.platformCode(UUIDUtil.getUUID())//订单号
				.details(Arrays.asList(erpTradeItemDTO))
				.payments(erpTradePaymentDTOList)
//				.receiveName(customer.getCustomerDetail().getCustomerName())
				.receiverMobile(paidCardERPPushDTO.getPhone())
//				.receiverProvince(addrMap.get(AddrLevel.PROVINCE))
//				.receiverCity(addrMap.get(AddrLevel.CITY))
//				.receiverDistrict(addrMap.get(AddrLevel.DISTRICT))
//				.receiverAddress(consignee.getDetailAddress())
				.build();
//		guanyierpProvider.autoPushTrade(pushTradeRequest);
		new Thread(() -> {
			guanyierpProvider.autoPushTrade(pushTradeRequest);
		}).start();
	}
}
