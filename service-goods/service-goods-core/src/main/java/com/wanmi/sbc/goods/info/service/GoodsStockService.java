package com.wanmi.sbc.goods.info.service;


import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.erp.api.enums.ShopCenterEnum;
import com.wanmi.sbc.erp.api.req.ShopCenterGoodsStockOrCostPriceReq;
import com.wanmi.sbc.erp.api.resp.NewGoodsInfoResp;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.erp.api.provider.GuanyierpProvider;
import com.wanmi.sbc.erp.api.provider.ShopCenterProductProvider;
import com.wanmi.sbc.erp.api.request.NewGoodsInfoRequest;
import com.wanmi.sbc.erp.api.resp.ShopCenterGoodsCostPriceResp;
import com.wanmi.sbc.erp.api.resp.ShopCenterGoodsStockResp;
import com.wanmi.sbc.goods.api.enums.GoodsBlackListCategoryEnum;
import com.wanmi.sbc.goods.api.request.blacklist.GoodsBlackListPageProviderRequest;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncStockReq;
import com.wanmi.sbc.goods.api.response.blacklist.GoodsBlackListPageProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.blacklist.service.GoodsBlackListService;
import com.wanmi.sbc.goods.bookuu.BooKuuSupplierClient;
import com.wanmi.sbc.goods.bookuu.request.GoodsStockRequest;
import com.wanmi.sbc.goods.bookuu.response.GoodsStockResponse;
import com.wanmi.sbc.goods.info.model.entity.GoodsStockInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.model.root.GoodsStockSync;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.repository.GoodsStockSyncRepository;
import com.wanmi.sbc.goods.info.request.ErpGoodsInfoRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoStockAndCostPriceSyncRequest;
import com.wanmi.sbc.goods.info.request.GoodsStockSyncQueryRequest;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 商品缓存服务
 * Created by daiyitian on 2017/4/11.
 */
@Slf4j
@Service
public class GoodsStockService {

	@Autowired
	private RedisService redisService;

	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private GoodsInfoStockService goodsInfoStockService;

	@Autowired
	private GoodsInfoRepository goodsInfoRepository;

	@Autowired
	private GuanyierpProvider guanyierpProvider;
	@Autowired
	private ShopCenterProductProvider shopCenterProductProvider;

	@Autowired
	private GoodsStockSyncRepository goodsStockSyncRepository;

	@Autowired
	private GoodsStockService goodsStockService;

	@Autowired
	private GoodsBlackListService goodsBlackListService;

	@Autowired
	private BooKuuSupplierClient booKuuSupplierClient;

	@Value("${default.providerId}")
	private String defaultProviderId;


	/**
	 * 批量加库存
	 *
	 * @param dtoList 增量库存参数
	 */
	public void batchAddStock(List<GoodsPlusStockDTO> dtoList) {
		if (CollectionUtils.isEmpty(dtoList)) {
			return;
		}
		//缓存是扣库存性缓存，加库存则扣除
		List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsId(), -g.getStock()))
				.collect(Collectors.toList());
		redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
	}

	/**
	 * 批量减库存
	 *
	 * @param dtoList 减量库存参数
	 */
	public void batchSubStock(List<GoodsMinusStockDTO> dtoList) {
		if (CollectionUtils.isEmpty(dtoList)) {
			return;
		}
		List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsId(), g.getStock()))
				.collect(Collectors.toList());
		redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE, beans);
	}

	@Transactional
	public void syncStock() {
		log.info("更新spu库存");
		Map<String, String> res = redisService.hgetAll(CacheKeyConstant.GOODS_STOCK_SUB_CACHE);
		List<GoodsMinusStockDTO> errStock = new ArrayList<>();//保存更新异常的数据
		List<GoodsPlusStockDTO> resetStock = new ArrayList<>();//重置
		if (MapUtils.isNotEmpty(res)) {
			res.forEach((goodsId, stock) -> {
				log.info("更新spu库存，扣除{} -> spuId:{}", stock, goodsId);
				Long tmpStock = NumberUtils.toLong(stock, 0);
				if (tmpStock != 0) {
					try {
						//持久至mysql
						goodsRepository.subStockById(tmpStock, goodsId);
						resetStock.add(new GoodsPlusStockDTO(tmpStock, goodsId));
					} catch (Exception e) {
						log.error("扣除spu库存异常", e);
						errStock.add(new GoodsMinusStockDTO(tmpStock, goodsId));
					}
				}
			});


			LocalTime nowTime = LocalTime.now();
			if (nowTime.getHour() > 0 && nowTime.getHour() < 8) {
				//如果在夜间，全清
				redisService.delete(CacheKeyConstant.GOODS_STOCK_SUB_CACHE);
				//保留错误数据，以便下次同步继续
				batchSubStock(errStock);
			} else {//重置-扣除redis
				batchAddStock(resetStock);
			}
		}
	}

	/**
	 * 同步ERP商品库存
	 */
