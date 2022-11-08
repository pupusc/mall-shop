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
import com.wanmi.sbc.goods.api.provider.freight.FreightTemplateGoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.ShopCenterGoodsProvider;
import com.wanmi.sbc.goods.api.request.freight.FreightTemplate49ChangeReq;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncCostPriceReq;
import com.wanmi.sbc.goods.api.request.shopcentersync.ShopCenterSyncStockReq;
import com.wanmi.sbc.goods.api.response.goods.ShopCenterCostPriceSyncResp;
import com.wanmi.sbc.goods.api.response.goods.ShopCenterStockSyncResp;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.wanmi.sbc.order.api.provider.shopcenter.ShopCenterOrdOrderProvider;
import com.wanmi.sbc.order.api.req.ShopCenterSyncDeliveryReq;
import com.wanmi.sbc.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;


@Slf4j
@RestController
@RequestMapping("/shopCenterSync")
public class ShopCenterSyncController {
	private static final Logger logger = LoggerFactory.getLogger(ShopCenterSyncController.class);
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

	@PostMapping("/syncData")
	public ShopCenterSyncResponse<Boolean> syncData(@RequestBody SyncDataReq request) {
		logger.info("ShopCenterSyncController.syncData.request={}", request);
		Integer tag = request.getTag();
		String data = request.getData();
		String requestId = request.getRequestId();
		if (StringUtils.isEmpty(data)) {
			logger.warn("电商中台推送同步.data为空，结束！requestId={}", requestId);
			return ShopCenterSyncResponse.success(false);
		}
		if (Objects.isNull(tag)) {
			logger.warn("电商中台推送同步.tag为空，结束！requestId={}", requestId);
			return ShopCenterSyncResponse.success(false);
		}

		try {
			switch (tag) {
				// 库存变动
				case 1001:
					ShopCenterSyncStockReq syncStock = JSON.parseObject(data, ShopCenterSyncStockReq.class);
					BaseResponse<ShopCenterStockSyncResp> stockResp = shopCenterGoodsProvider.shopCenterSyncGoodsStock(syncStock);
					refreshStock(stockResp.getContext());
					break;
				// 发货
				case 1002:
					ShopCenterSyncDeliveryReq syncDelivery = JSON.parseObject(data, ShopCenterSyncDeliveryReq.class);
					shopCenterOrdOrderProvider.shopCenterSyncDelivery(syncDelivery);
					break;
				// 成本变动
				case 1004:
					ShopCenterSyncCostPriceReq syncPrice = JSON.parseObject(data, ShopCenterSyncCostPriceReq.class);
					BaseResponse<ShopCenterCostPriceSyncResp> priceResp = shopCenterGoodsProvider.shopCenterSyncCostPrice(syncPrice);
					refreshCostPrice(priceResp.getContext());
					if (!CollectionUtils.isEmpty(priceResp.getContext().getGoodsIds())) {
						FreightTemplate49ChangeReq freightTemplate49ChangeReq = new FreightTemplate49ChangeReq();
						freightTemplate49ChangeReq.setSpuIds(priceResp.getContext().getGoodsIds());
						freightTemplateGoodsProvider.changeFreeDelivery49(freightTemplate49ChangeReq);
					}
					break;
				default:
					logger.warn("电商中台推送同步，未知的数据类型，tag={}!", tag);
					return ShopCenterSyncResponse.success(false);
			}

			return ShopCenterSyncResponse.success(true);
		} catch (Exception e) {
			logger.warn("电商中台推送同步.异常，结束！requestId={}", requestId, e);
			return ShopCenterSyncResponse.success(false);
		}
	}

	/**
	 * 刷新库存
	 */
	private void refreshStock(ShopCenterStockSyncResp resp) {
		Map<String, Integer> goodsStockMap = resp.getGoodsStockMap();
		Map<String, Integer> goodsInfoStockMap = resp.getGoodsInfoStockMap();
		// 删除redis缓存
		for (String goodsId : goodsStockMap.keySet()) {
			logger.info("ShopCenterSyncController.刷新goods库存.删除redis={}", RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
			redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsId);
		}
		for (String goodsInfoId : goodsInfoStockMap.keySet()) {
			logger.info("ShopCenterSyncController.刷新goodsInfo库存.删除redis={}", RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId);
			redisService.delete(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId);
		}

		// 更新ES
		if (MapUtils.isNotEmpty(goodsStockMap)) {
			EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(goodsStockMap).build();
			esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
		}
		if (MapUtils.isNotEmpty(goodsInfoStockMap)) {
			EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(goodsInfoStockMap).build();
			esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
		}
	}

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

