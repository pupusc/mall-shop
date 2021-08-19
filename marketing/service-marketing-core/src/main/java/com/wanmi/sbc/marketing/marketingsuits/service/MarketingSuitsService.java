package com.wanmi.sbc.marketing.marketingsuits.service;

import com.wanmi.sbc.common.enums.BoolFlag;
import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.KsBeanUtil;
import com.wanmi.sbc.customer.api.provider.customer.CustomerQueryProvider;
import com.wanmi.sbc.customer.api.provider.level.CustomerLevelQueryProvider;
import com.wanmi.sbc.customer.api.provider.paidcardcustomerrel.PaidCardCustomerRelQueryProvider;
import com.wanmi.sbc.customer.api.provider.store.StoreQueryProvider;
import com.wanmi.sbc.customer.api.request.customer.CustomerGetByIdRequest;
import com.wanmi.sbc.customer.api.request.level.CustomerLevelByCustomerIdAndStoreIdRequest;
import com.wanmi.sbc.customer.api.request.paidcardcustomerrel.PaidCardCustomerRelListRequest;
import com.wanmi.sbc.customer.api.request.store.StoreByIdRequest;
import com.wanmi.sbc.customer.api.response.customer.CustomerGetByIdResponse;
import com.wanmi.sbc.customer.api.response.level.CustomerLevelByCustomerIdAndStoreIdResponse;
import com.wanmi.sbc.customer.bean.enums.EnterpriseCheckState;
import com.wanmi.sbc.customer.bean.vo.PaidCardCustomerRelVO;
import com.wanmi.sbc.customer.bean.vo.StoreVO;
import com.wanmi.sbc.goods.api.provider.brand.GoodsBrandQueryProvider;
import com.wanmi.sbc.goods.api.provider.cate.GoodsCateQueryProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.brand.GoodsBrandByIdsRequest;
import com.wanmi.sbc.goods.api.request.cate.GoodsCateListByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoViewByIdsRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoByIdResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsSuitsDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.bean.vo.GoodsBrandVO;
import com.wanmi.sbc.goods.bean.vo.GoodsCateVO;
import com.wanmi.sbc.goods.bean.vo.GoodsInfoVO;
import com.wanmi.sbc.marketing.api.request.market.MarketingGetByIdRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsQueryRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuits.MarketingSuitsSaveRequest;
import com.wanmi.sbc.marketing.api.request.marketingsuitssku.MarketingSuitsSkuQueryRequest;
import com.wanmi.sbc.marketing.api.response.market.MarketingMoreGoodsInfoResponse;
import com.wanmi.sbc.marketing.api.response.marketingsuits.MarketingSuitsByMarketingIdResponse;
import com.wanmi.sbc.marketing.bean.constant.MarketingErrorCode;
import com.wanmi.sbc.marketing.bean.constant.MarketingSuitsErrorCode;
import com.wanmi.sbc.marketing.bean.enums.MarketingJoinLevel;
import com.wanmi.sbc.marketing.bean.enums.MarketingScopeType;
import com.wanmi.sbc.marketing.bean.enums.MarketingSubType;
import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import com.wanmi.sbc.marketing.bean.vo.*;
import com.wanmi.sbc.marketing.common.model.root.Marketing;
import com.wanmi.sbc.marketing.common.request.MarketingSaveRequest;
import com.wanmi.sbc.marketing.common.service.MarketingService;
import com.wanmi.sbc.marketing.marketingsuits.model.root.MarketingSuits;
import com.wanmi.sbc.marketing.marketingsuits.repository.MarketingSuitsRepository;
import com.wanmi.sbc.marketing.marketingsuitssku.model.root.MarketingSuitsSku;
import com.wanmi.sbc.marketing.marketingsuitssku.service.MarketingSuitsSkuService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>组合商品主表业务逻辑</p>
 * @author zhk
 * @date 2020-04-02 10:39:15
 */
@Service("MarketingSuitsService")
public class MarketingSuitsService {
	@Autowired
	private MarketingSuitsRepository marketingSuitsRepository;



