package com.wanmi.sbc.account.customerdrawcash.service;

import com.google.common.collect.Lists;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashQueryRequest;
import com.wanmi.sbc.account.api.request.customerdrawcash.CustomerDrawCashStatusQueryRequest;
import com.wanmi.sbc.account.bean.enums.*;
import com.wanmi.sbc.account.bean.vo.CustomerDrawCashVO;
import com.wanmi.sbc.account.customerdrawcash.model.root.CustomerDrawCash;
import com.wanmi.sbc.account.customerdrawcash.repository.CustomerDrawCashRepository;
import com.wanmi.sbc.account.funds.model.root.CustomerFunds;
import com.wanmi.sbc.account.funds.model.root.CustomerFundsDetail;
import com.wanmi.sbc.account.funds.repository.CustomerFundsDetailRepository;
import com.wanmi.sbc.account.funds.repository.CustomerFundsRepository;
import com.wanmi.sbc.account.funds.service.CustomerFundsService;
import com.wanmi.sbc.account.mq.MessageMqService;
import com.wanmi.sbc.common.base.MessageMQRequest;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.enums.NodeType;
import com.wanmi.sbc.common.enums.node.AccoutAssetsType;
import com.wanmi.sbc.common.util.GeneratorService;
import com.wanmi.sbc.common.util.KsBeanUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>会员提现管理业务逻辑</p>
 * @author chenyufei
 * @date 2019-02-25 17:22:24
 */
@Service("CustomerDrawCashService")
public class CustomerDrawCashService {
	@Autowired
	private CustomerDrawCashRepository customerDrawCashRepository;

	@Autowired
	private CustomerFundsService customerFundsService;

	@Autowired
	private CustomerFundsDetailRepository customerFundsDetailRepository;

	@Autowired
	private CustomerFundsRepository customerFundsRepository;

	@Autowired
	private GeneratorService generatorService;

	@Autowired
	private MessageMqService messageMqService;
	
	/** 
	 * 新增会员提现管理
	 * @author chenyufei
	 */
	@Transactional
	public CustomerDrawCash add(CustomerDrawCash entity) {

		String drawCashNo = generatorService.generate("TX");
		entity.setDrawCashNo(drawCashNo);
		entity.setApplyTime(LocalDateTime.now());
		entity.setDrawCashChannel(DrawCashChannel.WECHAT);
		entity.setAuditStatus(AuditStatus.WAIT);
		entity.setDrawCashStatus(DrawCashStatus.WAIT);
		entity.setCustomerOperateStatus(CustomerOperateStatus.APPLY);
		entity.setFinishStatus(FinishStatus.UNSUCCESS);
		entity.setDelFlag(DeleteFlag.NO);

		CustomerFunds customerFunds  = customerFundsRepository.findByCustomerId(entity.getCustomerId());
		//对应的金额计算
		BigDecimal withdrawAmount = entity.getDrawCashSum();
		customerFundsService.submitWithdrawCashApply(customerFunds.getCustomerFundsId(),withdrawAmount);
		entity.setAccountBalance(customerFunds.getAccountBalance());
		//会员资金明细
		customerDrawCashRepository.save(entity);
		addCustomerFundsDetail(customerFunds,entity);
		//提现申请，发送mq消息
		this.sendMessage(NodeType.ACCOUNT_ASSETS, AccoutAssetsType.BALANCE_WITHDRAW_APPLY_FOR, null, entity.getDrawCashId(), entity.getCustomerId(), entity.getCustomerAccount());
		return entity;
	}

	/**
	 * 新增账户明细记录
	 */
	private void addCustomerFundsDetail(CustomerFunds customerFunds,CustomerDrawCash entity){
		CustomerFundsDetail customerFundsDetail = new CustomerFundsDetail();
		customerFundsDetail.setCustomerId(entity.getCustomerId());
		customerFundsDetail.setFundsType(FundsType.COMMISSION_WITHDRAWAL);
		customerFundsDetail.setSubType(FundsSubType.COMMISSION_WITHDRAWAL);
		customerFundsDetail.setReceiptPaymentAmount(entity.getDrawCashSum());
		customerFundsDetail.setFundsStatus(FundsStatus.NO);
		customerFundsDetail.setAccountBalance(customerFunds.getAccountBalance());
		customerFundsDetail.setCreateTime(LocalDateTime.now());
		customerFundsDetail.setDrawCashId(entity.getDrawCashId());
		customerFundsDetailRepository.save(customerFundsDetail);
	}
	
	/** 
	 * 修改会员提现管理
	 * @author chenyufei
	 */
	@Transactional
	public CustomerDrawCash modify(CustomerDrawCash entity) {
		customerDrawCashRepository.save(entity);
		//提现成功通知阶段触发
		if(CustomerOperateStatus.APPLY.equals(entity.getCustomerOperateStatus()) && DrawCashStatus.SUCCESS.equals(entity.getDrawCashStatus())){
			this.sendMessage(NodeType.ACCOUNT_ASSETS, AccoutAssetsType.BALANCE_WITHDRAW_SUCCESS, null,
					entity.getDrawCashId(), entity.getCustomerId(), entity.getCustomerAccount());
		}
		return entity;
	}

	/**
	 * 单个删除会员提现管理
	 * @author chenyufei
	 */
	@Transactional
	public void deleteById(String id) {
		customerDrawCashRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除会员提现管理
	 * @author chenyufei
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		customerDrawCashRepository.deleteAll(ids.stream().map(id -> {
			CustomerDrawCash entity = new CustomerDrawCash();
			entity.setDrawCashId(id);
			return entity;
		}).collect(Collectors.toList()));
	}

