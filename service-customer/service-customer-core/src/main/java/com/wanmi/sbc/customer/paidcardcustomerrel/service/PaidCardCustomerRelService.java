package com.wanmi.sbc.customer.paidcardcustomerrel.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.IteratorUtils;
import com.wanmi.sbc.common.util.UUIDUtil;
import com.wanmi.sbc.customer.api.constant.CustomerErrorCode;
import com.wanmi.sbc.customer.api.request.customer.CustomerDeletePaidCardRequest;
import com.wanmi.sbc.customer.api.request.paidcard.PaidCardQueryRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.MaxDiscountPaidCardRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelPageRequest;
import com.wanmi.sbc.customer.bean.enums.EnableEnum;
import com.wanmi.sbc.customer.bean.vo.PaidCardVO;
import com.wanmi.sbc.customer.paidcard.model.root.PaidCard;
import com.wanmi.sbc.customer.paidcard.service.PaidCardService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.customer.paidcardcustomerrel.repository.PaidCardCustomerRelRepository;
import com.wanmi.sbc.customer.paidcardcustomerrel.model.root.PaidCardCustomerRel;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelQueryRequest;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>付费会员业务逻辑</p>
 * @author xuhai
 * @date 2021-01-29 14:03:59
 */
@Service("PaidCardCustomerRelService")
public class PaidCardCustomerRelService {
	@Autowired
	private PaidCardCustomerRelRepository paidCardCustomerRelRepository;