	@Autowired
	private MarketingSuitsSkuService marketingSuitsSkuService;

	@Autowired
	private GoodsInfoQueryProvider goodsInfoQueryProvider;

	@Autowired
	private MarketingService marketingService;

	@Autowired
	private GoodsCateQueryProvider goodsCateQueryProvider;

	@Autowired
	private GoodsBrandQueryProvider goodsBrandQueryProvider;

	@Autowired
	private CustomerLevelQueryProvider customerLevelQueryProvider;

	@Autowired
	private StoreQueryProvider storeQueryProvider;

	@Autowired
	private CustomerQueryProvider customerQueryProvider;

	@Autowired
	private PaidCardCustomerRelQueryProvider paidCardCustomerRelQueryProvider;


	/**
	 * 新增组合商品主表
	 * @author zhk
	 */
	@Transactional
	public void add(MarketingSuitsSaveRequest marketingSuitsSaveRequest) {

	    //校验
		List<GoodsSuitsDTO> goodsSuitsDTOList = this.check(marketingSuitsSaveRequest);

		List<String> skuIds = goodsSuitsDTOList.stream().map(GoodsSuitsDTO::getGoodsInfoId).collect(Collectors.toList());

		//保存活动数据
		MarketingSaveRequest marketingSaveRequest =  MarketingSaveRequest.builder()
				.marketingName(marketingSuitsSaveRequest.getSuitsName())
				.beginTime(marketingSuitsSaveRequest.getBeginTime())
				.endTime(marketingSuitsSaveRequest.getEndTime())
				.storeId(marketingSuitsSaveRequest.getBaseStoreId())
				.marketingType(MarketingType.SUITS)
				.subType(MarketingSubType.SUITS_GOODS)
				.isBoss(BoolFlag.NO)
				.joinLevel(marketingSuitsSaveRequest.getJoinLevel())
				.scopeType(MarketingScopeType.SCOPE_TYPE_CUSTOM)
				.skuIds(skuIds)
				.build();
		Marketing marketing = marketingService.addMarketing(marketingSaveRequest);

		//套餐价格
		BigDecimal suitPrice = goodsSuitsDTOList.stream().map(goodsSuitsDTO ->
				goodsSuitsDTO.getDiscountPrice().multiply(BigDecimal.valueOf(goodsSuitsDTO.getGoodsSkuNum())))
				.reduce(BigDecimal::add).get();
		//组合商品主表数据
		MarketingSuits entity = new MarketingSuits();
		entity.setMarketingId(marketing.getMarketingId());
		entity.setMainImage(marketingSuitsSaveRequest.getSuitsPictureUrl());
		entity.setSuitsPrice(suitPrice);
		MarketingSuits marketingSuits = marketingSuitsRepository.save(entity);

		//组合活动商品关联数据
		List<MarketingSuitsSku> suitSkus = goodsSuitsDTOList.stream().map(goodsSuitsDTO -> {
			MarketingSuitsSku suitsSku = new MarketingSuitsSku();
			suitsSku.setMarketingId(marketing.getMarketingId());
			suitsSku.setSkuId(goodsSuitsDTO.getGoodsInfoId());
			suitsSku.setNum(goodsSuitsDTO.getGoodsSkuNum());
			suitsSku.setDiscountPrice(goodsSuitsDTO.getDiscountPrice());
			suitsSku.setSuitsId(marketingSuits.getId());
			return suitsSku;
		}).collect(Collectors.toList());
		marketingSuitsSkuService.add(suitSkus);
	}