//    @Transactional
//    public Map<String, Map<String, Integer>> syncERPGoodsStock(String skuNo, int pageNum, int pageSize) {
//        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
//        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        infoQueryRequest.setGoodsType(GoodsType.VIRTUAL_COUPON.toValue());
//        infoQueryRequest.setAddedFlags(Lists.newArrayList(AddedFlag.YES.toValue(), AddedFlag.PART.toValue()));
//        if (StringUtils.isNoneBlank(skuNo)) {
//            infoQueryRequest.setLikeGoodsInfoNo(skuNo);
//        }
//        if (pageNum >= 0) {
//            infoQueryRequest.setPageNum(pageNum);
//        }
//        infoQueryRequest.setPageSize(pageSize);
//        Page<GoodsInfo> goodsInfoPage = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria(),
//                infoQueryRequest.getPageRequest());
//        List<GoodsInfo> goodsInfoList = goodsInfoPage.getContent();
//        // 3 遍历商品列表
//        Map<String, Map<String, Integer>> resultMap = new HashMap<>();
//        Map<String, Integer> skuStockMap = new HashMap<>();
//        goodsInfoList.forEach(goodsInfo -> {
//            //判断是否是组合商品，组合商品的库存不同步
//            log.info("========GoodsInfoId:{} ========  GoodsId：{}==========", goodsInfo.getGoodsInfoId(), goodsInfo.getGoodsId());
//            if (Objects.nonNull(goodsInfo.getCombinedCommodity()) && !goodsInfo.getCombinedCommodity()) {
//                SynGoodsInfoRequest erpSynGoodsStockRequest =
//                        SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
//                BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
//                        guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
//                if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse) && !ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse.getContext())) {
//                    List<ERPGoodsInfoVO> erpGoodsStockVOList =
//                            erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
//                    if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)) {
//                        //根据SKU维度查询库存,接口只会返回一条数据,所有只需要取List.get(0)
//                        ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
//                        log.info("========GoodsInfoId:{} ========  erpGoodsInfoVO：{}==========", goodsInfo.getGoodsInfoId(), erpGoodsInfoVO);
//                        // 只有大于0的库存才能同步
//                        if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0) {
//                            Long actualStock = goodsInfoStockService.getActualStock(Long.valueOf(erpGoodsInfoVO.getSalableQty()), goodsInfo.getGoodsInfoId());
//                            goodsInfoStockService.resetGoodsById(Long.valueOf(erpGoodsInfoVO.getSalableQty()), goodsInfo.getGoodsInfoId(), actualStock);
//                            skuStockMap.put(goodsInfo.getGoodsInfoId(), erpGoodsInfoVO.getSalableQty());
//                        }
//                    }
//                }
//            }
//        });
//
//        //同步更新SPU库存
//        Map<String, Integer> spuStockMap = new HashMap<>();
//        List<String> goodsIds = goodsInfoList.stream().map(GoodsInfo::getGoodsId).distinct().collect(Collectors.toList());
//        List<Goods> goodsList = goodsService.listByGoodsIds(goodsIds);
//        goodsList.stream().forEach(goodsVO -> {
//            List<GoodsInfo> goodsInfos = goodsInfoRepository.findByGoodsIds(Lists.newArrayList(goodsVO.getGoodsId()));
//            List<Long> salableQty = new ArrayList<>();
//            goodsInfos.forEach(goodsInfo -> {
//                log.info("========同步spu库存GoodsInfoId:{} ==================", goodsInfo.getGoodsInfoId());
//                if (Objects.nonNull(goodsInfo.getCombinedCommodity()) && !goodsInfo.getCombinedCommodity()) {
//                    SynGoodsInfoRequest erpSynGoodsStockRequest =
//                            SynGoodsInfoRequest.builder().skuCode(goodsInfo.getErpGoodsInfoNo()).build();
//                    BaseResponse<SyncGoodsInfoResponse> erpSyncGoodsStockResponseBaseResponse =
//                            guanyierpProvider.syncGoodsStock(erpSynGoodsStockRequest);
//                    if (!ObjectUtils.isEmpty(erpSyncGoodsStockResponseBaseResponse)
//                            && Objects.nonNull(erpSyncGoodsStockResponseBaseResponse.getContext())) {
//
//                        List<ERPGoodsInfoVO> erpGoodsStockVOList =
//                                erpSyncGoodsStockResponseBaseResponse.getContext().getErpGoodsInfoVOList();
//                        if (CollectionUtils.isNotEmpty(erpGoodsStockVOList)) {
//                            ERPGoodsInfoVO erpGoodsInfoVO = erpGoodsStockVOList.stream().max(Comparator.comparing(ERPGoodsInfoVO::getSalableQty)).get();
//                            // 只有大于0的库存才能同步
//                            log.info("========同步spu库存GoodsInfoId:{} ========  ERPGoodsInfoVO：{}==========", goodsInfo.getGoodsInfoId(), erpGoodsInfoVO);
//                            if (Long.valueOf(erpGoodsInfoVO.getSalableQty()) > 0) {
//                                salableQty.add(Long.valueOf(erpGoodsInfoVO.getSalableQty()));
//                            }
//                        }
//                    }
//                } else {
//                    salableQty.add(goodsInfo.getStock());
//                    log.info("========同步spu库存组合购商品GoodsInfoId:{}==========", goodsInfo.getGoodsInfoId());
//                }
//            });
//            //计算spu的商品总数
//            if (CollectionUtils.isNotEmpty(salableQty)) {
//                Long salableQtySum = salableQty.stream().reduce(Long::sum).get();
//                if (salableQtySum > 0) {
//                    goodsRepository.resetGoodsStockById(Long.valueOf(salableQtySum), goodsVO.getGoodsId());
//                    spuStockMap.put(goodsVO.getGoodsId(), salableQtySum.intValue());
//                    log.info("========goodsVOId:{} ========  salableQtySum：{}==========", goodsVO.getGoodsId(), salableQtySum);
//                }
//            }
//        });
//        resultMap.put("skus", skuStockMap);
//        resultMap.put("spus", spuStockMap);
//        log.info("resultMap====================>:{}", resultMap);
//        return resultMap;
//    }


	/**
	 * 批量更新库存 根据频率计算，此处可以考虑更新 前200个商品 以及剩余的商品
	 *
	 * @param hasSaveRedis 只有定时任务才设置为 true，保证每次采集到的都是上一次的数据
	 */
	public GoodsInfoStockSyncMaxIdProviderResponse batchUpdateStock(List<String> goodsIdList, boolean hasSaveRedis, List<ShopCenterSyncStockReq> shopCenterSyncStockReqs) {
		log.info("GoodsStockService batchUpdateStock goodsIdList: {} hasSaveRedis:{} erpGoodsNoList:{}",
				JSON.toJSONString(goodsIdList), hasSaveRedis, JSON.toJSONString(shopCenterSyncStockReqs));
		long beginTime = System.currentTimeMillis();
//		String providerId = defaultProviderId; //管易云
		List<String> goodsInfoIdList = new ArrayList<>();
		List<GoodsInfoStockSyncProviderResponse> tmpResult = new ArrayList<>();
		if (!CollectionUtils.isEmpty(shopCenterSyncStockReqs)) {
			List<String> erpGoodsNoList = shopCenterSyncStockReqs.stream().map(ShopCenterSyncStockReq::getGoodsCode).collect(Collectors.toList());
			List<Map<String, Object>> goodsInfoList = goodsInfoRepository.listByErpGoodsInfoNo(erpGoodsNoList);
			for (Map<String, Object> goodsParam : goodsInfoList) {
				for (Map.Entry<String, Object> entry : goodsParam.entrySet()) {
					if ("goods_info_id".equals(entry.getKey())) {
						goodsInfoIdList.add(entry.getValue().toString());
					}
				}
			}
		}

		if (!CollectionUtils.isEmpty(goodsIdList) || !CollectionUtils.isEmpty(goodsInfoIdList)) {
			shopCenterSyncStockReqs = shopCenterSyncStockReqs == null ? new ArrayList<>() : shopCenterSyncStockReqs;
			Map<String, ShopCenterSyncStockReq> erpGoodsInfoNo2ModelMap =
					shopCenterSyncStockReqs.stream().collect(Collectors.toMap(ShopCenterSyncStockReq::getGoodsCode, Function.identity(), (k1, k2) -> k1));

			GoodsInfoQueryRequest goodsInfoQueryRequest = new GoodsInfoQueryRequest();
			if (CollectionUtils.isNotEmpty(goodsInfoIdList)) {
				goodsInfoQueryRequest.setGoodsInfoIds(goodsInfoIdList);
			} else {
				goodsInfoQueryRequest.setGoodsIds(goodsIdList);
			}

			goodsInfoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
			goodsInfoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
			List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(goodsInfoQueryRequest.getWhereCriteria());
			List<GoodsInfoStockAndCostPriceSyncRequest> goodsInfoStockAndCostPriceSyncRequests = new ArrayList<>();
			for (GoodsInfo goodsInfoParam : goodsInfoList) {
				GoodsInfoStockAndCostPriceSyncRequest goodsInfoStockAndCostPriceSyncRequest = new GoodsInfoStockAndCostPriceSyncRequest();
				goodsInfoStockAndCostPriceSyncRequest.setGoodsId(goodsInfoParam.getGoodsId());
				goodsInfoStockAndCostPriceSyncRequest.setGoodsInfoId(goodsInfoParam.getGoodsInfoId());
				goodsInfoStockAndCostPriceSyncRequest.setErpGoodsNo(goodsInfoParam.getErpGoodsNo());
				goodsInfoStockAndCostPriceSyncRequest.setErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());
				goodsInfoStockAndCostPriceSyncRequest.setCostPrice(goodsInfoParam.getCostPrice());
				goodsInfoStockAndCostPriceSyncRequest.setCostPriceSyncFlag(goodsInfoParam.getCostPriceSyncFlag());
				goodsInfoStockAndCostPriceSyncRequest.setStockQTY(goodsInfoParam.getStock());
				goodsInfoStockAndCostPriceSyncRequest.setStockSyncFlag(goodsInfoParam.getStockSyncFlag());
				goodsInfoStockAndCostPriceSyncRequest.setGoodsInfoNo(goodsInfoParam.getGoodsInfoNo());
				goodsInfoStockAndCostPriceSyncRequest.setGoodsInfoName(goodsInfoParam.getGoodsInfoName());
				goodsInfoStockAndCostPriceSyncRequest.setMarketPrice(goodsInfoParam.getMarketPrice());
				goodsInfoStockAndCostPriceSyncRequest.setHasSaveRedis(hasSaveRedis);
				ShopCenterSyncStockReq shopCenterSyncStockReq = erpGoodsInfoNo2ModelMap.get(goodsInfoParam.getErpGoodsInfoNo());
				log.info("GoodsStockService batchUpdateStock shopCenterSyncStockReq {}", JSON.toJSONString(shopCenterSyncStockReq));
				if (shopCenterSyncStockReq != null) {
					goodsInfoStockAndCostPriceSyncRequest.setQuantity(shopCenterSyncStockReq.getQuantity() == null ? 0 : shopCenterSyncStockReq.getQuantity());
					goodsInfoStockAndCostPriceSyncRequest.setTag(shopCenterSyncStockReq.getTag());
				}
				log.info("GoodsStockService batchUpdateStock goodsInfoStockAndCostPriceSyncRequest {}", JSON.toJSONString(goodsInfoStockAndCostPriceSyncRequest));
				goodsInfoStockAndCostPriceSyncRequests.add(goodsInfoStockAndCostPriceSyncRequest);
			}

			if (CollectionUtils.isNotEmpty(goodsInfoStockAndCostPriceSyncRequests)) {
				tmpResult.addAll(this.executeBatchUpdateStockAndCostPrice(goodsInfoStockAndCostPriceSyncRequests));
			}
		}