	/**
	 * 修改提现单审核的状态
	 *
	 * @param drawCashIdList 批量提现单Id数据
	 * @param auditStatus 提现单新状态
	 */
	@Transactional(rollbackFor = Exception.class)
	public void updateDrawCashAuditStatus(List<String> drawCashIdList, AuditStatus auditStatus, String rejectReason,
										  FinishStatus finishStatus, DrawCashStatus drawCashStatus,
										  String drawCashFailedReason,BigDecimal accountBalance) {
		customerDrawCashRepository.updateDrawCashAuditStatusBatch(drawCashIdList,auditStatus,rejectReason,
				finishStatus,drawCashStatus,drawCashFailedReason,accountBalance);

		//审核通过/驳回，发送mq消息
		List<CustomerDrawCash> list = this.list(CustomerDrawCashQueryRequest.builder().drawCashIdList(drawCashIdList).sourceFromPlatForm(Boolean.TRUE).build());
		if(CollectionUtils.isNotEmpty(list)){
			if(AuditStatus.PASS.equals(auditStatus) && DrawCashStatus.SUCCESS.equals(drawCashStatus)){
				list.stream().forEach(cash -> {
					this.sendMessage(NodeType.ACCOUNT_ASSETS, AccoutAssetsType.BALANCE_WITHDRAW_SUCCESS, null, cash.getDrawCashId(), cash.getCustomerId(), cash.getCustomerAccount());
				});
			}else if(AuditStatus.REJECT.equals(auditStatus)){
				List<String> params = Lists.newArrayList(rejectReason);
				list.stream().forEach(cash -> {
					this.sendMessage(NodeType.ACCOUNT_ASSETS, AccoutAssetsType.BALANCE_WITHDRAW_REJECT, params, cash.getDrawCashId(), cash.getCustomerId(), cash.getCustomerAccount());
				});
			}
		}

	}
	
	/** 
	 * 单个查询会员提现管理
	 * @author chenyufei
	 */
	public CustomerDrawCash getById(String id){
		return customerDrawCashRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询会员提现管理
	 * @author chenyufei
	 */
	public Page<CustomerDrawCash> page(CustomerDrawCashQueryRequest queryReq){
		return customerDrawCashRepository.findAll(
				CustomerDrawCashWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询会员提现管理
	 * @author chenyufei
	 */
	public List<CustomerDrawCash> list(CustomerDrawCashQueryRequest queryReq){
		return customerDrawCashRepository.findAll(CustomerDrawCashWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author chenyufei
	 */
	public CustomerDrawCashVO wrapperVo(CustomerDrawCash customerDrawCash) {
		if (customerDrawCash != null){
			CustomerDrawCashVO customerDrawCashVO=new CustomerDrawCashVO();
			KsBeanUtil.copyPropertiesThird(customerDrawCash,customerDrawCashVO);
			return customerDrawCashVO;
		}
		return null;
	}

	/**
	 * 统计某个会员当日提现金额
	 * @param request
	 * @return
	 */
	public BigDecimal countDrawCashSum(CustomerDrawCashQueryRequest request){
		return customerDrawCashRepository.countDrawCashSum(request.getCustomerId(), LocalDateTime.of(LocalDate.now(), LocalTime.MIN), LocalDateTime.now());
	}


	/**
	 * 统计当前标签页
	 * @param request
	 * @return
	 */
	public Integer countDrawCashTabNum(CustomerDrawCashStatusQueryRequest request){
		return customerDrawCashRepository.countDrawCashTabNum(request.getAuditStatus(), request.getDrawCashStatus(),
				request.getCustomerOperateStatus(),request.getFinishStatus(),request.getDelFlag());
	}

	/**
	 * 根据会员ID更新会员账号
	 *
	 * @param customerId      会员ID
	 * @param customerAccount 会员账号
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateCustomerAccountByCustomerId(String customerId, String customerAccount) {
		return customerDrawCashRepository.updateCustomerAccountByCustomerId(customerId, customerAccount, LocalDateTime.now());
	}

	/**
	 * 根据会员ID更新会员名称
	 *
	 * @param customerId   会员ID
	 * @param customerName 会员名称
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateCustomerNameByCustomerId(String customerId, String customerName) {
		return customerDrawCashRepository.updateCustomerNameByCustomerId(customerId, customerName, LocalDateTime.now());
	}

	/**
	 * 发送消息
	 * @param nodeType
	 * @param nodeCode
	 * @param params
	 * @param routeParam
	 * @param customerId
	 */
	private void sendMessage(NodeType nodeType, AccoutAssetsType nodeCode, List<String> params, String routeParam, String customerId, String mobile){

		Map<String, Object> map = new HashMap<>();
		map.put("type", nodeType.toValue());
		map.put("node",nodeCode.toValue());
		map.put("id", routeParam);

		MessageMQRequest messageMQRequest = new MessageMQRequest();
		messageMQRequest.setNodeCode(nodeCode.getType());
		messageMQRequest.setNodeType(nodeType.toValue());
		messageMQRequest.setParams(params);
		messageMQRequest.setRouteParam(map);
		messageMQRequest.setCustomerId(customerId);
		messageMQRequest.setMobile(mobile);
		messageMqService.sendMessage(messageMQRequest);
	}
}