	/**
	 * 修改组合商品主表
	 * @author zhk
	 */
	@Transactional
	public void modify(MarketingSuitsSaveRequest marketingSuitsSaveRequest) {

	    //校验
		List<GoodsSuitsDTO> goodsSuitsDTOList = this.check(marketingSuitsSaveRequest);

		List<String> skuIds = goodsSuitsDTOList.stream().map(GoodsSuitsDTO::getGoodsInfoId).collect(Collectors.toList());

		//保存活动数据
		MarketingSaveRequest marketingSaveRequest =  MarketingSaveRequest.builder()
				.marketingId(marketingSuitsSaveRequest.getMarketingId())
				.marketingName(marketingSuitsSaveRequest.getSuitsName())
				.beginTime(marketingSuitsSaveRequest.getBeginTime())
				.endTime(marketingSuitsSaveRequest.getEndTime())
				.marketingType(MarketingType.SUITS)
				.subType(MarketingSubType.SUITS_GOODS)
				.joinLevel(marketingSuitsSaveRequest.getJoinLevel())
				.storeId(marketingSuitsSaveRequest.getBaseStoreId())
                .scopeType(MarketingScopeType.SCOPE_TYPE_CUSTOM)
                .skuIds(skuIds)
				.build();
		Marketing marketing = marketingService.modifyMarketing(marketingSaveRequest);

		//套餐价格
		BigDecimal suitPrice = goodsSuitsDTOList.stream().map(goodsSuitsDTO ->
				goodsSuitsDTO.getDiscountPrice().multiply(BigDecimal.valueOf(goodsSuitsDTO.getGoodsSkuNum())))
				.reduce(BigDecimal::add).get();
		//组合商品主表数据
		MarketingSuits entity = new MarketingSuits();
		entity.setMarketingId(marketing.getMarketingId());
		entity.setMainImage(marketingSuitsSaveRequest.getSuitsPictureUrl());
		entity.setSuitsPrice(suitPrice);
		//删除原有数据
        deleteByMarketingId(marketing.getMarketingId());
        MarketingSuits marketingSuits = marketingSuitsRepository.save(entity);

        //组合活动商品关联数据
		marketingSuitsSkuService.deleteByMarketingId(marketing.getMarketingId());
		List<MarketingSuitsSku> suitSkus = goodsSuitsDTOList.stream().map(goodsSuitsDTO -> {
			MarketingSuitsSku suitsSku = new MarketingSuitsSku();
			suitsSku.setMarketingId(marketing.getMarketingId());
			suitsSku.setSkuId(goodsSuitsDTO.getGoodsInfoId());
			suitsSku.setNum(goodsSuitsDTO.getGoodsSkuNum());
			suitsSku.setDiscountPrice(goodsSuitsDTO.getDiscountPrice());
			suitsSku.setSuitsId(marketingSuits.getId());
			return suitsSku;
		}).collect(Collectors.toList());
		marketingSuitsSkuService.add(suitSkus);
	}