//		else if (CollectionUtils.isNotEmpty(erpGoodsNoList)) {
//		} else if (CollectionUtils.isNotEmpty(erpGoodsNoList)) {
//			List<Map<String, Object>> goodsInfoList = goodsInfoRepository.listByErpGoodsNo(erpGoodsNoList);
//			List<GoodsInfoStockAndCostPriceSyncRequest> goodsInfoStockAndCostPriceSyncRequests = new ArrayList<>();
//			for (Map<String, Object> goodsParam : goodsInfoList) {
//				long tmpId = 0L;
//				String goodsId = "";
//				String goodsInfoId = "";
//				String erpGoodsNo = "";
//				String erpGoodsInfoNo = "";
//				BigDecimal costPrice = BigDecimal.ZERO;
//				int costPriceSyncFlag = 0;
//				Long stockQTY = 0L;
//				int stockSyncFlag = 0;
//				String goodsInfoNo = "";
//				String goodsInfoName = "";
//				BigDecimal marketPrice = BigDecimal.ZERO;
//				for (Map.Entry<String, Object> entry : goodsParam.entrySet()) {
//
//					if ("tmp_id".equals(entry.getKey())) {
//						tmpId = Long.parseLong(entry.getValue().toString());
//					}
//					if ("goods_id".equals(entry.getKey())) {
//						goodsId = entry.getValue().toString();
//					}
//					if ("goods_info_id".equals(entry.getKey())) {
//						goodsInfoId = entry.getValue().toString();
//					}
//					if ("erp_goods_no".equals(entry.getKey())) {
//						erpGoodsNo = entry.getValue().toString();
//					}
//					if ("erp_goods_info_no".equals(entry.getKey())) {
//						erpGoodsInfoNo = entry.getValue() == null ? "" : entry.getValue().toString();
//					}
//					if ("cost_price".equals(entry.getKey())) {
//						costPrice = new BigDecimal(entry.getValue().toString());
//					}
//					if ("cost_price_sync_flag".equals(entry.getKey())) {
//						costPriceSyncFlag = Integer.parseInt(entry.getValue().toString());
//					}
//					if ("stock".equals(entry.getKey())) {
//						stockQTY = Long.parseLong(entry.getValue().toString());
//					}
//					if ("stock_sync_flag".equals(entry.getKey())) {
//						stockSyncFlag = Integer.parseInt(entry.getValue().toString());
//					}
//					if ("goods_info_no".equals(entry.getKey())) {
//						goodsInfoNo = entry.getValue().toString();
//					}
//					if ("goods_info_name".equals(entry.getKey())) {
//						goodsInfoName = entry.getValue().toString();
//					}
//					if ("market_price".equals(entry.getKey())) {
//						marketPrice = new BigDecimal(entry.getValue().toString());
//					}
//				}
//				GoodsInfoStockAndCostPriceSyncRequest goodsInfoStockAndCostPriceSyncRequest = new GoodsInfoStockAndCostPriceSyncRequest();
//				goodsInfoStockAndCostPriceSyncRequest.setGoodsId(goodsId);
//				goodsInfoStockAndCostPriceSyncRequest.setGoodsInfoId(goodsInfoId);
//				goodsInfoStockAndCostPriceSyncRequest.setErpGoodsNo(erpGoodsNo);
//				goodsInfoStockAndCostPriceSyncRequest.setErpGoodsInfoNo(erpGoodsInfoNo);
//				goodsInfoStockAndCostPriceSyncRequest.setCostPrice(costPrice);
//				goodsInfoStockAndCostPriceSyncRequest.setCostPriceSyncFlag(costPriceSyncFlag);
//				goodsInfoStockAndCostPriceSyncRequest.setStockQTY(stockQTY);
//				goodsInfoStockAndCostPriceSyncRequest.setStockSyncFlag(stockSyncFlag);
//				goodsInfoStockAndCostPriceSyncRequest.setGoodsInfoNo(goodsInfoNo);
//				goodsInfoStockAndCostPriceSyncRequest.setGoodsInfoName(goodsInfoName);
//				goodsInfoStockAndCostPriceSyncRequest.setMarketPrice(marketPrice);
//				goodsInfoStockAndCostPriceSyncRequest.setHasSaveRedis(hasSaveRedis);
//				goodsInfoStockAndCostPriceSyncRequests.add(goodsInfoStockAndCostPriceSyncRequest);
////				if (maxTmpId < tmpId) {
////					maxTmpId = tmpId;
////				}
//			}
//
//			if (CollectionUtils.isNotEmpty(goodsInfoStockAndCostPriceSyncRequests)) {
//				tmpResult.addAll(this.executeBatchUpdateStockAndCostPrice(goodsInfoStockAndCostPriceSyncRequests));
//			}
//		}
		else {
			log.info("GoodsStockService batchUpdateStock goodsIdList and erpGoodsNoList is Empty return");
		}

		if (CollectionUtils.isNotEmpty(tmpResult)) {
			//更新goods库存
			Map<String, Integer> spuId2ActualStockQtyMap =
					tmpResult.stream().collect(Collectors.groupingBy(GoodsInfoStockSyncProviderResponse::getSpuId, Collectors.summingInt(GoodsInfoStockSyncProviderResponse::getActualStockQty)));
			spuId2ActualStockQtyMap.forEach((K, V) -> {
				goodsRepository.updateStockByGoodsId(Long.valueOf(V), K);
			});
		}
		GoodsInfoStockSyncMaxIdProviderResponse result = new GoodsInfoStockSyncMaxIdProviderResponse();
