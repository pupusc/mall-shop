package com.wanmi.sbc.goods.storetobeevaluate.service;

import io.seata.spring.annotation.GlobalTransactional;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.storeevaluate.StoreEvaluateSaveProvider;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateAddListRequest;
import com.wanmi.sbc.customer.api.request.storeevaluate.StoreEvaluateAddRequest;
import com.wanmi.sbc.goods.api.request.storetobeevaluate.StoreTobeEvaluateQueryRequest;
import com.wanmi.sbc.goods.bean.vo.StoreTobeEvaluateVO;
import com.wanmi.sbc.goods.storetobeevaluate.model.root.StoreTobeEvaluate;
import com.wanmi.sbc.goods.storetobeevaluate.repository.StoreTobeEvaluateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>店铺服务待评价业务逻辑</p>
 * @author lzw
 * @date 2019-03-20 17:01:46
 */
@Service("StoreTobeEvaluateService")
public class StoreTobeEvaluateService {

	@Autowired
	private StoreTobeEvaluateRepository storeTobeEvaluateRepository;

	@Autowired
	private StoreEvaluateSaveProvider storeEvaluateSaveProvider;
	
	/** 
	 * 新增店铺服务待评价
	 * @author lzw
	 */
	@GlobalTransactional
	@Transactional
	public StoreTobeEvaluate add(StoreTobeEvaluate entity) {
		storeTobeEvaluateRepository.save(entity);
		return entity;
	}
	
	/** 
	 * 修改店铺服务待评价
	 * @author lzw
	 */
	@Transactional
	public StoreTobeEvaluate modify(StoreTobeEvaluate entity) {
		storeTobeEvaluateRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除店铺服务待评价
	 * @author lzw
	 */
	@Transactional
	public void deleteById(String id) {
		storeTobeEvaluateRepository.deleteById(id);
	}
	
	/** 
	 * 批量删除店铺服务待评价
	 * @author lzw
	 */
	@Transactional
	public void deleteByIdList(List<String> ids) {
		storeTobeEvaluateRepository.deleteAll(ids.stream().map(id -> {
			StoreTobeEvaluate entity = new StoreTobeEvaluate();
			entity.setId(id);
			return entity;
		}).collect(Collectors.toList()));
	}
	
	/** 
	 * 单个查询店铺服务待评价
	 * @author lzw
	 */
	public StoreTobeEvaluate getById(String id){
		return storeTobeEvaluateRepository.findById(id).orElse(null);
	}
	
	/** 
	 * 分页查询店铺服务待评价
	 * @author lzw
	 */
	public Page<StoreTobeEvaluate> page(StoreTobeEvaluateQueryRequest queryReq){
		return storeTobeEvaluateRepository.findAll(
				StoreTobeEvaluateWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}
	
	/** 
	 * 列表查询店铺服务待评价
	 * @author lzw
	 */
	public List<StoreTobeEvaluate> list(StoreTobeEvaluateQueryRequest queryReq){
		return storeTobeEvaluateRepository.findAll(StoreTobeEvaluateWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 查询店铺评价服务数量
	 * @return
	 */
	public Long getStoreTobeEvaluateNum(StoreTobeEvaluateQueryRequest queryReq){
		return storeTobeEvaluateRepository.count(StoreTobeEvaluateWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author lzw
	 */
	public StoreTobeEvaluateVO wrapperVo(StoreTobeEvaluate storeTobeEvaluate) {
		if (storeTobeEvaluate != null){
			StoreTobeEvaluateVO storeTobeEvaluateVO=new StoreTobeEvaluateVO();
			KsBeanUtil.copyPropertiesThird(storeTobeEvaluate,storeTobeEvaluateVO);
			return storeTobeEvaluateVO;
		}
		return null;
	}

	/**
	 * @Description: 订单ID和店铺ID删除
	 * @param queryReq {@link StoreTobeEvaluateQueryRequest}
	 * @Author: Bob
	 * @Date: 2019-04-12 16:34
	 */
	@GlobalTransactional
	@Transactional(rollbackFor = Exception.class)
	public int delByOrderIDAndStoreID(StoreTobeEvaluateQueryRequest queryReq){

		return storeTobeEvaluateRepository.deleteByOrderNoAndStoreId(queryReq.getOrderNo(), queryReq.getStoreId());
	}

	/**
	 * @Description: 动态条件查询
	 * @param queryReq {@link StoreTobeEvaluateQueryRequest}
	 * @Author: Bob
	 * @Date: 2019-04-12 17:22
	 */
	public StoreTobeEvaluateVO query(StoreTobeEvaluateQueryRequest queryReq){
		StoreTobeEvaluate storeTobeEvaluate =
				storeTobeEvaluateRepository.findOne(StoreTobeEvaluateWhereCriteriaBuilder.build(queryReq)).orElse(null);
		return wrapperVo(storeTobeEvaluate);
	}

	/**
	 * @Author lvzhenwei
	 * @Description 待评价店铺服务自动评价
	 * @Date 10:37 2019/4/25
	 * @Param []
	 * @return void
	 **/
	@GlobalTransactional
	@Transactional
	public void autoStoreEvaluate(){
		StoreTobeEvaluateQueryRequest queryReq = new StoreTobeEvaluateQueryRequest();
		queryReq.setAutoStoreEvaluateDate(LocalDate.now());
		List<StoreTobeEvaluate> storeTobeEvaluateList =  storeTobeEvaluateRepository
				.findAll(StoreTobeEvaluateWhereCriteriaBuilder.build(queryReq));
		List<StoreEvaluateAddRequest> storeEvaluateAddRequestList = new ArrayList<>();
		List<String> idList = new ArrayList<>();
		storeTobeEvaluateList.forEach(storeTobeEvaluate -> {
			idList.add(storeTobeEvaluate.getId());
			StoreEvaluateAddRequest storeEvaluateAddRequest = new StoreEvaluateAddRequest();
			storeEvaluateAddRequest.setStoreId(storeTobeEvaluate.getStoreId());
			storeEvaluateAddRequest.setStoreName(storeTobeEvaluate.getStoreName());
			storeEvaluateAddRequest.setOrderNo(storeTobeEvaluate.getOrderNo());
			storeEvaluateAddRequest.setCustomerId(storeTobeEvaluate.getCustomerId());
			storeEvaluateAddRequest.setCustomerName(storeTobeEvaluate.getCustomerName());
			storeEvaluateAddRequest.setCustomerAccount(storeTobeEvaluate.getCustomerAccount());
			storeEvaluateAddRequest.setGoodsScore(5);
			storeEvaluateAddRequest.setServerScore(5);
			storeEvaluateAddRequest.setLogisticsScore(5);
			storeEvaluateAddRequest.setCompositeScore(new BigDecimal(5));
			storeEvaluateAddRequest.setCreateTime(LocalDateTime.now());
			storeEvaluateAddRequest.setCreatePerson("system");
			storeEvaluateAddRequestList.add(storeEvaluateAddRequest);
		});
		StoreEvaluateAddListRequest storeEvaluateAddListRequest = new StoreEvaluateAddListRequest();
		storeEvaluateAddListRequest.setStoreEvaluateAddRequestList(storeEvaluateAddRequestList);
		if(storeEvaluateAddRequestList.size()>0){
			//将评论数据进行保存
			storeEvaluateSaveProvider.addList(storeEvaluateAddListRequest);
			//将以评论的待评论数据删除
			deleteByIdList(idList);
		}
	}
}
