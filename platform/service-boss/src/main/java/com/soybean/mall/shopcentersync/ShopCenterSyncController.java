package com.soybean.mall.shopcentersync;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.shopcentersync.req.SyncDataReq;
import com.soybean.mall.shopcentersync.resp.ShopCenterSyncResponse;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.feishu.FeiShuNoticeEnum;
import com.wanmi.sbc.feishu.constant.FeiShuMessageConstant;
import com.wanmi.sbc.feishu.service.FeiShuSendMessageService;
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.ShopCenterGoodsProvider;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplate49ChangeReq;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncCostPriceReq;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncStockReq;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
import com.wanmi.sbc.goods.api.response.goods.ShopCenterCostPriceSyncResp;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.order.api.provider.shopcenter.ShopCenterOrdOrderProvider;
import com.wanmi.sbc.order.api.req.ShopCenterSyncDeliveryReq;
import com.wanmi.sbc.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/shopCenterSync")
public class ShopCenterSyncController {
	@Autowired
	private RedisService redisService;
	@Autowired
	private ShopCenterGoodsProvider shopCenterGoodsProvider;
	@Autowired
	private ShopCenterOrdOrderProvider shopCenterOrdOrderProvider;
	@Autowired
	private EsGoodsStockProvider esGoodsStockProvider;
	@Autowired
	private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;
	@Autowired
	private FreightTemplateGoodsProvider freightTemplateGoodsProvider;

	@Autowired
	private FeiShuSendMessageService feiShuSendMessageService;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@PostMapping("/syncData")
	public ShopCenterSyncResponse<Boolean> syncData(@RequestBody SyncDataReq request) {
		log.info("ShopCenterSyncController.syncData.request={}", request);
		Integer tag = request.getTag();
		String data = request.getData();
		String requestId = request.getRequestId();
		if (StringUtils.isEmpty(data)) {
			log.warn("电商中台推送同步.data为空，结束！requestId={}", requestId);
			return ShopCenterSyncResponse.success(false);
		}
		if (Objects.isNull(tag)) {
			log.warn("电商中台推送同步.tag为空，结束！requestId={}", requestId);
			return ShopCenterSyncResponse.success(false);
		}

		try {

			//库存 成本价
			if (Objects.equals(tag, 1001) || Objects.equals(tag, 1004)) {
				ShopCenterSyncStockReq syncStock = JSON.parseObject(data, ShopCenterSyncStockReq.class);
				BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> stockAndPriceResp = shopCenterGoodsProvider.updateStockAndPrice(syncStock);
				log.info("ShopCenterSyncController syncData stockAndPriceResp {}", JSON.toJSONString(stockAndPriceResp));
				refreshAndSendMsg(stockAndPriceResp.getContext());

				//成本价 49包邮
				if (Objects.equals(tag, 1004)) {
					if (!CollectionUtils.isEmpty(stockAndPriceResp.getContext().getGoodsInfoStockSyncList())) {
						Set<String> spuIds = new HashSet<>();
						for (GoodsInfoStockSyncProviderResponse goodsInfoStockSyncProviderResponse : stockAndPriceResp.getContext().getGoodsInfoStockSyncList()) {
							spuIds.add(goodsInfoStockSyncProviderResponse.getSpuId());
						}
						FreightTemplate49ChangeReq freightTemplate49ChangeReq = new FreightTemplate49ChangeReq();
						freightTemplate49ChangeReq.setSpuIds(new ArrayList<>(spuIds));
						freightTemplateGoodsProvider.changeFreeDelivery49(freightTemplate49ChangeReq);
					}
				}
			}

			//发货
			if (Objects.equals(tag, 1002)) {
				ShopCenterSyncDeliveryReq syncDelivery = JSON.parseObject(data, ShopCenterSyncDeliveryReq.class);
				shopCenterOrdOrderProvider.shopCenterSyncDelivery(syncDelivery);
			}
//			switch (tag) {
//				// 库存变动
//				case 1001:
//					ShopCenterSyncStockReq syncStock = JSON.parseObject(data, ShopCenterSyncStockReq.class);
//					BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> stockAndPriceResp = shopCenterGoodsProvider.updateStockAndPrice(syncStock);
//					refreshAndSendMsg(stockAndPriceResp.getContext());
//					break;
//				// 发货
//				case 1002:
//					ShopCenterSyncDeliveryReq syncDelivery = JSON.parseObject(data, ShopCenterSyncDeliveryReq.class);
//					shopCenterOrdOrderProvider.shopCenterSyncDelivery(syncDelivery);
//					break;
//				// 成本变动
//				case 1004:
//					ShopCenterSyncCostPriceReq syncPrice = JSON.parseObject(data, ShopCenterSyncCostPriceReq.class);
//					BaseResponse<ShopCenterCostPriceSyncResp> priceResp = shopCenterGoodsProvider.shopCenterSyncCostPrice(syncPrice);
//					refreshCostPrice(priceResp.getContext());
//					if (!CollectionUtils.isEmpty(priceResp.getContext().getGoodsIds())) {
//						FreightTemplate49ChangeReq freightTemplate49ChangeReq = new FreightTemplate49ChangeReq();
//						freightTemplate49ChangeReq.setSpuIds(priceResp.getContext().getGoodsIds());
//						freightTemplateGoodsProvider.changeFreeDelivery49(freightTemplate49ChangeReq);
//					}
//					break;
//				default:
//					log.warn("电商中台推送同步，未知的数据类型，tag={}!", tag);
//					return ShopCenterSyncResponse.success(false);
//			}

			return ShopCenterSyncResponse.success(true);
		} catch (Exception e) {
			log.warn("电商中台推送同步.异常，结束！requestId={}", requestId, e);
			return ShopCenterSyncResponse.success(false);
		}
	}