//		result.setMaxTmpId(maxTmpId);
		result.setGoodsInfoStockSyncList(tmpResult);
		log.info("GoodsStockService batchUpdateStock complete all cost: {} ms", (System.currentTimeMillis() - beginTime));
		return result;
	}


	/**
	 * 执行批量更新库存
	 */
	private List<GoodsInfoStockSyncProviderResponse> executeBatchUpdateStockAndCostPrice(List<GoodsInfoStockAndCostPriceSyncRequest> goodsInfoStockAndCostPriceSyncRequests) {
		if (CollectionUtils.isEmpty(goodsInfoStockAndCostPriceSyncRequests)) {
			log.info("GoodsStockService batchUpdateStock erpGoodsCodeNo:{} erpGoodsCodeNo is null return ", JSON.toJSONString(goodsInfoStockAndCostPriceSyncRequests));
			return new ArrayList<>();
		}
//		//获取仓库黑名单
//		List<String> unStaticsKey = new ArrayList<>();
//		GoodsBlackListPageProviderRequest goodsBlackListPageProviderRequest = new GoodsBlackListPageProviderRequest();
//		goodsBlackListPageProviderRequest.setBusinessCategoryColl(
//				Collections.singletonList(GoodsBlackListCategoryEnum.UN_SHOW_WAREHOUSE.getCode()));
//		GoodsBlackListPageProviderResponse goodsBlackListPageProviderResponse = goodsBlackListService.listNoPage(goodsBlackListPageProviderRequest);
//		if (goodsBlackListPageProviderResponse.getWareHouseListModel() != null && !CollectionUtils.isEmpty(goodsBlackListPageProviderResponse.getWareHouseListModel().getNormalList())) {
//			unStaticsKey.addAll(goodsBlackListPageProviderResponse.getWareHouseListModel().getNormalList());
//		}

//
//		Set<String> erpGoodsCodeNoSet = goodsInfoStockAndCostPriceSyncRequests.stream().map(GoodsInfoStockAndCostPriceSyncRequest::getErpGoodsNo).collect(Collectors.toSet());
//		log.info("GoodsStockService batchUpdateStock erpGoodsCodeNoSet:{} ", JSON.toJSONString(erpGoodsCodeNoSet));
//		if (CollectionUtils.isEmpty(erpGoodsCodeNoSet)) {
//			log.info("GoodsStockService batchUpdateStock erpGoodsCodeNoSet isEmpty return ");
//			return new ArrayList<>();
//		}

		//获取sku 库存状态
		Map<String, ErpGoodsInfoRequest> erpSkuCode2ErpGoodsInfoMap = new HashMap<>();

		Set<String> erpGoodsCodeSyncCostPriceSet = new HashSet<>();
		Set<String> erpGoodsCodeSyncStockSet = new HashSet<>();

		for (GoodsInfoStockAndCostPriceSyncRequest goodsInfoStockAndCostPriceSyncRequest : goodsInfoStockAndCostPriceSyncRequests) {

			if (!Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getStockSyncFlag(), 1)
					&& !Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getCostPriceSyncFlag(), 1)) {
				log.info("GoodsStockService goodsInfoId:{} 既不需要同步成本价 又不需要同步库存", goodsInfoStockAndCostPriceSyncRequest.getGoodsInfoId());
				continue;
			}

			if (Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getTag(), 1001)) {
				//如果设置
				if (!Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getStockSyncFlag(), 1)) {
					continue;
				}

				ErpGoodsInfoRequest erpGoodsInfoRequest = erpSkuCode2ErpGoodsInfoMap.get(goodsInfoStockAndCostPriceSyncRequest.getErpGoodsInfoNo());
				if (erpGoodsInfoRequest == null) {
					erpGoodsInfoRequest = new ErpGoodsInfoRequest();
					erpSkuCode2ErpGoodsInfoMap.put(goodsInfoStockAndCostPriceSyncRequest.getErpGoodsInfoNo(), erpGoodsInfoRequest);
				}
				erpGoodsInfoRequest.setHasSyncStock(true);
