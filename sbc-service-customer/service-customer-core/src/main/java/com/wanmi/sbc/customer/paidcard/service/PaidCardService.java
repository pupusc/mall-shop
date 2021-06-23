package com.wanmi.sbc.customer.paidcard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sbc.wanmi.erp.bean.enums.ERPTradePayChannel;
import com.wanmi.ares.interfaces.GoodsService;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.PaidCardConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.BusinessCodeGenUtils;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.request.levelrights.CustomerLevelRightsQueryRequest;
import com.wanmi.sbc.customer.api.request.paidcard.*;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelAddRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelModifyRequest;
import com.wanmi.sbc.customer.api.request.paidcardrightsrel.PaidCardRightsRelQueryRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleAddRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleModifyRequest;
import com.wanmi.sbc.customer.api.request.paidcardrule.PaidCardRuleQueryRequest;
import com.wanmi.sbc.customer.bean.dto.PaidCardERPPushDTO;
import com.wanmi.sbc.customer.bean.dto.PaidCardRedisDTO;
import com.wanmi.sbc.customer.bean.enums.*;
import com.wanmi.sbc.customer.bean.vo.*;
import com.wanmi.sbc.customer.levelrights.model.root.CustomerLevelRights;
import com.wanmi.sbc.customer.levelrights.service.CustomerLevelRightsService;
import com.wanmi.sbc.customer.model.root.Customer;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import com.wanmi.sbc.customer.paidcard.repository.PaidCardRepository;
import com.wanmi.sbc.customer.paidcardbuyrecord.model.root.PaidCardBuyRecord;
import com.wanmi.sbc.customer.paidcardbuyrecord.repository.PaidCardBuyRecordRepository;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import com.wanmi.sbc.customer.paidcardcustomerrel.repository.PaidCardCustomerRelRepository;
import com.wanmi.sbc.customer.paidcardcustomerrel.service.PaidCardCustomerRelService;
import com.wanmi.sbc.customer.paidcardrightsrel.model.root.PaidCardRightsRel;
import com.wanmi.sbc.customer.paidcardrightsrel.repository.PaidCardRightsRelRepository;
import com.wanmi.sbc.customer.paidcardrightsrel.service.PaidCardRightsRelService;
import com.wanmi.sbc.customer.paidcardrightsrel.service.PaidCardRightsRelWhereCriteriaBuilder;
import com.wanmi.sbc.customer.paidcardrule.model.root.PaidCardRule;
import com.wanmi.sbc.customer.paidcardrule.repository.PaidCardRuleRepository;
import com.wanmi.sbc.customer.paidcardrule.service.PaidCardRuleService;
import com.wanmi.sbc.customer.paidcardrule.service.PaidCardRuleWhereCriteriaBuilder;
import com.wanmi.sbc.customer.service.CustomerService;
import com.wanmi.sbc.customer.util.PaidSmsSendUtil;
import com.wanmi.sbc.setting.api.provider.baseconfig.BaseConfigQueryProvider;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.baseconfig.BaseConfigRopResponse;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigKey;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ComparatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.domain.Page;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>付费会员业务逻辑</p>
 * @author xuhai
 * @date 2021-01-29 14:03:56
 */
@Slf4j
@Service("PaidCardService")
public class PaidCardService {
	@Autowired
	private PaidCardRepository paidCardRepository;

	@Autowired
	private PaidCardRuleRepository paidCardRuleRepository;

	@Autowired
	private PaidCardRightsRelRepository paidCardRightsRelRepository;

	@Autowired
	private CustomerLevelRightsService customerLevelRightsService;

	@Autowired
	private PaidCardCustomerRelRepository paidCardCustomerRelRepository;

	@Autowired
	private PaidCardRuleService paidCardRuleService;

	@Autowired
	private PaidCardCustomerRelService paidCardCustomerRelService;

	@Autowired
	private PaidCardRightsRelService paidCardRightsRelService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private PaidCardBuyRecordRepository paidCardBuyRecordRepository;

	@Autowired
	private PaidSmsSendUtil paidSmsSendUtil;
	@Autowired
	private BinderAwareChannelResolver resolver;

	@Autowired
	private BaseConfigQueryProvider baseConfigQueryProvider;