	/**
	 * 刷新库存
	 */
	private void refreshAndSendMsg(GoodsInfoStockSyncMaxIdProviderResponse resp) {
		if (CollectionUtils.isEmpty(resp.getGoodsInfoStockSyncList())) {
			log.info("ShopCenterSyncController refreshAndSendMsg goodsInfoStockSyncList is empty return ");
			return;
		}
		Map<String, Integer> skuId2StockQtySumMap = new HashMap<>();
		Map<String, Integer> spuId2StockQtySumMap = new HashMap<>();
		List<GoodsInfoStockSyncProviderResponse> stockSendMessageList = new ArrayList<>();
		List<GoodsInfoStockSyncProviderResponse> costPriceSendMessageList = new ArrayList<>();
		for (GoodsInfoStockSyncProviderResponse goodsInfoStockSyncParam : resp.getGoodsInfoStockSyncList()) {

			//删除缓存
			String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsInfoStockSyncParam.getSpuId());
			if (StringUtils.isNotBlank(goodsDetailInfo)) {
				redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsInfoStockSyncParam.getSpuId());
			}

			//计算skuCode 数量
			Integer stockSumQtyTmp = skuId2StockQtySumMap.get(goodsInfoStockSyncParam.getSkuId());
			stockSumQtyTmp = stockSumQtyTmp == null ? goodsInfoStockSyncParam.getActualStockQty() : stockSumQtyTmp + goodsInfoStockSyncParam.getActualStockQty();
			skuId2StockQtySumMap.put(goodsInfoStockSyncParam.getSkuId(), stockSumQtyTmp);

			//计算spuCode 数量
			stockSumQtyTmp = spuId2StockQtySumMap.get(goodsInfoStockSyncParam.getSpuId());
			stockSumQtyTmp = stockSumQtyTmp == null ? goodsInfoStockSyncParam.getActualStockQty() : stockSumQtyTmp + goodsInfoStockSyncParam.getActualStockQty();
			spuId2StockQtySumMap.put(goodsInfoStockSyncParam.getSpuId(), stockSumQtyTmp);

			//发送消息
			if (goodsInfoStockSyncParam.getActualStockQty() != null && !Objects.equals(goodsInfoStockSyncParam.getActualStockQty(), goodsInfoStockSyncParam.getLastStockQty())) {
				stockSendMessageList.add(goodsInfoStockSyncParam);
			}