	/**
	 * 校验
	 * @param marketingSuitsSaveRequest
	 * @return
	 */
	private List<GoodsSuitsDTO> check(MarketingSuitsSaveRequest marketingSuitsSaveRequest){

		List<GoodsSuitsDTO> goodsSuitsDTOList = marketingSuitsSaveRequest.getGoodsSuitsDTOList();


		//1、校验商品数量
		if(goodsSuitsDTOList.size() < 2){
			throw new SbcRuntimeException(MarketingSuitsErrorCode.MARKETING_SUIT_GOODS_NUM_MIN_2);
		}else if(goodsSuitsDTOList.size() > 20){
			throw new SbcRuntimeException(MarketingSuitsErrorCode.MARKETING_SUIT_GOODS_NUM_MAX_20);
		}

		//2、活动商品统一数量
		if(DefaultFlag.YES.equals(marketingSuitsSaveRequest.getGoodsSkuNumIdentify())){
			Long goodsSkuNum = goodsSuitsDTOList.get(0).getGoodsSkuNum();
			for (GoodsSuitsDTO goodsSuitsDTO: goodsSuitsDTOList) {
				goodsSuitsDTO.setGoodsSkuNum(goodsSkuNum);
			}
		}

		Integer existsSkuNum = NumberUtils.INTEGER_ZERO;
		HashMap<String,Long> goodsSuitsMap =new HashMap<>();
		for (GoodsSuitsDTO goodsSuitsDTO : goodsSuitsDTOList) {
			goodsSuitsMap.put(goodsSuitsDTO.getGoodsInfoId(),goodsSuitsDTO.getGoodsSkuNum());
			GoodsInfoByIdRequest goodsInfoByIdRequest = GoodsInfoByIdRequest.builder().goodsInfoId(goodsSuitsDTO.getGoodsInfoId()).build();
			GoodsInfoByIdResponse goodsInfo = goodsInfoQueryProvider.getById(goodsInfoByIdRequest).getContext();
			if(Objects.nonNull(goodsInfo) && AddedFlag.YES.toValue() == (goodsInfo.getAddedFlag())){
				//3、校验商品折扣价
				if(goodsSuitsDTO.getDiscountPrice().compareTo(goodsInfo.getMarketPrice()) > 0){
					throw new SbcRuntimeException(MarketingSuitsErrorCode.MARKETING_SUIT_GOODS_DISCOUNT_PRICE);
				}
			}else{
				throw new SbcRuntimeException(MarketingSuitsErrorCode.MARKETING_SUIT_GOODS_EXIST, new Object[]{goodsInfo.getGoodsInfoName()});
			}

			//4、校验商品是否已经存在于10个活动中
			List<String> marketingSuitsExists = marketingService.getMarketingSuitsExists(goodsSuitsDTO.getGoodsInfoId(), marketingSuitsSaveRequest.getBeginTime(),
					marketingSuitsSaveRequest.getEndTime(), marketingSuitsSaveRequest.getBaseStoreId(), marketingSuitsSaveRequest.getMarketingId());
			if(CollectionUtils.isNotEmpty(marketingSuitsExists) && marketingSuitsExists.size() >= 10){
				existsSkuNum ++;
			}
		}
		if(existsSkuNum > NumberUtils.INTEGER_ZERO){
			throw new SbcRuntimeException(MarketingErrorCode.MARKETING_GOODS_TIME_CONFLICT, new Object[]{existsSkuNum.intValue()});
		}

		//校验组合购活动是否重复
		this.checkRepeatMaketing(marketingSuitsSaveRequest,goodsSuitsMap);

		return goodsSuitsDTOList;
	}




	/**
	 * @Author yangzhen
	 * @Description //校验组合购活动是否重复，校验标准1、活动下的商品和已存在的组合购活动一致，每个商品数量一致，活动开始时间和结束时间产生交叉
	 * @Date 11:53 2020/8/7
	 * @Param [marketingSuitsSaveRequest, goodsSuitsMap]
	 * @return void
	 **/
	private void checkRepeatMaketing (MarketingSuitsSaveRequest marketingSuitsSaveRequest,HashMap<String,Long> goodsSuitsMap){
		List<GoodsSuitsDTO> goodsSuitsDTOList = marketingSuitsSaveRequest.getGoodsSuitsDTOList();

		//根据sku_id 查到 已有的markingid
		List<Marketing> marketingList = marketingService.getMarketingNotEndBySuitsSkuId(goodsSuitsDTOList.get(0).getGoodsInfoId());
		if(Objects.nonNull(marketingSuitsSaveRequest.getMarketingId())){
			marketingList = marketingList.stream().filter(v -> !v.getMarketingId().equals(marketingSuitsSaveRequest.getMarketingId())).collect(Collectors.toList());
		}
		if(CollectionUtils.isNotEmpty(marketingList)){
			//把每个markingid 下的商品都查询出来
			marketingList.forEach((marketing)->{
				//组合购商品信息列表
				List<MarketingSuitsSku> marketingSuitsSkuList =
						marketingSuitsSkuService.getByMarketingIds(Arrays.asList(marketing.getMarketingId()));
				//1、先判断组合购包含商品数是否一致
				if(marketingSuitsSkuList.size() == goodsSuitsDTOList.size()){
					//判断新增的skuid和数量 和库里的是不是一致
					List<MarketingSuitsSku> marketingSuitsSkus = marketingSuitsSkuList.stream().filter(
							item -> goodsSuitsMap.get(item.getSkuId()) == item.getNum())
							.collect(Collectors.toList());
					//过滤后结果如果和新增保持一致
					if(CollectionUtils.isNotEmpty(marketingSuitsSkus)){
						if(marketingSuitsSkus.size() == marketingSuitsSkuList.size()){
							//根据marketing 查询活动详情
							marketingSuitsSkuList.forEach(marketingSuitsSku -> {
								Marketing marketingPo = marketingService.queryById(marketingSuitsSku.getMarketingId());
								//开始时间在另一个活动时间范围内[begin, end)
								if((marketingSuitsSaveRequest.getBeginTime().isAfter(marketingPo.getBeginTime())
										|| marketingSuitsSaveRequest.getBeginTime().equals(marketingPo.getBeginTime()))
										&& marketingSuitsSaveRequest.getBeginTime().isBefore(marketingPo.getEndTime())){
									throw new SbcRuntimeException(MarketingSuitsErrorCode.MARKETING_IS_EXIST);
								}
								//结束时间在另一个活动时间范围内（begin, end]
								if(marketingSuitsSaveRequest.getEndTime().isAfter(marketingPo.getBeginTime())
										&& (marketingSuitsSaveRequest.getEndTime().isBefore(marketingPo.getEndTime())
										|| marketingSuitsSaveRequest.getEndTime().equals(marketingPo.getEndTime()))){
									throw new SbcRuntimeException(MarketingSuitsErrorCode.MARKETING_IS_EXIST);
								}
							});
						}
					}
				}

			});
		}
	}