	@Autowired
	private PaidCardService paidCardService;
	@Autowired
	private BinderAwareChannelResolver resolver;

	
	/** 
	 * 新增付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardCustomerRel add(PaidCardCustomerRel entity) {
		List<PaidCardCustomerRel> list = list(PaidCardCustomerRelQueryRequest.builder().delFlag(DeleteFlag.NO).customerId(entity.getCustomerId()).build());
		if(CollectionUtils.isNotEmpty(list)) {
			list.forEach(paidCardCustomerRel -> {
				if (Objects.equals(entity.getPaidCardId(),paidCardCustomerRel.getPaidCardId())) {
					throw new SbcRuntimeException(CustomerErrorCode.PAID_CUSTOMER_EXIST);
				}
			});
		}
		entity.setId(UUIDUtil.getUUID());
		paidCardCustomerRelRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改付费会员
	 * @author xuhai
	 */
	@Transactional
	public PaidCardCustomerRel modify(PaidCardCustomerRel entity) {
		paidCardCustomerRelRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteById(String id) {
		paidCardCustomerRelRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除付费会员
	 * @author xuhai
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		paidCardCustomerRelRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询付费会员
	 * @author xuhai
	 */
	public PaidCardCustomerRel getById(String id){
		return paidCardCustomerRelRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询付费会员
	 * @author xuhai
	 */
	public Page<PaidCardCustomerRel> page(PaidCardCustomerRelQueryRequest queryReq){
		return paidCardCustomerRelRepository.findAll(
				PaidCardCustomerRelWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}


	/**
	 * 批量数据的时候使用 分页查询付费会员
	 * @author xuhai
	 */
	public List<PaidCardCustomerRel> pageByMaxAutoId(PaidCardCustomerRelQueryRequest request){
//		Sort sort = Sort.by(Sort.Order.asc("tmpId"));
//		Specification<PaidCardCustomerRel> build = PaidCardCustomerRelWhereCriteriaBuilder.build(request);
//		Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
//		Page<PaidCardCustomerRel> all = paidCardCustomerRelRepository.findAll(build, pageable);
//		return all.getContent();
		return paidCardCustomerRelRepository.pageByMaxAutoId(request.getPaidCardIdList(), request.getCurrentTime(), request.getMaxTmpId(), request.getPageSize());
	}

	/**
	 * 获取30天即将过期的会员卡信息
	 * @author xuhai
	 */
	public List<PaidCardCustomerRel> pageSendMsgFlagByEndTimeAndMaxAutoId(PaidCardCustomerRelQueryRequest request){
		return paidCardCustomerRelRepository.pageSendMsgFlagByEndTimeAndMaxAutoId(request.getEndTimeEnd(), request.getMaxTmpId(), request.getPageSize());
	}


	/**
	 * 获取已经过期的会员卡信息
	 * @author xuhai
	 */
	public List<PaidCardCustomerRel> pageSendExpireMsgFlagByEndTimeAndMaxAutoId(PaidCardCustomerRelQueryRequest request){
		return paidCardCustomerRelRepository.pageSendExpireMsgFlagByEndTimeAndMaxAutoId(request.getEndTimeEnd(), request.getMaxTmpId(), request.getPageSize());
	}
	
	/** 
	 * 列表查询付费会员
	 * @author xuhai
	 */
	public List<PaidCardCustomerRel> list(PaidCardCustomerRelQueryRequest queryReq){
		return paidCardCustomerRelRepository.findAll(
				PaidCardCustomerRelWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}




	/**
	 * 将实体包装成VO
	 * @author xuhai
	 */
	public PaidCardCustomerRelVO wrapperVo(PaidCardCustomerRel paidCardCustomerRel) {
		if (paidCardCustomerRel != null){
			PaidCardCustomerRelVO paidCardCustomerRelVO=new PaidCardCustomerRelVO();
			KsBeanUtil.copyPropertiesThird(paidCardCustomerRel,paidCardCustomerRelVO);
			paidCardCustomerRelVO.setMaxTmpId(paidCardCustomerRel.getTmpId());
			return paidCardCustomerRelVO;
		}
		return null;
	}

	public BaseResponse<List<PaidCardCustomerRelVO>> getRelInfo(PaidCardCustomerRelQueryRequest request) {
		List<PaidCardCustomerRel> list = paidCardCustomerRelRepository.findAll(PaidCardCustomerRelWhereCriteriaBuilder.build(request));
		list = list.stream().filter(IteratorUtils.distinctByKey(PaidCardCustomerRel::getCustomerId)).collect(Collectors.toList());
		List<PaidCardCustomerRelVO> paidCardCustomerRelVOS = KsBeanUtil.convertList(list, PaidCardCustomerRelVO.class);
		return BaseResponse.success(paidCardCustomerRelVOS);
	}

	/**
	 * 查询用户所有的付费卡信息
	 * @param request
	 * @return
	 */
	public List<PaidCardCustomerRelVO> listCustomerRelFullInfo(PaidCardCustomerRelListRequest request) {
		List<PaidCardCustomerRelVO>paidCardCustomerRelVOList = null;
		PaidCardCustomerRelQueryRequest.PaidCardCustomerRelQueryRequestBuilder builder = PaidCardCustomerRelQueryRequest.builder()
				.customerId(request.getCustomerId()).delFlag(request.getDelFlag()).endTimeFlag(request.getEndTimeFlag()).customerIdList(request.getCustomerIdList());
		if(Objects.nonNull(request.getEndTimeBegin())){
			builder = builder.endTimeBegin(request.getEndTimeBegin());
		}
		List<PaidCardCustomerRel> paidCardCustomerRels = this.list(builder.build());
		if(CollectionUtils.isNotEmpty(paidCardCustomerRels)){
			paidCardCustomerRelVOList = KsBeanUtil.convertList(paidCardCustomerRels, PaidCardCustomerRelVO.class);
			List<String> paidCardIdList = paidCardCustomerRelVOList.stream().map(PaidCardCustomerRelVO::getPaidCardId).collect(Collectors.toList());
			List<PaidCard> paidCardList = paidCardService.list(PaidCardQueryRequest.builder().idList(paidCardIdList).build());
			List<PaidCardVO> paidCardVOS = KsBeanUtil.convertList(paidCardList, PaidCardVO.class);
			paidCardCustomerRelVOList.forEach(paidCardCustomerRelVO->{
				PaidCardVO paidCardVO = paidCardVOS.stream().filter(paidCard -> paidCard.getId().equals(paidCardCustomerRelVO.getPaidCardId())).findFirst().orElse(null);
				if(DeleteFlag.NO.equals(paidCardCustomerRelVO.getDelFlag()) && paidCardCustomerRelVO.getEndTime().isAfter(LocalDateTime.now())) {
					paidCardVO.setIsHave(Boolean.TRUE);
				}
				paidCardCustomerRelVO.setPaidCardVO(paidCardVO);
			});
			//平台禁用且该会员未拥有的不展示
			paidCardCustomerRelVOList = paidCardCustomerRelVOList.stream().filter(rel -> !(EnableEnum.DISABLE.equals(rel.getPaidCardVO().getEnable())
			&& (DeleteFlag.YES.equals(rel.getDelFlag())
					|| !LocalDateTime.now().isBefore(rel.getEndTime())))).collect(Collectors.toList());

            paidCardCustomerRelVOList.sort(Comparator.comparing(PaidCardCustomerRelVO::getBeginTime));
		}

		return paidCardCustomerRelVOList;
	}

	public BaseResponse deleteCustomerPaidCard(CustomerDeletePaidCardRequest request) {
		PaidCardCustomerRel paidCardCustomerRel = this.paidCardCustomerRelRepository.findById(request.getPaidCardRelId()).orElse(null);
		if(Objects.nonNull(paidCardCustomerRel) && paidCardCustomerRel.getDelFlag().equals(DeleteFlag.NO)){
			paidCardCustomerRel.setDelFlag(DeleteFlag.YES);
			this.paidCardCustomerRelRepository.save(paidCardCustomerRel);
			// 更新es
			Map<String, Object> params = new HashMap<>();
			params.put("customerId", paidCardCustomerRel.getCustomerId());
			Map<String, String> paidCardMap = new HashMap<>();
			params.put("operType", "delete");
			paidCardMap.put("paidCardId", paidCardCustomerRel.getPaidCardId());
			params.put("esPaidCardList", paidCardMap);
			resolver.resolveDestination(MQConstant.Q_ES_SERVICE_CUSTOMER_MODIFY_BASE_INFO)
					.send(new GenericMessage<>(JSONObject.toJSONString(params)));
		}
		return BaseResponse.SUCCESSFUL();
	}

	@Transactional
	public void changeSendMsgFlag(List<String> relIdList) {
		this.paidCardCustomerRelRepository.changeSendMsgFlag(relIdList);
	}

	/**
	 * 分页查询付费会员用户id
	 * @param pageRequest
	 * @return
	 */
	public List<String> listCustomerIdByPageable(PageRequest pageRequest){
		return paidCardCustomerRelRepository.findCustomerIdByPageable(pageRequest);
	}


	@Transactional
	public void changeExpireSendMsgFlag(List<String> relIdList) {
		this.paidCardCustomerRelRepository.changeExpireSendMsgFlag(relIdList);
	}

	public List<PaidCardVO> getMaxDiscountPaidCard(MaxDiscountPaidCardRequest request) {
		String customerId = request.getCustomerId();
		List<PaidCardCustomerRel> paidCardCustomerRels = this.list(PaidCardCustomerRelQueryRequest.builder()
				.customerId(customerId)
				.delFlag(DeleteFlag.NO)
				.endTimeBegin(LocalDateTime.now()).build());
		if(CollectionUtils.isEmpty(paidCardCustomerRels)){
			return new ArrayList<>();
		}
		List<String> paidCardIdList = paidCardCustomerRels.stream().map(PaidCardCustomerRel::getPaidCardId).distinct().collect(Collectors.toList());
		List<PaidCard> paidCardList = this.paidCardService.list(PaidCardQueryRequest.builder().idList(paidCardIdList).build());
		if(CollectionUtils.isEmpty(paidCardList)){
			return new ArrayList<>();
		}
		paidCardList.sort(Comparator.comparing(PaidCard::getDiscountRate));

		return KsBeanUtil.convertList(paidCardList,PaidCardVO.class);
	}
}
