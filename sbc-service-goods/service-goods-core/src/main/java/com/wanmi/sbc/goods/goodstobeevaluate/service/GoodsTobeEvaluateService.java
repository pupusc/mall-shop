package com.wanmi.sbc.goods.goodstobeevaluate.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.goods.api.request.goodstobeevaluate.GoodsTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.bean.vo.GoodsTobeEvaluateVO;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import com.wanmi.sbc.goods.goodsevaluate.repository.GoodsEvaluateRepository;
import com.wanmi.sbc.goods.goodstobeevaluate.model.root.GoodsTobeEvaluate;
import com.wanmi.sbc.goods.goodstobeevaluate.repository.GoodsTobeEvaluateRepository;
import com.wanmi.sbc.goods.mq.GoodsEvaluateNumMqService;
import com.wanmi.sbc.setting.api.provider.systemconfig.SystemConfigQueryProvider;
import com.wanmi.sbc.setting.api.request.ConfigQueryRequest;
import com.wanmi.sbc.setting.api.response.systemconfig.SystemConfigTypeResponse;
import com.wanmi.sbc.setting.bean.enums.ConfigType;
import com.wanmi.sbc.setting.bean.vo.ConfigVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>订单商品待评价业务逻辑</p>
 * @author lzw
 * @date 2019-03-20 14:47:38
 */
@Service("GoodsTobeEvaluateService")
public class GoodsTobeEvaluateService {

	@Autowired
	private GoodsTobeEvaluateRepository goodsTobeEvaluateRepository;

	@Autowired
	private GoodsEvaluateRepository goodsEvaluateRepository;

	@Autowired
	private GoodsEvaluateNumMqService goodsEvaluateNumMqService;

	@Autowired
	private SystemConfigQueryProvider systemConfigQueryProvider;