	/**
	 * 单个删除组合商品主表
	 * @author zhk
	 */
	@Transactional
	public void deleteByMarketingId(Long marketingId) {
		marketingSuitsRepository.deleteByMarketingId(marketingId);
	}

//	/**
//	 * 批量删除组合商品主表
//	 * @author zhk
//	 */
//	@Transactional
//	public void deleteByIdList(List<Long> ids) {
//		marketingSuitsRepository.deleteByIdList(ids);
//	}

	public MarketingSuitsByMarketingIdResponse getByMarketingId( Long marketingId) {
		MarketingGetByIdRequest marketingGetByIdRequest = new MarketingGetByIdRequest();
		marketingGetByIdRequest.setMarketingId(marketingId);
		// 营销活动信息
		MarketingVO marketing = KsBeanUtil.convert(marketingService.queryById(marketingId), MarketingVO.class);
		if (!Objects.equals(MarketingSubType.SUITS_GOODS, marketing.getSubType())) {
			throw new SbcRuntimeException(MarketingErrorCode.NOT_EXIST);
		}
		// 套装活动信息
		MarketingSuitsQueryRequest listReq = new MarketingSuitsQueryRequest();
		listReq.setMarketingId(marketingId);
		MarketingSuitsVO marketingSuits = KsBeanUtil.convert(this.list(listReq).get(0), MarketingSuitsVO.class);
		// 套装活动商品
		MarketingSuitsSkuQueryRequest queryReq = new MarketingSuitsSkuQueryRequest();
		queryReq.setMarketingId(marketingId);
		List<MarketingSuitsSku> suitsSkuList = marketingSuitsSkuService.list(queryReq);
		List<MarketingSuitsSkuVO> marketingSuitsSkuList = suitsSkuList.stream().map(entity -> marketingSuitsSkuService.wrapperVo(entity)).collect(Collectors.toList());

		return new MarketingSuitsByMarketingIdResponse(marketing, marketingSuits, marketingSuitsSkuList);
	}

