package com.wanmi.sbc.goods.info.service;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.goods.api.response.goods.ShopCenterCostPriceSyncResp;
import com.wanmi.sbc.goods.api.response.goods.ShopCenterStockSyncResp;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.redis.RedisService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ShopCenterGoodsStockService {
	private static final Logger logger = LoggerFactory.getLogger(ShopCenterGoodsStockService.class);
	@Autowired
	private GoodsRepository goodsRepository;
	@Autowired
	private GoodsInfoRepository goodsInfoRepository;
	@Autowired
	private RedisService redisService;

	/**
	 * 更新库存
	 */
	public BaseResponse<ShopCenterStockSyncResp> updateStock(String goodsCode, Integer quantity) {
		Map<String, Integer> goodsStockMap = new HashMap<>();
		Map<String, Integer> goodsInfoStockMap = new HashMap<>();
		ShopCenterStockSyncResp resp = new ShopCenterStockSyncResp(goodsStockMap, goodsInfoStockMap);

		if (StringUtils.isEmpty(goodsCode) || Objects.isNull(quantity)) {
			return BaseResponse.success(resp);
		}
		List<GoodsInfo> infoList = goodsInfoRepository.findByErpGoodsInfoNoAndDelFlag(goodsCode, DeleteFlag.NO);
		if (CollectionUtils.isEmpty(infoList)) {
			return BaseResponse.success(resp);
		}
		Integer syncStock = 1;
		infoList = infoList.stream().filter(g -> {
			if (!syncStock.equals(g.getStockSyncFlag())) {
				logger.info("ShopCenterGoodsStockService.过滤.未开启库存同步.goodsCode={},goodsInfoId={}", g.getErpGoodsInfoNo(), g.getGoodsInfoId());
				return false;
			}
			return true;
		}).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(infoList)) {
			return BaseResponse.success(resp);
		}

		// 修改goodsInfo 库存
		for (GoodsInfo info : infoList) {
			redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + info.getGoodsInfoId(), quantity.toString());
			goodsInfoStockMap.put(info.getGoodsInfoId(), quantity);
		}
		logger.info("ShopCenterGoodsStockService.goodsInfo修改库存.quantity={},ids={}", quantity, goodsInfoStockMap.keySet());
		goodsInfoRepository.updateStockByIds(goodsInfoStockMap.keySet(), quantity.longValue());

		// 修改goods 库存
		List<String> goodsIds = infoList.stream().map(GoodsInfo::getGoodsId).collect(Collectors.toList());
		List<GoodsInfo> allGoodsInfo = goodsInfoRepository.findByGoodsIds(goodsIds);
		// 统计总库存
		for (GoodsInfo info : allGoodsInfo) {
			Integer stockNum = goodsStockMap.getOrDefault(info.getGoodsId(), 0);
			if (goodsInfoStockMap.containsKey(info.getGoodsInfoId())) {
				stockNum += quantity;
			} else {
				stockNum += (Objects.nonNull(info.getStock()) ? info.getStock().intValue() : 0);
			}
			goodsStockMap.put(info.getGoodsId(), stockNum);
		}
		for (Map.Entry<String, Integer> entry : goodsStockMap.entrySet()) {
			logger.info("ShopCenterGoodsStockService.goods修改库存.quantity={},id={}", entry.getValue(), entry.getKey());
			goodsRepository.updateStockByGoodsId(entry.getValue().longValue(), entry.getKey());
		}

		return BaseResponse.success(resp);
	}

	public BaseResponse<ShopCenterCostPriceSyncResp> updateCostPrice(String goodsCode, Integer costPrice) {
		ShopCenterCostPriceSyncResp resp = new ShopCenterCostPriceSyncResp();

		if (StringUtils.isEmpty(goodsCode) || Objects.isNull(costPrice)) {
			return BaseResponse.success(resp);
		}
		List<GoodsInfo> infoList = goodsInfoRepository.findByErpGoodsInfoNoAndDelFlag(goodsCode, DeleteFlag.NO);
		if (CollectionUtils.isEmpty(infoList)) {
			return BaseResponse.success(resp);
		}
		Integer syncPrice = 1;
		infoList = infoList.stream().filter(g -> {
			if (!syncPrice.equals(g.getCostPriceSyncFlag())) {
				logger.info("ShopCenterGoodsStockService.过滤.未开启价格同步.goodsCode={},goodsInfoId={}", g.getErpGoodsInfoNo(), g.getGoodsInfoId());
				return false;
			}
			return true;
		}).collect(Collectors.toList());
		if (CollectionUtils.isEmpty(infoList)) {
			return BaseResponse.success(resp);
		}

		BigDecimal price = costPrice == 0 ? new BigDecimal("0")
				: new BigDecimal(costPrice).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_DOWN);
		List<String> goodsInfoIds = infoList.stream().map(GoodsInfo::getGoodsInfoId).collect(Collectors.toList());

		// 更新成本价
		goodsInfoRepository.updateCostPriceByIds(goodsInfoIds, price);
		logger.info("ShopCenterGoodsStockService.goodsInfo修改价格.price={},ids={}", price, goodsInfoIds);
		resp.setGoodsInfoIds(goodsInfoIds);
		return BaseResponse.success(resp);
	}
}