	/**
	 * 新增订单商品待评价
	 * @author lzw
	 */
	@GlobalTransactional
	@Transactional
	public GoodsTobeEvaluate add(GoodsTobeEvaluate entity) {
		goodsTobeEvaluateRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改订单商品待评价
	 * @author lzw
	 */
	@Transactional
	public GoodsTobeEvaluate modify(GoodsTobeEvaluate entity) {
		goodsTobeEvaluateRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除订单商品待评价
	 * @author lzw
	 */
	@Transactional
	public void deleteById(String id) {
		goodsTobeEvaluateRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除订单商品待评价
	 * @author lzw
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		goodsTobeEvaluateRepository.deleteAll(ids.stream().map(id -> {
			GoodsTobeEvaluate entity = new GoodsTobeEvaluate();
			entity.setId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询订单商品待评价
	 * @author lzw
	 */
	public GoodsTobeEvaluate getById(String id){
		return goodsTobeEvaluateRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询订单商品待评价
	 * @author lzw
	 */
	public Page<GoodsTobeEvaluate> page(GoodsTobeEvaluateQueryRequest queryReq){
		return goodsTobeEvaluateRepository.findAll(
				GoodsTobeEvaluateWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询订单商品待评价
	 * @author lzw
	 */
	public List<GoodsTobeEvaluate> list(GoodsTobeEvaluateQueryRequest queryReq){
		return goodsTobeEvaluateRepository.findAll(GoodsTobeEvaluateWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 获取待评价数量
	 * @return
	 */
	public Long getGoodsTobeEvaluateNum(GoodsTobeEvaluateQueryRequest queryReq){
		return goodsTobeEvaluateRepository.count(GoodsTobeEvaluateWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author lzw
	 */
	public GoodsTobeEvaluateVO wrapperVo(GoodsTobeEvaluate goodsTobeEvaluate) {
		if (goodsTobeEvaluate != null){
			GoodsTobeEvaluateVO goodsTobeEvaluateVO=new GoodsTobeEvaluateVO();
			KsBeanUtil.copyPropertiesThird(goodsTobeEvaluate,goodsTobeEvaluateVO);
			return goodsTobeEvaluateVO;
		}
		return null;
	}

	/**
	 * @Description: 订单ID和skuID删除
	 * @param queryReq {@link GoodsTobeEvaluateQueryRequest}
	 * @Author: Bob
	 * @Date: 2019-04-12 16:34
	 */
	@GlobalTransactional
	@Transactional(rollbackFor = Exception.class)
	public int delByOrderIDAndSkuID(GoodsTobeEvaluateQueryRequest queryReq){
		return goodsTobeEvaluateRepository.deleteByOrderNoAndGoodsInfoId(queryReq.getOrderNo(),
				queryReq.getGoodsInfoId());
	}

	/**
	 * @Description: 动态条件查询
	 * @param queryReq {@link GoodsTobeEvaluateQueryRequest}
	 * @Author: Bob
	 * @Date: 2019-04-12 17:22
	 */
	public GoodsTobeEvaluateVO query(GoodsTobeEvaluateQueryRequest queryReq){
		GoodsTobeEvaluate goodsTobeEvaluate =
				goodsTobeEvaluateRepository.findOne(GoodsTobeEvaluateWhereCriteriaBuilder.build(queryReq)).orElse(null);
		return wrapperVo(goodsTobeEvaluate);
	}

	/**
	 * @Author lvzhenwei
	 * @Description 商品自动评价
	 * @Date 9:44 2019/4/25
	 * @Param []
	 * @return void
	 **/
	@Transactional
	public void autoGoodsEvaluate(){
		ConfigQueryRequest request = new ConfigQueryRequest();
		request.setConfigType(ConfigType.ORDER_SETTING_TIMEOUT_EVALUATE.toValue());
		request.setDelFlag(DeleteFlag.NO.toValue());
		ConfigVO config = systemConfigQueryProvider.findByConfigTypeAndDelFlag(request).getContext().getConfig();
		String comment = null;
		if(Objects.nonNull(config) && Constants.yes.equals(config.getStatus())){
			comment = JSON.parseObject(config.getContext()).getString("comment");
		}
		if(StringUtils.isEmpty(comment)){
			comment = "用户未填写评价内容";
		}
		final String finalComment = comment;

		//获取待评价的商品列表数据
		GoodsTobeEvaluateQueryRequest queryReq = new GoodsTobeEvaluateQueryRequest();
		queryReq.setAutoGoodsEvaluateDate(LocalDate.now());
		List<GoodsTobeEvaluate> goodsTobeEvaluateList = goodsTobeEvaluateRepository.findAll(GoodsTobeEvaluateWhereCriteriaBuilder.build(queryReq));
		List<String> idList = new ArrayList<>();
		List<GoodsEvaluate> goodsEvaluateList = new ArrayList<>();
		goodsTobeEvaluateList.forEach(goodsTobeEvaluate -> {
			idList.add(goodsTobeEvaluate.getId());
			GoodsEvaluate goodsEvaluate = new GoodsEvaluate();
			goodsEvaluate.setStoreId(goodsTobeEvaluate.getStoreId());
			goodsEvaluate.setStoreName(goodsTobeEvaluate.getStoreName());
			goodsEvaluate.setGoodsId(goodsTobeEvaluate.getGoodsId());
			goodsEvaluate.setGoodsInfoId(goodsTobeEvaluate.getGoodsInfoId());
			goodsEvaluate.setGoodsInfoName(goodsTobeEvaluate.getGoodsInfoName());
			goodsEvaluate.setOrderNo(goodsTobeEvaluate.getOrderNo());
			goodsEvaluate.setBuyTime(goodsTobeEvaluate.getBuyTime());
			goodsEvaluate.setGoodsImg(goodsTobeEvaluate.getGoodsImg());
			goodsEvaluate.setSpecDetails(goodsTobeEvaluate.getGoodsSpecDetail());
			goodsEvaluate.setCustomerId(goodsTobeEvaluate.getCustomerId());
			goodsEvaluate.setCustomerName(goodsTobeEvaluate.getCustomerName());
			goodsEvaluate.setCustomerAccount(goodsTobeEvaluate.getCustomerAccount());
			goodsEvaluate.setEvaluateScore(5);
			goodsEvaluate.setEvaluateContent(finalComment);
			goodsEvaluate.setEvaluateTime(LocalDateTime.now());
			goodsEvaluate.setGoodNum(0);
			goodsEvaluate.setIsAnonymous(1);
			goodsEvaluate.setIsEdit(0);
			goodsEvaluate.setIsShow(0);
			goodsEvaluate.setIsUpload(0);
			goodsEvaluate.setDelFlag(DeleteFlag.NO.toValue());
			goodsEvaluate.setIsAnswer(0);
			goodsEvaluate.setIsSys(Constants.yes);
			goodsEvaluate.setCreateTime(LocalDateTime.now());
			goodsEvaluate.setCreatePerson("system");
			goodsEvaluateList.add(goodsEvaluate);
		});
		if(goodsEvaluateList.size()>0){
			//保存评论数据
			addGoodsEvaluateList(goodsEvaluateList);
			//删除已评论的待评论数据
			deleteByIdList(idList);
		}
	}

	/**
	 * @Author lvzhenwei
	 * @Description 保存评论数据
	 * @Date 9:55 2019/4/25
	 * @Param [goodsEvaluateAddListRequest]
	 * @return void
	 **/
	private void addGoodsEvaluateList(List<GoodsEvaluate> goodsEvaluateList){
		//保存评论数据
		goodsEvaluateRepository.saveAll(goodsEvaluateList);
		//增加评论数
		goodsEvaluateList.forEach(goodsEvaluate -> {
			goodsEvaluateNumMqService.updateGoodsEvaluateNum(goodsEvaluate);
		});
	}

}