	/**
	 * 商家端-查询组合购详情
	 * @param marketingId
	 * @return
	 */
	public MarketingSuitsByMarketingIdResponse getByMarketingIdForSupplier(Long marketingId){
		MarketingSuitsByMarketingIdResponse response = getByMarketingId(marketingId);

		List<String> skuIds = response.getMarketingSuitsSkuVOList().stream()
				.map(MarketingSuitsSkuVO::getSkuId).collect(Collectors.toList());

		List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(GoodsInfoViewByIdsRequest.builder()
				.goodsInfoIds(skuIds).isHavSpecText(1).build()).getContext().getGoodsInfos();

		if (CollectionUtils.isNotEmpty(goodsInfos)) {
			//查询分类信息
			List<Long> cateIds = goodsInfos.stream().map(GoodsInfoVO::getCateId).distinct().collect(Collectors.toList());
			GoodsCateListByConditionRequest cateRequest = new GoodsCateListByConditionRequest();
			cateRequest.setCateIds(cateIds);
			List<GoodsCateVO> goodsCateVOList = goodsCateQueryProvider.listByCondition(cateRequest).getContext().getGoodsCateVOList();
			//查询品牌信息
			List<Long> brandIds = goodsInfos.stream().map(GoodsInfoVO::getBrandId).distinct().collect(Collectors.toList());
			List<GoodsBrandVO> goodsBrandVOList = goodsBrandQueryProvider.listByIds(GoodsBrandByIdsRequest.builder()
					.brandIds(brandIds).build()).getContext().getGoodsBrandVOList();

			//组装商品数据
			response.getMarketingSuitsSkuVOList().stream().forEach(marketingSuitsSkuVO -> {
				goodsInfos.stream().filter(goodsInfoVO ->
						goodsInfoVO.getGoodsInfoId().equals(marketingSuitsSkuVO.getSkuId())).findFirst().ifPresent(goodsInfoVO -> {
					marketingSuitsSkuVO.setGoodsInfoNo(goodsInfoVO.getGoodsInfoNo());
					marketingSuitsSkuVO.setGoodsInfoName(goodsInfoVO.getGoodsInfoName());
					marketingSuitsSkuVO.setSpecText(goodsInfoVO.getSpecText());
					marketingSuitsSkuVO.setMarketPrice(goodsInfoVO.getMarketPrice());
					marketingSuitsSkuVO.setBuyPoint(goodsInfoVO.getBuyPoint());

					goodsCateVOList.stream().filter(goodsCateVO ->
							goodsCateVO.getCateId().equals(goodsInfoVO.getCateId())).findFirst().ifPresent(goodsCateVO -> {
						marketingSuitsSkuVO.setCateName(goodsCateVO.getCateName());
					});

					goodsBrandVOList.stream().filter(goodsBrandVO ->
							goodsBrandVO.getBrandId().equals(goodsInfoVO.getBrandId())).findFirst().ifPresent(goodsBrandVO -> {
						marketingSuitsSkuVO.setBrandName(goodsBrandVO.getBrandName());
					});
				});
			});
		}

		return response;
	}