	@Autowired
	private SystemConfigQueryProvider systemConfigQueryProvider;




	/** 
	 * 新增付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCard add(PaidCardAddRequest paidCardAddRequest) {
	//PaidCard paidCard = new PaidCard();

		// 校验名称是否存在
		String name = paidCardAddRequest.getName();
	  	Integer count = this.paidCardRepository.countByNameAndDelFlag(name,DeleteFlag.NO);
		if(count >0){
			// 说明该名称已经存在
			throw new SbcRuntimeException("k-600001","付费会员名称已经存在");
		}
		// 检查spu编码是否存在
		count = this.paidCardRepository.countByErpSpuCodeAndDelFlag(paidCardAddRequest.getErpSpuCode(),DeleteFlag.NO);
		if(count >0){
			// 说明该名称已经存在
			throw new SbcRuntimeException("k-600002","付费会员ErpSpuCode已经存在");
		}



		PaidCard paidCard = KsBeanUtil.convert(paidCardAddRequest,PaidCard.class);
		paidCard.setId(UUIDUtil.getUUID());
		paidCard.setUpdateTime(paidCard.getCreateTime());
		paidCard.setUpdatePerson(paidCard.getCreatePerson());
		paidCard.setAccessType(AccessType.BUY);
		paidCardRepository.save(paidCard);

		// 维护付费续费规则信息
		List<PaidCardRuleAddRequest> ruleList = paidCardAddRequest.getRuleList();
		List<PaidCardRule> paidCardRuleList = KsBeanUtil.convertList(ruleList, PaidCardRule.class);
		// 检查sku编码是否存在
		List<String> skuCodeList = paidCardRuleList.stream()
				.filter(rule -> StatusEnum.ENABLE.equals(rule.getStatus()))
				.map(PaidCardRule::getErpSkuCode).distinct().collect(Collectors.toList());
		count = this.paidCardRuleRepository.countByErpSkuCodeIn(skuCodeList);
		if(count >0){
			// 说明该名称已经存在
			throw new SbcRuntimeException("k-600003","付费会员ErpSkuCode已经存在");
		}
		//填充规则数据
		paidCardRuleList.forEach(rule->{
			rule.setPaidCardId(paidCard.getId());
			rule.setId(UUIDUtil.getUUID());
		});

		paidCardRuleRepository.saveAll(paidCardRuleList);

		//维护付费会员关联信息
		List<PaidCardRightsRelAddRequest> rightsList = paidCardAddRequest.getRightsList();
		List<PaidCardRightsRel> paidCardRightsRels = KsBeanUtil.convertList(rightsList, PaidCardRightsRel.class);
		//填充信息
		paidCardRightsRels.forEach(rights->{
			rights.setId(UUIDUtil.getUUID());
			rights.setPaidCardId(paidCard.getId());
		});
		this.paidCardRightsRelRepository.saveAll(paidCardRightsRels);
		return paidCard;
	}
	
	/** 
	 * 修改付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCard modify(PaidCardModifyRequest paidCardModifyRequest) {

		// 更新付费卡主体信息
		PaidCard paidCard = this.paidCardRepository.getOne(paidCardModifyRequest.getId());

		if(AccessType.BUY.equals(paidCard.getAccessType())) {
			// 校验spuCode是否已经存在
			String erpSpuCode = paidCardModifyRequest.getErpSpuCode();
			Integer count = this.paidCardRepository.countByErpSpuCode4Modify(paidCardModifyRequest.getId(),erpSpuCode);
			if(count >0){
				// 说明该名称已经存在
				throw new SbcRuntimeException("k-600002","付费会员ErpSpuCode已经存在");
			}
		}

		paidCard.setUpdatePerson(paidCardModifyRequest.getUpdatePerson());
		paidCard.setUpdateTime(paidCardModifyRequest.getUpdateTime());
		paidCard.setIcon(paidCardModifyRequest.getIcon());
		paidCard.setBackground(paidCardModifyRequest.getBackground());
		paidCard.setBgType(paidCardModifyRequest.getBgType());
		paidCard.setRule(paidCardModifyRequest.getRule());
		paidCard.setAgreement(paidCardModifyRequest.getAgreement());
		paidCard.setTextColor(paidCardModifyRequest.getTextColor());
		paidCard.setDiscountRate(paidCardModifyRequest.getDiscountRate());
		paidCard.setErpSpuCode(paidCardModifyRequest.getErpSpuCode());
		paidCardRepository.save(paidCard);

		// 先删除
		paidCardRuleRepository.deleteByPaidCardId(paidCard.getId());
		//用户购买可进行费用操作，有赞同步的卡暂不支持
		if(AccessType.BUY.equals(paidCard.getAccessType())) {
			// 更新付费规则信息
			List<PaidCardRuleModifyRequest> ruleList = paidCardModifyRequest.getRuleList();
			List<PaidCardRule> paidCardRuleList = KsBeanUtil.convertList(ruleList, PaidCardRule.class);

			List<String> skuCodeList = paidCardRuleList.stream()
					.filter(rule -> StatusEnum.ENABLE.equals(rule.getStatus()))
					.map(PaidCardRule::getErpSkuCode).collect(Collectors.toList());
			// 检查sku编码是否存在
			Integer count = this.paidCardRuleRepository.countByErpSkuCode4Modify(paidCardModifyRequest.getId(),skuCodeList);
			if(count >0){
				// 说明该名称已经存在
				throw new SbcRuntimeException("k-600003","付费会员ErpSkuCode已经存在");
			}
			this.paidCardRuleRepository.saveAll(paidCardRuleList);
		}

		//更新权益关联信息
		List<PaidCardRightsRelModifyRequest> rightsList = paidCardModifyRequest.getRightsList();
		this.paidCardRightsRelRepository.deleteByPaidCardId(paidCard.getId());
		List<PaidCardRightsRel> paidCardRightsRels = KsBeanUtil.convertList(rightsList, PaidCardRightsRel.class);
		paidCardRightsRels.forEach(paidCardRightsRel->{
			paidCardRightsRel.setId(UUIDUtil.getUUID());
			paidCardRightsRel.setPaidCardId(paidCard.getId());
		});
		this.paidCardRightsRelRepository.saveAll(paidCardRightsRels);

		return paidCard;
	}

	/**
	 * 单个删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteById(String id) {
		paidCardRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		paidCardRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询付费会员
	 * @author xuhai
	 */
	public PaidCard getById(String id){
		return paidCardRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询付费会员
	 * @author xuhai
	 */
	public Page<PaidCard> page(PaidCardQueryRequest queryReq){
		Page<PaidCard> page = paidCardRepository.findAll(
				PaidCardWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
		if(!page.hasContent()){
			return page;
		}
		List<PaidCard> content = page.getContent();
		List<String> cardIdList = content.stream().map(PaidCard::getId).collect(Collectors.toList());
		List<Object> resultList =  paidCardCustomerRelRepository.findByPaidCardGroup(cardIdList, DeleteFlag.NO);
		resultList.forEach(row->{
			Object[] cells = (Object[]) row;
			Long count = ((BigInteger) cells[0]).longValue();
			String pId = (String) cells[1];
			PaidCard paidCard = content.stream().filter(card -> card.getId().equals(pId)).findFirst().orElse(null);
			if(Objects.nonNull(paidCard)){
				paidCard.setCustomerNum(count);
			}
		});

		// 加载权益信息
		List<PaidCardRightsRel> paidCardRightsRels = this.paidCardRightsRelService.list(PaidCardRightsRelQueryRequest.builder()
				.paidCardIdList(cardIdList)
				.build());
		Map<String, List<PaidCardRightsRel>> rightsMap = paidCardRightsRels.stream().collect(Collectors.groupingBy(PaidCardRightsRel::getPaidCardId));
		//获取所有的权益id
		List<Integer> rightsIdList = paidCardRightsRels.stream().map(PaidCardRightsRel::getRightsId).distinct().collect(Collectors.toList());

		//加载所有的权益数据
		Map<Integer, CustomerLevelRights> customerLevelRightsMap = this.customerLevelRightsService.list(CustomerLevelRightsQueryRequest.builder()
				.rightsIdList(rightsIdList)
				.build()).stream().collect(Collectors.toMap(CustomerLevelRights::getRightsId, Function.identity()));
		content.forEach(paidCard->{
			String id = paidCard.getId();
			List<PaidCardRightsRel> rightsRels = rightsMap.get(id);
			List<Integer> rightsIds = rightsRels.stream().map(PaidCardRightsRel::getRightsId).collect(Collectors.toList());
			List<String> rightsNameList = new ArrayList<>();
			rightsIds.forEach(rightId -> {
				CustomerLevelRights customerLevelRights = customerLevelRightsMap.get(rightId);
				if(Objects.nonNull(customerLevelRights)) {
					rightsNameList.add(customerLevelRights.getRightsName());
				}
			});
			paidCard.setRightsNameList(rightsNameList);
		});
		return page;
	}
	
	/** 
	 * 列表查询付费会员
	 * @author xuhai
	 */
	public List<PaidCard> list(PaidCardQueryRequest queryReq){
		return paidCardRepository.findAll(
				PaidCardWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 将实体包装成VO
	 * @author xuhai
	 */
	public PaidCardVO wrapperVo(PaidCard paidCard) {
		if (paidCard != null){
			PaidCardVO paidCardVO=new PaidCardVO();
			KsBeanUtil.copyPropertiesThird(paidCard,paidCardVO);
			return paidCardVO;
		}
		return null;
	}

	/**
	 * 填充付费卡信息
	 * @param paidCardVO
	 * @return
	 */
	public PaidCardVO fillPaidCardInfo(PaidCardVO paidCardVO) {
		String id = paidCardVO.getId();
		// 填充付费规则信息
		PaidCardRuleQueryRequest paidCardRuleQueryRequest = PaidCardRuleQueryRequest.builder().paidCardId(id).build();
		List<PaidCardRule> paidCardRules = this.paidCardRuleRepository.findAll(PaidCardRuleWhereCriteriaBuilder.build(paidCardRuleQueryRequest));
		paidCardRules.sort(Comparator.comparing(PaidCardRule::getType)
				.thenComparing(PaidCardRule::getTimeUnit)
				.thenComparing(PaidCardRule::getTimeVal));
		paidCardVO.setRuleVOList(KsBeanUtil.convertList(paidCardRules, PaidCardRuleVO.class));

		//填充权益信息
		List<PaidCardRightsRel> paidCardRightsRels = this.paidCardRightsRelRepository.findAll(
				PaidCardRightsRelWhereCriteriaBuilder.build(
						PaidCardRightsRelQueryRequest.builder().paidCardId(id).build()
				)
		);
		List<PaidCardRightsRelVO> paidCardRightsRelVOS = KsBeanUtil.convertList(paidCardRightsRels, PaidCardRightsRelVO.class);
		paidCardVO.setRightsVOList(paidCardRightsRelVOS);

		return paidCardVO;
	}

	/**
	 * 修改付费卡启用状态
	 * @param id
	 * @param enable
	 */
	public void changeEnableStatus(String id, EnableEnum enable) {
		PaidCard paidCard = this.paidCardRepository.getOne(id);
		if(!paidCard.getEnable().equals(enable)){
			paidCard.setEnable(enable);
			this.paidCardRepository.save(paidCard);
		}
	}

	public BaseResponse<List<PaidCardVO>> queryCardInfo(CustomerPaidCardQueryRequest request) {
		// 加载所有的付费卡
		List<PaidCard> paidCards = this.list(PaidCardQueryRequest.builder()
				.delFlag(DeleteFlag.NO)
				.build());
		if(CollectionUtils.isEmpty(paidCards)){
			return BaseResponse.SUCCESSFUL();
		}

		List<String> paidCardIdList = paidCards.stream().map(PaidCard::getId).collect(Collectors.toList());

		// 加载所有的规则数据
		List<PaidCardRule> paidCardRules = this.paidCardRuleService.list(PaidCardRuleQueryRequest.builder()
				.status(StatusEnum.ENABLE)
				.paidCardIdList(paidCardIdList)
				.build());

		paidCardRules.sort(Comparator.comparing(PaidCardRule::getType)
				.thenComparing(PaidCardRule::getTimeUnit)
				.thenComparing(PaidCardRule::getTimeVal));
		List<PaidCardRuleVO> allPaidCardRuleVOList = KsBeanUtil.convertList(paidCardRules, PaidCardRuleVO.class);
		Map<String, List<PaidCardRuleVO>> paidCardRulesMap = allPaidCardRuleVOList.stream().collect(Collectors.groupingBy(PaidCardRuleVO::getPaidCardId));

		// 加载权益信息
		List<PaidCardRightsRel> paidCardRightsRels = paidCardRightsRelService.list(PaidCardRightsRelQueryRequest.builder()
				.paidCardIdList(paidCardIdList)
				.build());

		List<PaidCardRightsRelVO> paidCardRightsRelVOS = KsBeanUtil.convertList(paidCardRightsRels, PaidCardRightsRelVO.class);

		//获取所有的权益id
		List<Integer> rightsIdList = paidCardRightsRelVOS.stream().map(PaidCardRightsRelVO::getRightsId).distinct().collect(Collectors.toList());

		//加载所有的权益数据
		List<CustomerLevelRights> customerLevelRightsList = this.customerLevelRightsService.list(CustomerLevelRightsQueryRequest.builder()
				.rightsIdList(rightsIdList)
				.build());
		List<CustomerLevelRightsVO> customerLevelRightsVOS = KsBeanUtil.convertList(customerLevelRightsList, CustomerLevelRightsVO.class);
		//将权益数据回填到付费卡关联权限实体
		paidCardRightsRelVOS.forEach(paidCardRightsRelVO->{
			Integer rightsId = paidCardRightsRelVO.getRightsId();
			CustomerLevelRightsVO customerLevelRightsVO = customerLevelRightsVOS
					.stream()
					.filter(customerLevelRights->customerLevelRights.getRightsId().equals(rightsId))
					.findFirst()
					.orElse(null);
			paidCardRightsRelVO.setCustomerLevelRights(customerLevelRightsVO);
		});

		// 讲权益数据按照付费卡id分组
		Map<String, List<PaidCardRightsRelVO>> paidCardRightsMap = paidCardRightsRelVOS.stream().collect(Collectors.groupingBy(PaidCardRightsRelVO::getPaidCardId));
		// 转化成VO
		List<PaidCardVO> paidCardVOS = KsBeanUtil.convertList(paidCards, PaidCardVO.class);

		// 检索当前用户所有已经购买的付费卡
		List<PaidCardCustomerRel> customerRels = this.paidCardCustomerRelService.list(PaidCardCustomerRelQueryRequest.builder()
				.delFlag(DeleteFlag.NO)
				.customerId(request.getCustomerId())
				.build());

		List<PaidCardCustomerRelVO> customerRelVOS = KsBeanUtil.convertList(customerRels, PaidCardCustomerRelVO.class);

		// 加载付费卡配置信息
		ConfigQueryRequest configQueryRequest = new ConfigQueryRequest();
		configQueryRequest.setConfigKey(ConfigKey.PAID_CARD.toValue());
		configQueryRequest.setConfigType(ConfigType.PAID_CARD_CONFIG.toValue());
		configQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
		log.info("查询付费会员基本信息，入参 ConfigQueryRequest：",configQueryRequest);
		BaseResponse<SystemConfigTypeResponse> response = systemConfigQueryProvider.findByConfigTypeAndDelFlag(configQueryRequest);
		String context = response.getContext().getConfig().getContext();
		HashMap<String, Object> configMap = JSON.parseObject(context, HashMap.class);
		Integer remainDay = Integer.valueOf(configMap.get("remainDay").toString());
		// 回填付费卡数据
		LocalDateTime now = LocalDateTime.now();
		paidCardVOS = paidCardVOS.stream().map(paidCardVO -> {
			String paidCardVOId = paidCardVO.getId();
			List<PaidCardRuleVO> paidCardRuleVOS = paidCardRulesMap.get(paidCardVOId);
			List<PaidCardRightsRelVO> rightsRelVOS = paidCardRightsMap.get(paidCardVOId);
			paidCardVO.setRuleVOList(paidCardRuleVOS);
			paidCardVO.setRightsVOList(rightsRelVOS);

			if (CollectionUtils.isNotEmpty(customerRels)) {
				//给付费卡打标识
				PaidCardCustomerRelVO rel = customerRelVOS.stream()
						.filter(customerRel -> customerRel.getPaidCardId().equals(paidCardVOId))
						.sorted(Comparator.comparing(PaidCardCustomerRelVO::getBeginTime).reversed())
						.findFirst().orElse(null);
				if(Objects.nonNull(rel)){
					paidCardVO.setPaidCardCustomerRel(rel);
					paidCardVO.setIsHave(rel.getEndTime().isAfter(LocalDateTime.now()) ? Boolean.TRUE : Boolean.FALSE);
					if(rel.getEndTime().minusDays(remainDay).isBefore(now)){
						paidCardVO.setIsNeedRemain(Boolean.TRUE);
					}
				}
			}
			return paidCardVO;
		}).filter(v -> !(Boolean.FALSE.equals(v.getIsHave()) && EnableEnum.DISABLE.equals(v.getEnable()))).collect(Collectors.toList());

		//按是否开通->是否到期->是否有赞迁移->卡创建时间排序
		paidCardVOS.sort(Comparator.comparing(PaidCardVO::getIsHave).thenComparing((o1, o2) -> {
			//有绑定关系就证明开通过
			if(Objects.nonNull(o1.getPaidCardCustomerRel()) && Objects.nonNull(o2.getPaidCardCustomerRel())
					|| Objects.isNull(o1.getPaidCardCustomerRel()) && Objects.isNull(o2.getPaidCardCustomerRel())){
				return 0;
			}else if(Objects.nonNull(o1.getPaidCardCustomerRel())){
				return 1;
			} else {
				return -1;
			}
		}).thenComparing(Comparator.comparing(PaidCardVO::getAccessType).reversed()).thenComparing(PaidCardVO::getCreateTime).reversed());
		return BaseResponse.success(paidCardVOS);
	}

	@Transactional(rollbackFor = Exception.class)
    public void dealPayCallBack(PaidCardRedisDTO paidCardRedisDTO) {
		String payCode = paidCardRedisDTO.getBusinessId();
		String customerId = paidCardRedisDTO.getCustomerId();
		String ruleId = paidCardRedisDTO.getRuleId();
		Customer customer = customerService.findById(customerId);
		PaidCardRule paidCardRule = this.paidCardRuleRepository.findById(ruleId).get();

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
			LocalDateTime end = this.paidCardRuleService.getEndTime(now,paidCardRule);
			paidCardCustomerRel.setEndTime(end);

		}else{
			//说明是续费
			// 查询出当前的实例
			paidCardCustomerRel =
					paidCardCustomerRelRepository.findInstanceByPaidCard(paidCardRule.getPaidCardId(),now,customer.getCustomerId());
			// 更新数据
			beginTime = paidCardCustomerRel.getEndTime();
			LocalDateTime end = this.paidCardRuleService.getEndTime(paidCardCustomerRel.getEndTime(),paidCardRule);
			paidCardCustomerRel.setEndTime(end);
		}
		paidCardCustomerRel.setSendMsgFlag(Boolean.FALSE);
		paidCardCustomerRel.setSendExpireMsgFlag(Boolean.FALSE);
		paidCardCustomerRelRepository.save(paidCardCustomerRel);

		//生成购买记录
		PaidCardBuyRecord paidCardBuyRecord = new PaidCardBuyRecord();
		paidCardBuyRecord.setPayCode(payCode);
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
						paidCard.getName() + paidCardRule.getName(),
						endTime.getYear()+"",
						endTime.getMonth().getValue()+"",
						endTime.getDayOfMonth()+""
				});
		BaseConfigRopResponse config = baseConfigQueryProvider.getBaseConfig().getContext();
		Map<String, Object> params4Message = new HashMap<>();
		Map<String, Object> routeParam = new HashMap<>();
		routeParam.put("type",12);
		params4Message.put("params",Arrays.asList(paidCard.getName() +paidCardRule.getName(),
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


		// 维护es中的的会员信息
		// 更新es
		Map<String, Object>params = new HashMap<>();
		params.put("customerId", paidCardBuyRecord.getCustomerId());

		List<Map<String, String>> esPaidCardList = new ArrayList<>();
		Map<String, String> paidCardMap = new HashMap<>();
		paidCardMap.put("paidCardName", paidCardBuyRecord.getPaidCardName());
		paidCardMap.put("paidCardId", paidCardBuyRecord.getPaidCardId());
		params.put("esPaidCardList", paidCardMap);
		resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO)
				.send(new GenericMessage<>(JSONObject.toJSONString(params)));

		ERPTradePayChannel erpTradePayChannel = null;
		Integer payType = paidCardRedisDTO.getPayType();
		if(payType == 0){
			erpTradePayChannel = ERPTradePayChannel.weixin;
		}else if(payType == 1){
			erpTradePayChannel = ERPTradePayChannel.aliPay;
		}else{
			erpTradePayChannel = ERPTradePayChannel.other;
		}

		//将付费卡订单推送到ERP
		this.paidCardRuleService.pushPaidCard(PaidCardERPPushDTO.builder()
				.account(customer.getCustomerAccount())
				.payTime(paidCardBuyRecord.getCreateTime())
				.payTypeCode(erpTradePayChannel.toValue())
				.phone(customer.getCustomerDetail().getContactPhone())
				.platformCode(UUIDUtil.getUUID())
				.price(paidCardRule.getPrice().toString())
				//.shopCode("99999")
				.skuCode(paidCardRule.getErpSkuCode())
				.spuCode(paidCard.getErpSpuCode())
				.vipCode(customer.getCustomerAccount())
				.build());


	}

	public void sendWillExpireSms(List<PaidCardExpireRequest> requestList) {
		BaseConfigRopResponse config = baseConfigQueryProvider.getBaseConfig().getContext();
		requestList.forEach(req->{
			paidSmsSendUtil.send(PaidCardSmsTemplate.PAID_CARD_WILL_VALID_REMAIN_CODE, new String[]{
							req.getPhone()
					},

					new String[]{
							req.getPaidCardName() ,
							req.getYear(),
							req.getMonth(),
							req.getDay()
					});
			log.info("发送短信结束");
			Map<String, Object> params4Message = new HashMap<>();
			Map<String, Object> routeParam = new HashMap<>();
			routeParam.put("type",12);
			params4Message.put("params",Arrays.asList(req.getPaidCardName() ,
					req.getYear(),
					req.getMonth(),
					req.getDay(),
					config.getMobileWebsite()+PaidCardConstant.PAID_CARD_REBUY_ADDRESS)
					);
			params4Message.put("customerId", req.getCustomerId());
			params4Message.put("routeParam", routeParam);
			params4Message.put("nodeType", 1);
			params4Message.put("nodeCode", PaidCardSmsTemplate.PAID_CARD_WILL_VALID_REMAIN_CODE.name());
			log.info("站内信：{}",params4Message);
			resolver.resolveDestination(MQConstant.PAID_CARD_MESSAGE_Q_NAME)
					.send(new GenericMessage<>(JSONObject.toJSONString(params4Message)));

		});



	}

	public void sendExpireSms(List<PaidCardExpireRequest> requestList) {
		BaseConfigRopResponse config = baseConfigQueryProvider.getBaseConfig().getContext();
		requestList.forEach(req->{
			paidSmsSendUtil.send(PaidCardSmsTemplate.PAID_CARD_VALID_REMAIN_CODE, new String[]{
							req.getPhone()
					},
//					您好，您的%s将于%s年%s月%s日到期，为了不影响您的权益使用，请登陆商城及时续费。
					new String[]{
							req.getPaidCardName()
					});
			Map<String, Object> params4Message = new HashMap<>();
			Map<String, Object> routeParam = new HashMap<>();
			routeParam.put("type",12);
			params4Message.put("params",Arrays.asList(req.getPaidCardName(),req.getPaidCardName(),
					config.getMobileWebsite()+PaidCardConstant.PAID_CARD_REBUY_ADDRESS));
			params4Message.put("customerId", req.getCustomerId());
			params4Message.put("routeParam", routeParam);
			params4Message.put("nodeType", 1);
			params4Message.put("nodeCode", PaidCardSmsTemplate.PAID_CARD_VALID_REMAIN_CODE.name());
			log.info("站内信：{}",params4Message);
			resolver.resolveDestination(MQConstant.PAID_CARD_MESSAGE_Q_NAME)
					.send(new GenericMessage<>(JSONObject.toJSONString(params4Message)));
		});
	}
}
