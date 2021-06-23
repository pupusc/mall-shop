package com.wanmi.sbc.marketing.marketingsuitssku.service;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.goods.bean.dto.GoodsSuitsDTO;
import com.wanmi.sbc.marketing.api.request.market.MarketingAddRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsSaveRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsCountBySkuIdRequest;
import com.wanmi.sbc.marketing.api.response.marketingsuitssku.MarketinSuitsCountBySkuIdResponse;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.wanmi.sbc.marketing.marketingsuitssku.repository.MarketingSuitsSkuRepository;
import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsSkuQueryRequest;
import com.wanmi.sbc.marketing.bean.vo.MarketingSuitsSkuVO;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>组合活动关联商品sku表业务逻辑</p>
 * @author zhk
 * @date 2020-04-02 10:51:12
 */
@Service("MarketingSuitsSkuService")
public class MarketingSuitsSkuService {
	@Autowired
	private MarketingSuitsSkuRepository marketingSuitsSkuRepository;

	/**
	 * 新增组合活动关联商品sku表
	 * @author zhk
	 */
	@Transactional
	public List<MarketingSuitsSku> add(List<MarketingSuitsSku> marketingSuitsSkus) {
		return marketingSuitsSkuRepository.saveAll(marketingSuitsSkus);
	}

	/**
	 * 修改组合活动关联商品sku表
	 * @author zhk
	 */
	@Transactional
	public MarketingSuitsSku modify(MarketingSuitsSku entity) {
		marketingSuitsSkuRepository.save(entity);
		return entity;
	}

	/**
	 * 单个删除组合活动关联商品sku表
	 * @author zhk
	 */
	@Transactional
	public void deleteByMarketingId(Long marketingId) {
		marketingSuitsSkuRepository.deleteByMarketingId(marketingId);
	}

//	/**
//	 * 批量删除组合活动关联商品sku表
//	 * @author zhk
//	 */
//	@Transactional
//	public void deleteByIdList(List<Long> ids) {
//		marketingSuitsSkuRepository.deleteByIdList(ids);
//	}

	/**
	 * 单个查询组合活动关联商品sku表
	 * @author zhk
	 */
	public MarketingSuitsSku getOne(Long id){
		return marketingSuitsSkuRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "组合活动关联商品sku表不存在"));
	}

	/**
	 * 分页查询组合活动关联商品sku表
	 * @author zhk
	 */
	public Page<MarketingSuitsSku> page(MarketingSuitsSkuQueryRequest queryReq){
		return marketingSuitsSkuRepository.findAll(
				MarketingSuitsSkuWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询组合活动关联商品sku表
	 * @author zhk
	 */
	public List<MarketingSuitsSku> list(MarketingSuitsSkuQueryRequest queryReq){
		return marketingSuitsSkuRepository.findAll(MarketingSuitsSkuWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 统计组合活动数据
	 */
	public MarketinSuitsCountBySkuIdResponse count(MarketingSuitsCountBySkuIdRequest marketingSuitsCountBySkuIdRequest){
		MarketingSuitsSkuQueryRequest queryReq = MarketingSuitsSkuQueryRequest.builder()
				.marketingType(marketingSuitsCountBySkuIdRequest.getMarketingType())
				.skuId(marketingSuitsCountBySkuIdRequest.getGoodsInfoId())
				.delFlag(DefaultFlag.NO)
				.build();
		List<MarketingSuitsSku> marketingSuitsSkuList = new ArrayList<>();
	    marketingSuitsSkuList = marketingSuitsSkuRepository.findAll(MarketingSuitsSkuWhereCriteriaBuilder.build(queryReq));
//		marketingSuitsSkuList = marketingSuitsSkuRepository.getByParam(queryReq.getSkuId(),queryReq.getDelFlag().toValue(),queryReq.getMarketingType().toValue());
	    List<MarketingSuitsSkuVO> marketingSuitsSkuVOList = new ArrayList<>();
		marketingSuitsSkuList.forEach(marketingSuitsSku->{
			MarketingSuitsSkuVO marketingSuitsSkuVO = KsBeanUtil.convert(marketingSuitsSku,MarketingSuitsSkuVO.class);
			marketingSuitsSkuVOList.add(marketingSuitsSkuVO);
		});
		return MarketinSuitsCountBySkuIdResponse.builder().marketingSuitsSkuVOList(marketingSuitsSkuVOList)
				.count((long)marketingSuitsSkuList.stream().map(MarketingSuitsSku :: getMarketingId ).distinct().collect(Collectors.toList()).size()).build();
	}

	/**
	 * 将实体包装成VO
	 * @author zhk
	 */
	public MarketingSuitsSkuVO wrapperVo(MarketingSuitsSku marketingSuitsSku) {
		if (marketingSuitsSku != null){
			MarketingSuitsSkuVO marketingSuitsSkuVO = KsBeanUtil.convert(marketingSuitsSku, MarketingSuitsSkuVO.class);
			return marketingSuitsSkuVO;
		}
		return null;
	}

	/**
	 * 根据活动id查询sku列表
	 * @param marketingIds
	 * @return
	 */
	public List<MarketingSuitsSku> getByMarketingIds(List<Long> marketingIds){
		return marketingSuitsSkuRepository.getByMarketingIds(marketingIds);
	}
}