	/**
	 * 根据skuId获取所有组合购活动信息
	 * @param skuId
	 * @return
	 */
	public List<MarketingMoreGoodsInfoResponse> getMarketingSuitsBySkuId(String skuId, Long storeId, String customerId){

		//活动信息列表
		List<Marketing> marketingList = marketingService.getMarketingBySuitsSkuId(skuId);
		//商品没有活动storeId
		if(CollectionUtils.isEmpty(marketingList)){
			return new ArrayList<>();
		}

		//用户登录情况下，过滤用户是可见的组合商品；用户未登录，只展示针对全平台用户的组合购活动
		if (StringUtils.isNotBlank(customerId)){
			CustomerLevelByCustomerIdAndStoreIdResponse levelResponse = customerLevelQueryProvider
					.getCustomerLevelByCustomerIdAndStoreId(CustomerLevelByCustomerIdAndStoreIdRequest.builder().customerId(customerId).storeId(storeId).build())
					.getContext();
			marketingList = marketingList.stream().filter(marketing -> {

				if(marketing.getJoinLevel().equals("-4")) {
					//企业会员
					CustomerGetByIdRequest customerGetByIdRequest = new CustomerGetByIdRequest();
					customerGetByIdRequest.setCustomerId(customerId);
					CustomerGetByIdResponse customerResponse = customerQueryProvider.getCustomerById(customerGetByIdRequest).getContext();
					if(EnterpriseCheckState.CHECKED.equals(customerResponse.getEnterpriseCheckState())) {
						return true;
					}
				} else if(marketing.getJoinLevel().equals("-5")){
					//付费会员
					List<PaidCardCustomerRelVO> relVOList = paidCardCustomerRelQueryProvider
							.list(PaidCardCustomerRelListRequest.builder().delFlag(DeleteFlag.NO).endTimeBegin(LocalDateTime.now()).customerId(customerId).build())
							.getContext().getPaidCardCustomerRelVOList();
					if(CollectionUtils.isNotEmpty(relVOList)) {
						return true;
					}
				}else if("-1".equals(marketing.getJoinLevel())){
					//全平台
					return true;
				}else if(Objects.nonNull(levelResponse) ){
					//不限等级
					if("0".equals(marketing.getJoinLevel())){
						return true;
					}else if(Arrays.asList(marketing.getJoinLevel().split(",")).contains(String.valueOf(levelResponse.getLevelId()))){
						return true;
					}
				}
				return false;
			}).collect(Collectors.toList());
		}else{
			//筛选第三方商家全平台及自营商家全等级的活动
			StoreVO storeVO = storeQueryProvider.getById(StoreByIdRequest.builder().storeId(storeId).build()).getContext().getStoreVO();
			marketingList = marketingList.stream()
					.filter(v -> "-1".equals(v.getJoinLevel()) || (BoolFlag.NO == storeVO.getCompanyType() && "0".equals(v.getJoinLevel())))
					.collect(Collectors.toList());
		}
		if(CollectionUtils.isEmpty(marketingList)){
			return new ArrayList<>();
		}
		List<Long> marketingIds = marketingList.stream().map(Marketing::getMarketingId).collect(Collectors.toList());

		//组合购信息列表
		List<MarketingSuits> marketingSuitsList = marketingSuitsRepository.getByMarketingIds(marketingIds);

		//组合购商品信息列表
		List<MarketingSuitsSku> marketingSuitsSkuList = marketingSuitsSkuService.getByMarketingIds(marketingIds);

		//组装数据
		List<MarketingMoreGoodsInfoResponse> responseList = new ArrayList<>();
		for (Marketing marketing : marketingList) {
			MarketingMoreGoodsInfoResponse response = new MarketingMoreGoodsInfoResponse();
			//1.组合购信息
			marketingSuitsList.stream().filter(marketingSuits -> marketingSuits.getMarketingId().equals(marketing.getMarketingId()))
					.findFirst().ifPresent(marketingSuits -> {
				MarketingSuitsGoodsInfoDetailVO goodsInfoDetailVO = MarketingSuitsGoodsInfoDetailVO.builder()
						.marketingId(marketing.getMarketingId())
						.marketingName(marketing.getMarketingName())
						.mainImage(marketingSuits.getMainImage())
						.suitsPrice(marketingSuits.getSuitsPrice())
						.build();
				response.setMarketingSuitsGoodsInfoDetailVO(goodsInfoDetailVO);
			});

			//2.组合购商品信息
			List<MarketingSuitsSku> skuList = marketingSuitsSkuList.stream().filter(marketingSuitsSku ->
					marketingSuitsSku.getMarketingId().equals(marketing.getMarketingId()))
					.collect(Collectors.toList());

			List<String> skuIds = skuList.stream().map(MarketingSuitsSku::getSkuId).collect(Collectors.toList());
			List<GoodsInfoVO> goodsInfos = goodsInfoQueryProvider.listViewByIds(GoodsInfoViewByIdsRequest.builder()
					.goodsInfoIds(skuIds)
					.deleteFlag(DeleteFlag.NO)
					.isHavSpecText(1)
					.storeId(storeId)
					.build()).getContext().getGoodsInfos();
			//过滤下架状态、没有库存、代销不可售的商品
			if(CollectionUtils.isNotEmpty(goodsInfos)){
				goodsInfos = goodsInfos.stream().filter(goodsInfoVO -> {
					if(Objects.isNull(goodsInfoVO.getProviderId())){
						if(goodsInfoVO.getAddedFlag() == AddedFlag.YES.toValue() && goodsInfoVO.getStock() > 0)
							return Boolean.TRUE;
					} else {
						if(Constants.yes.equals(goodsInfoVO.getVendibility()) && goodsInfoVO.getAddedFlag() == AddedFlag.YES.toValue() && goodsInfoVO.getStock() > 0)
							return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}).collect(Collectors.toList());
			}
			//商品不存在或不是上架状态、没有库存
			if(CollectionUtils.isEmpty(goodsInfos) || goodsInfos.size() != skuList.size()){
				continue;
			}
			//组合商品原价
			BigDecimal marketPrice = BigDecimal.ZERO;
			List<SuitsRelationGoodsInfoVO> relationGoodsInfoVOS = new ArrayList<>();
			//组装sku数据
			for (MarketingSuitsSku marketingSuitsSku: skuList) {
				Optional<GoodsInfoVO> optional = goodsInfos.stream().filter(goodsInfoVO ->
						goodsInfoVO.getGoodsInfoId().equals(marketingSuitsSku.getSkuId())).findFirst();
				if (optional.isPresent()) {
					GoodsInfoVO goodsInfoVO = optional.get();
					SuitsRelationGoodsInfoVO suitsRelationGoodsInfoVO = SuitsRelationGoodsInfoVO.builder()
							.goodInfoId(goodsInfoVO.getGoodsInfoId())
							.goodsInfoName(goodsInfoVO.getGoodsInfoName())
							.mainImage(goodsInfoVO.getGoodsInfoImg())
							.marketPrice(goodsInfoVO.getMarketPrice())
							.specDetail(goodsInfoVO.getSpecText())
							.goodsInfoNum(marketingSuitsSku.getNum())
							.build();
					relationGoodsInfoVOS.add(suitsRelationGoodsInfoVO);
					marketPrice = marketPrice.add(goodsInfoVO.getMarketPrice().multiply(BigDecimal.valueOf(marketingSuitsSku.getNum())));
				}

			}

			BigDecimal suitsNoNeedPrice = marketPrice.subtract(response.getMarketingSuitsGoodsInfoDetailVO().getSuitsPrice());
			response.getMarketingSuitsGoodsInfoDetailVO().setSuitsNoNeedPrice(suitsNoNeedPrice);
			response.setSuitsRelationGoodsInfoVOList(relationGoodsInfoVOS);
			responseList.add(response);
		}

		return responseList;
	}

	/**
	 * 单个查询组合商品主表
	 * @author zhk
	 */
	public MarketingSuits getOne(Long id){
		return marketingSuitsRepository.findById(id)
		.orElseThrow(() -> new SbcRuntimeException(CommonErrorCode.SPECIFIED, "组合商品主表不存在"));
	}

	/**
	 * 分页查询组合商品主表
	 * @author zhk
	 */
	public Page<MarketingSuits> page(MarketingSuitsQueryRequest queryReq){
		return marketingSuitsRepository.findAll(
				MarketingSuitsWhereCriteriaBuilder.build(queryReq),
				queryReq.getPageRequest());
	}

	/**
	 * 列表查询组合商品主表
	 * @author zhk
	 */
	public List<MarketingSuits> list(MarketingSuitsQueryRequest queryReq){
		return marketingSuitsRepository.findAll(MarketingSuitsWhereCriteriaBuilder.build(queryReq));
	}

	/**
	 * 将实体包装成VO
	 * @author zhk
	 */
	public MarketingSuitsVO wrapperVo(MarketingSuits marketingSuits) {
		if (marketingSuits != null){
			MarketingSuitsVO marketingSuitsVO = KsBeanUtil.convert(marketingSuits, MarketingSuitsVO.class);
			return marketingSuitsVO;
		}
		return null;
	}
}