//				erpGoodsInfoRequest.setHasSyncCostPrice(false);
				erpGoodsInfoRequest.setErpStock(goodsInfoStockAndCostPriceSyncRequest.getQuantity().longValue());
				continue;
			}

			if (Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getTag(), 1004)) {
				if (!Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getCostPriceSyncFlag(), 1)) {
					continue;
				}

				ErpGoodsInfoRequest erpGoodsInfoRequest = erpSkuCode2ErpGoodsInfoMap.get(goodsInfoStockAndCostPriceSyncRequest.getErpGoodsInfoNo());
				if (erpGoodsInfoRequest == null) {
					erpGoodsInfoRequest = new ErpGoodsInfoRequest();
					erpSkuCode2ErpGoodsInfoMap.put(goodsInfoStockAndCostPriceSyncRequest.getErpGoodsInfoNo(), erpGoodsInfoRequest);
				}
//				erpGoodsInfoRequest.setHasSyncStock(false);
				erpGoodsInfoRequest.setHasSyncCostPrice(true);
				erpGoodsInfoRequest.setErpCostPrice(new BigDecimal(goodsInfoStockAndCostPriceSyncRequest.getQuantity()+"").divide(new BigDecimal("100"),2, RoundingMode.HALF_DOWN));
				continue;
			}


			if (Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getCostPriceSyncFlag(), 1)) {
				erpGoodsCodeSyncCostPriceSet.add(goodsInfoStockAndCostPriceSyncRequest.getErpGoodsInfoNo());
			}

			if (Objects.equals(goodsInfoStockAndCostPriceSyncRequest.getStockSyncFlag(), 1)) {
				erpGoodsCodeSyncStockSet.add(goodsInfoStockAndCostPriceSyncRequest.getErpGoodsInfoNo());
			}
		}



		if (!CollectionUtils.isEmpty(erpGoodsCodeSyncCostPriceSet)) {
			ShopCenterGoodsStockOrCostPriceReq shopCenterGoodsStockOrCostPriceReq = new ShopCenterGoodsStockOrCostPriceReq();
			shopCenterGoodsStockOrCostPriceReq.setGoodsCodes(new ArrayList<>(erpGoodsCodeSyncCostPriceSet));
			shopCenterGoodsStockOrCostPriceReq.setShopId(ShopCenterEnum.SHOPCENTER.getCode());
			List<ShopCenterGoodsCostPriceResp> infoList = shopCenterProductProvider.searchGoodsCostPrice(shopCenterGoodsStockOrCostPriceReq).getContext();
			for (ShopCenterGoodsCostPriceResp shopCenterGoodsCostPriceResp : infoList) {
				ErpGoodsInfoRequest erpGoodsInfoRequest = erpSkuCode2ErpGoodsInfoMap.get(shopCenterGoodsCostPriceResp.getGoodsCode());
				if (erpGoodsInfoRequest == null) {
					erpGoodsInfoRequest = new ErpGoodsInfoRequest();
					erpSkuCode2ErpGoodsInfoMap.put(shopCenterGoodsCostPriceResp.getGoodsCode(), erpGoodsInfoRequest);
				}
				erpGoodsInfoRequest.setHasSyncCostPrice(true);
				erpGoodsInfoRequest.setErpCostPrice(new BigDecimal(shopCenterGoodsCostPriceResp.getCostPrice()+"").divide(new BigDecimal("100"),2, RoundingMode.HALF_DOWN));
			}
		}

		if (!CollectionUtils.isEmpty(erpGoodsCodeSyncStockSet)) {
			ShopCenterGoodsStockOrCostPriceReq shopCenterGoodsStockOrCostPriceReq = new ShopCenterGoodsStockOrCostPriceReq();
			shopCenterGoodsStockOrCostPriceReq.setGoodsCodes(new ArrayList<>(erpGoodsCodeSyncStockSet));
			shopCenterGoodsStockOrCostPriceReq.setShopId(ShopCenterEnum.SHOPCENTER.getCode());
			List<ShopCenterGoodsStockResp> infoList = shopCenterProductProvider.searchGoodsStock(shopCenterGoodsStockOrCostPriceReq).getContext();
			for (ShopCenterGoodsStockResp shopCenterGoodsStockResp : infoList) {
				ErpGoodsInfoRequest erpGoodsInfoRequest = erpSkuCode2ErpGoodsInfoMap.get(shopCenterGoodsStockResp.getGoodsCode());
				if (erpGoodsInfoRequest == null) {
					erpGoodsInfoRequest = new ErpGoodsInfoRequest();
					erpSkuCode2ErpGoodsInfoMap.put(shopCenterGoodsStockResp.getGoodsCode(), erpGoodsInfoRequest);
				}
				erpGoodsInfoRequest.setHasSyncStock(true);
				erpGoodsInfoRequest.setErpStock(shopCenterGoodsStockResp.getQuantity().longValue());
			}
		}


		log.info("GoodsStockService batchUpdateStock goodsInfoStockAndCostPriceSyncRequests:{} erpSkuCode2ErpGoodsInfoMap:{} "
				, JSON.toJSONString(goodsInfoStockAndCostPriceSyncRequests), erpSkuCode2ErpGoodsInfoMap);
		return goodsInfoStockService.batchUpdateGoodsInfoStockAndCostPrice(goodsInfoStockAndCostPriceSyncRequests, erpSkuCode2ErpGoodsInfoMap);
	}


	public long countGoodsStockSync() {
		Specification<GoodsStockSync> request = (root, cquery, cbuild) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(cbuild.equal(root.get("status"), 0));
			predicates.add(cbuild.equal(root.get("deleted"), 0));
			Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
			return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
		};
		return goodsStockSyncRepository.count(request);
	}

	/**
	 * 查询goods_stock_sync并同步库存
	 *
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<GoodsInfoStockSyncProviderResponse> syncGoodsStock(int pageNum, int pageSize) {
		List<GoodsInfoStockSyncProviderResponse> result = new ArrayList<>();
		GoodsStockSyncQueryRequest stockSyncQueryRequest = new GoodsStockSyncQueryRequest();
		stockSyncQueryRequest.setStatus(0);
		if (pageNum >= 0) {
			stockSyncQueryRequest.setPageNum(pageNum);
		}
		stockSyncQueryRequest.setPageSize(pageSize);
		Page<GoodsStockSync> stockPage = goodsStockSyncRepository.findAll(stockSyncQueryRequest.getWhereCriteria(), stockSyncQueryRequest.getPageRequest());
		if (CollectionUtils.isEmpty(stockPage.getContent())) {
			return result;
		}
		List<GoodsStockSync> goodsStockList = stockPage.getContent();
		// 3 遍历商品列表并更新sku库存
		for (GoodsStockSync goodsStockSyncParam : goodsStockList) {
			GoodsInfoStockSyncProviderResponse goodsInfoStockSync = goodsStockService.updateGoodsStockSingle(goodsStockSyncParam);
			if (goodsInfoStockSync == null) {
				continue;
			}
			result.add(goodsInfoStockSync);
		}
		return result;
	}

	@Transactional
	public GoodsInfoStockSyncProviderResponse updateGoodsStockSingle(GoodsStockSync stock) {
		//查询sku信息
		GoodsStockInfo goodsInfo = goodsInfoRepository.findGoodsInfoId(stock.getGoodsNo());
		if (goodsInfo == null) {
//            goodsStockSyncRepository.updateStatus(stock.getId());
			log.info("updateGoodsStockSingle there is no sku or stock,stockSyncFlag is false, goods:{},goodsInfo:{}", stock, goodsInfo);
			return null;
		}

		Long actualStock = goodsInfo.getStockQty();
		if (Objects.equals(goodsInfo.getStockSyncFlag(), 1)) {
			actualStock = goodsInfoStockService.getActualStock(stock.getStock().longValue(), goodsInfo.getGoodsInfoId());
			goodsInfoStockService.resetStockByGoodsInfoId(stock.getStock().longValue(), goodsInfo.getGoodsInfoId(), actualStock);
			//更新spu库存
			goodsRepository.resetGoodsStockById(actualStock, goodsInfo.getGoodsId());
			//更新状态
			goodsStockSyncRepository.updateStatus(stock.getId());
		}

		GoodsInfoStockSyncProviderResponse goodsInfoStockSyncProviderResponse = new GoodsInfoStockSyncProviderResponse();
		goodsInfoStockSyncProviderResponse.setSpuId(goodsInfo.getGoodsId());
		goodsInfoStockSyncProviderResponse.setSkuId(goodsInfo.getGoodsInfoId());
		goodsInfoStockSyncProviderResponse.setSkuNo(goodsInfo.getGoodsInfoNo());
		goodsInfoStockSyncProviderResponse.setSkuName(goodsInfo.getGoodsInfoName());
		goodsInfoStockSyncProviderResponse.setActualStockQty(actualStock.intValue());

		//存入redis
		String lastSyncStockKey = goodsInfo.getGoodsInfoId();


		try {
			String lastSyncStockValue = redisService.getHashValue(RedisKeyConstant.GOODS_INFO_SYNC_STOCK_KEY, lastSyncStockKey);
			log.info("GoodsStockService lastSyncStockKey {} lastSyncStockValue {}", lastSyncStockKey, lastSyncStockValue);

			if (!StringUtils.isBlank(lastSyncStockValue)) {
				goodsInfoStockSyncProviderResponse.setLastStockQty(Integer.parseInt(lastSyncStockValue));
			} else {
				goodsInfoStockSyncProviderResponse.setLastStockQty(0);
			}
		} catch (Exception ex) {
			log.error("GoodsStockService lastSyncStockKey {} lastSyncStockValue {} exception", lastSyncStockKey, ex);

		}
		redisService.putHash(RedisKeyConstant.GOODS_INFO_SYNC_STOCK_KEY, lastSyncStockKey, actualStock.toString(), 6 * 30 * 24 * 60 * 60);
		return goodsInfoStockSyncProviderResponse;
	}


	/**
	 * 指定商品同步库存
	 */
	@Transactional
	public void bookuuSyncGoodsStock(List<String> goodsIdList) {
		log.info("GoodsStockService bookuuSyncGoodsStock begin param: {}", JSON.toJSONString(goodsIdList));
		long beginTime = System.currentTimeMillis();
		GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
		infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
		infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
		infoQueryRequest.setGoodsIds(goodsIdList);
		List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
		if (CollectionUtils.isEmpty(goodsInfoList)) {
			return;
		}

		//获取库存
		GoodsStockRequest request = new GoodsStockRequest();
		request.setGoodsIdList(goodsIdList);
		BaseResponse<List<GoodsStockResponse>> goodsStock = booKuuSupplierClient.getGoodsStock(request);
		List<GoodsStockResponse> context = goodsStock.getContext();
		if (CollectionUtils.isEmpty(context)) {
			return;
		}

		Map<String, Integer> erpGoodsNo2StockQtyMap = new HashMap<>();
		for (GoodsStockResponse goodsStockResponse : context) {
			erpGoodsNo2StockQtyMap.put(goodsStockResponse.getErpGoodsNo(), goodsStockResponse.getStockQty());
		}


		for (GoodsInfo goodsInfoParam : goodsInfoList) {
			if (Objects.equals(goodsInfoParam.getStockSyncFlag(), 0)) {
				continue;
			}
			Integer stockQty = erpGoodsNo2StockQtyMap.get(goodsInfoParam.getErpGoodsNo());
			stockQty = stockQty == null ? 0 : stockQty;

			Long actualStock = goodsInfoStockService.getActualStock(stockQty.longValue(), goodsInfoParam.getGoodsInfoId());
			goodsInfoStockService.resetStockByGoodsInfoId(stockQty.longValue(), goodsInfoParam.getGoodsInfoId(), actualStock);
			//更新状态
			goodsStockSyncRepository.updateStatusByErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());

			goodsRepository.updateStockByGoodsId(actualStock, goodsInfoParam.getGoodsId());
		}
		log.info("GoodsStockService bookuuSyncGoodsStock end param: {} cost: {}", JSON.toJSONString(goodsIdList), (System.currentTimeMillis() - beginTime));
