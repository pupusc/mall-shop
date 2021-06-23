package com.wanmi.sbc.marketing.grouponrecord.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.marketing.api.request.grouponrecord.GrouponRecordQueryRequest;
import com.wanmi.sbc.marketing.bean.vo.GrouponRecordVO;
import com.wanmi.sbc.marketing.grouponrecord.model.entity.IncrBuyNumEntity;
import com.wanmi.sbc.marketing.grouponrecord.model.root.GrouponRecord;
import com.wanmi.sbc.marketing.grouponrecord.repository.GrouponRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>拼团活动参团信息表业务逻辑</p>
 * @author groupon
 * @date 2019-05-17 16:17:44
 */
@Service("GrouponRecordService")
public class GrouponRecordService {
	@Autowired
	private GrouponRecordRepository grouponRecordRepository;
	
	/** 
	 * 新增拼团活动参团信息表
	 * @author groupon
	 */
	@Transactional
	public GrouponRecord add(GrouponRecord entity) {
		grouponRecordRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改拼团活动参团信息表
	 * @author groupon
	 */
	@Transactional
	public GrouponRecord modify(GrouponRecord entity) {
		grouponRecordRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除拼团活动参团信息表
	 * @author groupon
	 */
	@Transactional
	public void deleteById(String id) {
		grouponRecordRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除拼团活动参团信息表
	 * @author groupon
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		grouponRecordRepository.deleteAll(ids.stream().map(id -> {
			GrouponRecord entity = new GrouponRecord();
			entity.setGrouponRecordId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询拼团活动参团信息表
	 * @author groupon
	 */
	public GrouponRecord getById(String id){
		return grouponRecordRepository.findById(id).get();
	}

	/**
	 * 单个查询C端用户参团情况
	 * @author groupon
	 */
	public GrouponRecord getByCustomerIdAndGoodsInfoId(String grouponActivityId,String customerId,String goodsInfoId){
		return grouponRecordRepository.findTopByGrouponActivityIdAndCustomerIdAndGoodsInfoId(grouponActivityId,
				customerId,goodsInfoId);
	}


	/**
	 * 列表查询拼团活动参团信息表
	 * @author groupon
	 */
	public List<GrouponRecord> list(GrouponRecordQueryRequest queryReq){
		return grouponRecordRepository.findAll(GrouponRecordWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 根据活动ID、会员ID、SKU编号更新已购买数量（增加操作）
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	public void incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(IncrBuyNumEntity params){

		String grouponActivityId = params.getGrouponActivityId();
		String customerId = params.getCustomerId();
		String goodsInfoId = params.getGoodsInfoId();
		Integer buyNum = params.getBuyNum();

		GrouponRecord record = grouponRecordRepository.findTopByGrouponActivityIdAndCustomerIdAndGoodsInfoId(
				grouponActivityId, customerId, goodsInfoId);

		if (Objects.nonNull(record)) {
			// 如果记录存在，更新
			grouponRecordRepository.incrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(
					grouponActivityId,customerId,goodsInfoId,buyNum,LocalDateTime.now());
		} else {
			// 如果记录不存在，新增
			GrouponRecord newRecord = new GrouponRecord();
			LocalDateTime nowDate = LocalDateTime.now();
			newRecord.setGrouponActivityId(grouponActivityId);
			newRecord.setCustomerId(customerId);
			newRecord.setGoodsId(params.getGoodsId());
			newRecord.setGoodsInfoId(goodsInfoId);
			newRecord.setBuyNum(buyNum);
			newRecord.setLimitSellingNum(params.getLimitSellingNum());
			newRecord.setCreateTime(nowDate);
			newRecord.setUpdateTime(nowDate);
			grouponRecordRepository.save(newRecord);
		}

	}

	/**
	 * 根据活动ID、会员ID、SKU编号更新已购买数量（减少操作）
	 * @param grouponActivityId
	 * @param customerId
	 * @param goodsInfoId
	 * @param buyNum
	 * @return
	 */
	@Transactional(rollbackFor = Exception.class)
	@GlobalTransactional
	public int decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(String grouponActivityId, String customerId,
																		String goodsInfoId, Integer buyNum){
		return grouponRecordRepository.decrBuyNumByGrouponActivityIdAndCustomerIdAndGoodsInfoId(grouponActivityId,customerId,goodsInfoId,buyNum,LocalDateTime.now());
	}



	/**
	 * 将实体包装成VO
	 * @author groupon
	 */
	public GrouponRecordVO wrapperVo(GrouponRecord grouponRecord) {
		if (grouponRecord != null){
			GrouponRecordVO grouponRecordVO=new GrouponRecordVO();
			KsBeanUtil.copyPropertiesThird(grouponRecord,grouponRecordVO);
			return grouponRecordVO;
		}
		return null;
	}
}