			//只是处理需要同步 成本价的商品
			if (goodsInfoStockSyncParam.getActualCostPrice().compareTo(goodsInfoStockSyncParam.getLastCostPrice()) != 0
					|| goodsInfoStockSyncParam.getCurrentMarketPrice().compareTo(goodsInfoStockSyncParam.getLastMarketPrice()) != 0) {
				costPriceSendMessageList.add(goodsInfoStockSyncParam);
			}
		}

		if (!skuId2StockQtySumMap.isEmpty()) {
			//更新库存
			EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skuId2StockQtySumMap).build();
			esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);

			EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spuId2StockQtySumMap).build();
			esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
		}

		if (CollectionUtils.isNotEmpty(costPriceSendMessageList)) {
			List<String> skuIdCostPriceList = new ArrayList<>();
			for (GoodsInfoStockSyncProviderResponse goodsInfoStockSyncParam : costPriceSendMessageList) {
				if (goodsInfoStockSyncParam.getActualCostPrice().compareTo(goodsInfoStockSyncParam.getLastCostPrice()) == 0){
					continue;
				}
				skuIdCostPriceList.add(goodsInfoStockSyncParam.getSkuId());
			}
			if (!CollectionUtils.isEmpty(skuIdCostPriceList)) {
				//更新成本价
				EsGoodsInfoAdjustPriceRequest costPrice = new EsGoodsInfoAdjustPriceRequest();
				costPrice.setGoodsInfoIds(skuIdCostPriceList);
				costPrice.setType(PriceAdjustmentType.MARKET);
				esGoodsInfoElasticProvider.adjustPrice(costPrice);
			}
		}

		//发送库存消息
		for (GoodsInfoStockSyncProviderResponse p : stockSendMessageList) {

			if (p.getActualStockQty() > FeiShuMessageConstant.FEI_SHU_STOCK_LIMIT) {
				continue;
			}
			log.info("ERPGoodsStockSyncJobHandler stock send feishu message :{}", JSON.toJSONString(p));
			String content = MessageFormat.format(FeiShuMessageConstant.FEI_SHU_STOCK_NOTIFY, p.getSkuNo(), p.getSkuName(), sdf.format(new Date()) , p.getActualStockQty());
			feiShuSendMessageService.sendMessage(content, FeiShuNoticeEnum.STOCK);
		}

		//发送成本价消息
		for (GoodsInfoStockSyncProviderResponse p : costPriceSendMessageList) {
			//计算毛利率
			BigDecimal oldRate = new BigDecimal(0);
			BigDecimal newRate = new BigDecimal(0);
			if(p.getCurrentMarketPrice() != null && p.getCurrentMarketPrice().compareTo(new BigDecimal(0)) != 0){
				oldRate = (p.getLastMarketPrice().subtract(p.getLastCostPrice())).multiply(new BigDecimal(100)).divide(p.getLastMarketPrice(),2, RoundingMode.HALF_UP);
				newRate = (p.getCurrentMarketPrice().subtract(p.getActualCostPrice())).multiply(new BigDecimal(100)).divide(p.getCurrentMarketPrice(),2,RoundingMode.HALF_UP);
			}
			log.info("ERPGoodsStockSyncJobHandler cost price send feishu message :{} oldRate:{} newRate:{}", JSON.toJSONString(p), oldRate, newRate);
			if (newRate.compareTo(new BigDecimal(FeiShuMessageConstant.FEI_SHU_COST_PRICE_LT_LIMIT)) <=0
					|| newRate.compareTo(new BigDecimal(FeiShuMessageConstant.FEI_SHU_COST_PRICE_GT_LIMIT)) >=0) {
				log.info("ERPGoodsStockSyncJobHandler cost price send feishu message :{}", JSON.toJSONString(p));
				String content = MessageFormat.format(FeiShuMessageConstant.FEI_SHU_COST_PRICE_NOTIFY, p.getSkuNo(), p.getSkuName(),
						p.getCurrentMarketPrice(), sdf.format(new Date()) ,p.getLastCostPrice(), p.getActualCostPrice(), oldRate, newRate);
				feiShuSendMessageService.sendMessage(content, FeiShuNoticeEnum.COST_PRICE);
			}
		}
	}
//	private void refreshStock(ShopCenterStockSyncResp resp) {
//		Map<String, Integer> goodsStockMap = resp.getGoodsStockMap();
//		Map<String, Integer> goodsInfoStockMap = resp.getGoodsInfoStockMap();
//		// 删除redis缓存
//		for (String goodsId : goodsStockMap.keySet()) {
//			logger.info("ShopCenterSyncController.刷新goods库存.删除redis={}", RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
//			redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
//		}
////		for (String goodsInfoId : goodsInfoStockMap.keySet()) {
////			logger.info("ShopCenterSyncController.刷新goodsInfo库存.删除redis={}", RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId);
////			redisService.delete(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId);
////		}
//
//		// 更新ES
//		if (MapUtils.isNotEmpty(goodsStockMap)) {
//			EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(goodsStockMap).build();
//			esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
//		}
//		if (MapUtils.isNotEmpty(goodsInfoStockMap)) {
//			EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(goodsInfoStockMap).build();
//			esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
//		}
//	}

	/**
	 * 刷新成本价
	 */
	private void refreshCostPrice(ShopCenterCostPriceSyncResp resp) {
		if (CollectionUtils.isEmpty(resp.getGoodsInfoIds())) {
			return;
		}
		// 更新成本价
		EsGoodsInfoAdjustPriceRequest costPrice = new EsGoodsInfoAdjustPriceRequest();
		costPrice.setGoodsInfoIds(resp.getGoodsInfoIds());
		costPrice.setType(PriceAdjustmentType.MARKET);
		esGoodsInfoElasticProvider.adjustPrice(costPrice);
	}
}