//        GoodsInfoQueryRequest infoQueryRequest = new GoodsInfoQueryRequest();
//        infoQueryRequest.setDelFlag(DeleteFlag.NO.toValue());
//        infoQueryRequest.setAddedFlag(AddedFlag.YES.toValue());
//        infoQueryRequest.setGoodsIds(goodsIdList);
//        List<GoodsInfo> goodsInfoList = goodsInfoRepository.findAll(infoQueryRequest.getWhereCriteria());
//
//        List<GoodsInfo> filterGoodsInfoList = new ArrayList<>();
//        List<String> filterErpGoodsInfoNoList = new ArrayList<>();
//        Map<String, Long> goodsId2StockQtyMap = new HashMap<>();
//        for (GoodsInfo goodsInfoParam : goodsInfoList) {
//            if (Objects.equals(goodsInfoParam.getStockSyncFlag(),0)) {
//                Long goodsIdStockQty = goodsId2StockQtyMap.get(goodsInfoParam.getGoodsId());
//                goodsIdStockQty = goodsIdStockQty == null ? 0 : goodsIdStockQty + goodsInfoParam.getStock();
//                goodsId2StockQtyMap.put(goodsInfoParam.getGoodsId(), goodsIdStockQty);
//                goodsStockSyncRepository.updateStatusByErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());
//                log.info("bookuuSyncGoodsStock goodsInfoId:{} erpGoodsInfoNo:{} stockSyncFlag : 0 continue", JSON.toJSONString(goodsIdList), goodsInfoParam.getErpGoodsInfoNo());
//                continue;
//            }
//            filterErpGoodsInfoNoList.add(goodsInfoParam.getErpGoodsInfoNo());
//            filterGoodsInfoList.add(goodsInfoParam);
//        }
//
//        //同步库存
//        GoodsStockSyncQueryRequest stockSyncQueryRequest = new GoodsStockSyncQueryRequest();
////        stockSyncQueryRequest.setStatus(0);
//        stockSyncQueryRequest.setDeleted(0);
//        stockSyncQueryRequest.setGoodsNos(filterErpGoodsInfoNoList);
//        List<GoodsStockSync> goodsStockSyncList = goodsStockSyncRepository.findAll(stockSyncQueryRequest.getWhereCriteria());
//        Map<String, GoodsStockSync> erpGoodsInfoNo2GoodsStockSyncMap = new HashMap<>();
//        for (GoodsStockSync goodsStockSync : goodsStockSyncList) {
//            erpGoodsInfoNo2GoodsStockSyncMap.put(goodsStockSync.getGoodsNo(), goodsStockSync);
//        }
//
//        for (GoodsInfo goodsInfoParam : filterGoodsInfoList) {
//            GoodsStockSync goodsStockSync = erpGoodsInfoNo2GoodsStockSyncMap.get(goodsInfoParam.getErpGoodsInfoNo());
//            if (goodsStockSync == null) {
//                log.info("bookuuSyncGoodsStock goodsInfoId:{} goodsStockSync is null continue", JSON.toJSONString(goodsInfoIdList));
//                continue;
//            }
//
//            Long actualStock = goodsInfoStockService.getActualStock(goodsStockSync.getStock().longValue(), goodsInfoParam.getGoodsInfoId());
//            goodsInfoStockService.resetStockByGoodsInfoId(goodsStockSync.getStock().longValue(), goodsInfoParam.getGoodsInfoId(), actualStock);
//            //更新状态
//            goodsStockSyncRepository.updateStatusByErpGoodsInfoNo(goodsInfoParam.getErpGoodsInfoNo());
//
//            Long goodsIdStockQty = goodsId2StockQtyMap.get(goodsInfoParam.getGoodsId());
//            goodsIdStockQty = goodsIdStockQty == null ? actualStock : goodsIdStockQty + actualStock;
//            goodsId2StockQtyMap.put(goodsInfoParam.getGoodsId(), goodsIdStockQty);
//        }
//        goodsId2StockQtyMap.forEach((K, V) -> {
//            goodsRepository.updateStockByGoodsId(Long.valueOf(V), K);
//        });
	}

}
