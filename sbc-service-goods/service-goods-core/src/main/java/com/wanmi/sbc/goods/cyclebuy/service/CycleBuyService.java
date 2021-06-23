package com.wanmi.sbc.goods.cyclebuy.service;

import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.request.cyclebuy.CycleBuyQueryRequest;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.enums.DailyIssueType;
import com.wanmi.sbc.goods.bean.enums.DeliveryCycle;
import com.wanmi.sbc.goods.bean.vo.CycleBuyGiftVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuySendDateRuleVO;
import com.wanmi.sbc.goods.bean.vo.CycleBuyVO;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuy;
import com.wanmi.sbc.goods.cyclebuy.model.root.CycleBuyGift;
import com.wanmi.sbc.goods.cyclebuy.repository.CycleBuyGiftRepository;
import com.wanmi.sbc.goods.cyclebuy.repository.CycleBuyRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.common.util.KsBeanUtil;

import java.time.LocalDateTime;
import java.util.Arrays;

import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Locale;

/**
 * <p>周期购活动业务逻辑</p>
 * @author weiwenhao
 * @date 2021-01-21 09:15:37
 */
@Service("CycleBuyService")
public class CycleBuyService {

	@Autowired
	private CycleBuyRepository cycleBuyRepository;

	@Autowired
	private CycleBuyGiftRepository cycleBuyGiftRepository;

	private static final String WEEK_DESC = "每周";

	private static final String MONTH_DESC = "每月-号";

	/** 
	 * 新增周期购活动
	 * @author weiwenhao
	 */
	@Transactional
	public CycleBuy add(CycleBuy entity) {
		entity.setAddedFlag(AddedFlag.YES);
		entity.setDelFlag(DeleteFlag.NO);
		entity.setCreateTime(LocalDateTime.now());
		cycleBuyRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改周期购活动
	 * @author weiwenhao
	 */
	@Transactional
	public CycleBuy modify(CycleBuy entity) {
		entity.setUpdateTime(LocalDateTime.now());
		cycleBuyRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除周期购活动
	 * @author weiwenhao
	 */
	@Transactional
	public void deleteById(Long id) {
		cycleBuyRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除周期购活动
	 * @author weiwenhao
	 */
	@Transactional
	public void deleteByIdList(List<Long> ids) {
		cycleBuyRepository.deleteByIdList(ids);
	}
	
	/** 
	 * 单个查询周期购活动
	 * @author weiwenhao
	 */
	public CycleBuy getById(Long id){
		CycleBuy cycleBuy=cycleBuyRepository.findById(id).orElse(null);
		return cycleBuy;
	}
	
	/** 
	 * 分页查询周期购活动
	 * @author weiwenhao
	 */
	public Page<CycleBuy> page(CycleBuyQueryRequest queryReq){
		return cycleBuyRepository.findAll(
				CycleBuyWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询周期购活动
	 * @author weiwenhao
	 */
	public List<CycleBuy> list(CycleBuyQueryRequest queryReq){
		return cycleBuyRepository.findAll(
				CycleBuyWhereCriteriaBuilder.build(queryReq),
				queryReq.getSort());
	}

	/**
	 * 根据goodsId查询周期购活动
	 * @param goodsId
	 * @return
	 */
	public CycleBuyVO getByGoodsId(String goodsId) {

		//活动主体
		CycleBuy cycleBuy = cycleBuyRepository.findByGoodsId(goodsId);
		CycleBuyVO cycleBuyVO = this.wrapperVo(cycleBuy);
		if(Objects.nonNull(cycleBuyVO)) {
			//赠品
			List<CycleBuyGift> cycleBuyGiftList = cycleBuyGiftRepository.findByCycleBuyId(cycleBuyVO.getId());
			cycleBuyVO.setCycleBuyGiftVOList(KsBeanUtil.convertList(cycleBuyGiftList, CycleBuyGiftVO.class));
			//发货日期规则列表
			List<CycleBuySendDateRuleVO> ruleVOList = this.getCycleBuySendDateRuleVOList(cycleBuyVO.getDeliveryCycle(), cycleBuyVO.getSendDateRule());
			cycleBuyVO.setCycleBuySendDateRuleVOList(ruleVOList);
		}

		return cycleBuyVO;
	}


	/**
	 * 根据goodsId查询周期购活动
	 * @param goodsId
	 * @return
	 */
	public CycleBuyVO getByGoodsDetailsId(String goodsId) {
		//活动主体
		CycleBuy cycleBuy = cycleBuyRepository.findByGoodsId(goodsId);
		CycleBuyVO cycleBuyVO = this.wrapperVo(cycleBuy);
		if(Objects.nonNull(cycleBuyVO)) {
			//赠品
			List<CycleBuyGift> cycleBuyGiftList = cycleBuyGiftRepository.findByCycleBuyId(cycleBuyVO.getId());
			cycleBuyVO.setCycleBuyGiftVOList(KsBeanUtil.convertList(cycleBuyGiftList, CycleBuyGiftVO.class));
		}
		return cycleBuyVO;
	}


	/**
	 * 组装发货日期规则列表
	 * @param deliveryCycle
	 *
	 * @return
	 */
	public List<CycleBuySendDateRuleVO> getCycleBuySendDateRuleVOList(DeliveryCycle deliveryCycle, List<String> rules){

		List<CycleBuySendDateRuleVO> list  = new ArrayList<>();

		//配送周期和发货日期都不为空
		if(Objects.nonNull(deliveryCycle) && CollectionUtils.isNotEmpty(rules)) {
			switch (deliveryCycle) {
				case EVERYDAY:
					rules.forEach(rule -> {
						CycleBuySendDateRuleVO ruleVO = new CycleBuySendDateRuleVO();
						DailyIssueType type = DailyIssueType.fromValue(Integer.parseInt(rule));
						ruleVO.setSendDateRule(rule);
						ruleVO.setRuleDescription(type.getDescription());
						list.add(ruleVO);
					});
					break;
				case WEEKLY:
					rules.forEach(rule -> {
						CycleBuySendDateRuleVO ruleVO = new CycleBuySendDateRuleVO();
						DayOfWeek dayOfWeek = DayOfWeek.of(Integer.parseInt(rule));
						ruleVO.setSendDateRule(rule);
						ruleVO.setRuleDescription(WEEK_DESC + dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.CHINESE));
						list.add(ruleVO);
					});
					break;
				case MONTHLY:
					rules.forEach(rule -> {
						CycleBuySendDateRuleVO ruleVO = new CycleBuySendDateRuleVO();
						ruleVO.setSendDateRule(rule);
						ruleVO.setRuleDescription(MONTH_DESC.replace("-", rule));
						list.add(ruleVO);
					});
					break;
				default:
			}
		}
		return list;
	}

	/**
	 * 将实体包装成VO
	 * @author weiwenhao
	 */
	public CycleBuyVO wrapperVo(CycleBuy cycleBuy) {
		if (cycleBuy != null){
			CycleBuyVO cycleBuyVO=new CycleBuyVO();
			KsBeanUtil.copyPropertiesThird(cycleBuy,cycleBuyVO);
			if (Objects.nonNull(cycleBuy.getSendDateRule())) {
				List<String> sendDateRule= Arrays.asList(cycleBuy.getSendDateRule().split(","));
				cycleBuyVO.setSendDateRule(sendDateRule);
			}
			return cycleBuyVO;
		}
		return null;
	}
}
