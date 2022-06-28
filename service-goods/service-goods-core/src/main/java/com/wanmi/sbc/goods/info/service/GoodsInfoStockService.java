package com.wanmi.sbc.goods.info.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMinusStockByIdRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.bean.enums.AddedFlag;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.info.repository.GoodsRepository;
import com.wanmi.sbc.goods.info.request.ErpGoodsInfoRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoQueryRequest;
import com.wanmi.sbc.goods.info.request.GoodsInfoStockAndCostPriceSyncRequest;
import com.wanmi.sbc.goods.mq.GoodsInfoStockSink;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * sku库存服务
 *
 * @author: hehu
 * @createDate: 2020-03-16 14:35:19
 * @version: 1.0
 */
@Service
@Slf4j
public class GoodsInfoStockService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private GoodsInfoStockSink goodsInfoStockSink;

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 初始化redis缓存
     *
     * @param stock
     * @param goodsInfoId
     */
    public void initCacheStock(Long stock, String goodsInfoId) {
        log.info("初始化/覆盖redis库存开始：skuId={},stock={}", goodsInfoId, stock);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock.toString());
        log.info("初始化/覆盖redis库存结束：skuId={},stock={}", goodsInfoId, stock);
    }

//    /**
//     * 更新商品库存
//     * @param erpStock
//     * @param goodsInfoId
//     */
//    public void resetStockByGoodsInfoId(Long erpStock,String goodsInfoId,Long actualStock){
//        log.info("初始化/覆盖redis库存开始：skuId={},stock={}", goodsInfoId, erpStock);
//        redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, actualStock.toString());
//        redisService.setString(RedisKeyConstant.GOODS_INFO_LAST_STOCK_PREFIX + goodsInfoId, erpStock.toString());
//        log.info("初始化/覆盖redis库存结束：skuId={},stock={}", goodsInfoId, actualStock);
//
//        //发送mq，更新数据库库存
////        log.info("更新redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, actualStock);
//        // GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
//        //  goodsInfoStockSink.resetOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
//        goodsInfoRepository.resetStockById(actualStock, goodsInfoId);
////        log.info("更新redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, actualStock);
//    }
//
//    /**
//     * 更新商品库存
//     * @param erpStock
//     * @param goodsInfoId
//     */
//    public void resetStockByGoodsInfoId(Long erpStock,String goodsInfoId,Long actualStock){
//        log.info("初始化/覆盖redis库存开始：skuId={},stock={}", goodsInfoId, erpStock);
//        redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, actualStock.toString());
//        log.info("初始化/覆盖redis库存结束：skuId={},stock={}", goodsInfoId, actualStock);
//
//        //发送mq，更新数据库库存
////        log.info("更新redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, actualStock);
//        // GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
//        //  goodsInfoStockSink.resetOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
//        goodsInfoRepository.resetStockById(actualStock, goodsInfoId);
////        log.info("更新redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, actualStock);
//    }
//
//
//    /**
//     * 重制库存和成本价
//     * @param goodsInfoId
//     * @param erpStock
//     * @param actualStock
//     * @param erpCostPrice
//     */
//    private void resetStockCostPriceByGoodsInfoId(String goodsInfoId, Long erpStock, Long actualStock, BigDecimal erpCostPrice) {
//        log.info("GoodsInfoStockService resetStockCostPriceByGoodsInfoId skuId {} erpStock: {} actualStock:{} erpCostPrice: {}", goodsInfoId, erpStock, actualStock, erpCostPrice);
//        redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, actualStock.toString());
//        redisService.setString(RedisKeyConstant.GOODS_INFO_LAST_STOCK_PREFIX + goodsInfoId, erpStock.toString());
//        goodsInfoRepository.updateCostPriceAndStockById(erpCostPrice, actualStock, goodsInfoId);
//    }

    /**
     * 更新商品库存
     * @param erpStock
     * @param goodsInfoId
     */
    public void resetStockByGoodsInfoId(Long erpStock,String goodsInfoId,Long actualStock){
        log.info("初始化/覆盖redis库存开始：skuId={},stock={}", goodsInfoId, erpStock);
        redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, actualStock.toString());
        log.info("初始化/覆盖redis库存结束：skuId={},stock={}", goodsInfoId, actualStock);
        goodsInfoRepository.resetStockById(actualStock, goodsInfoId);
    }

    /**
     * 重制库存和成本价
     * @param goodsInfoId
     * @param erpStock
     * @param actualStock
     * @param erpCostPrice
     */
    private void resetStockCostPriceByGoodsInfoId(String goodsInfoId, Long erpStock, Long actualStock, BigDecimal erpCostPrice) {
        log.info("GoodsInfoStockService resetStockCostPriceByGoodsInfoId skuId {} erpStock: {} actualStock:{} erpCostPrice: {}", goodsInfoId, erpStock, actualStock, erpCostPrice);
        redisService.setString(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, actualStock.toString());
        goodsInfoRepository.updateCostPriceAndStockById(erpCostPrice, actualStock, goodsInfoId);
    }


    /**
     * 减少冻结库存
     */
    public void decryFreezeStock(List<GoodsInfoMinusStockDTO> releaseFrozenStockList) {
        for (GoodsInfoMinusStockDTO goodsInfoMinusStockDTO : releaseFrozenStockList) {
            redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_FREEZE_PREFIX + goodsInfoMinusStockDTO.getGoodsInfoId(),
                    -goodsInfoMinusStockDTO.getStock());
        }
    }

    /**
     * 批量更新库存
     */
//    @Transactional
    public List<GoodsInfoStockSyncProviderResponse> batchUpdateGoodsInfoStockAndCostPrice
        (List<GoodsInfoStockAndCostPriceSyncRequest> goodsInfoStockAndCostPriceSyncRequests, Map<String, ErpGoodsInfoRequest> erpSkuCode2ErpGoodsInfoMap){

        List<GoodsInfoStockSyncProviderResponse> result = new ArrayList<>();
        for (GoodsInfoStockAndCostPriceSyncRequest goodsInfoStockAndCostPriceSyncParam : goodsInfoStockAndCostPriceSyncRequests) {
            boolean canSyncStock = goodsInfoStockAndCostPriceSyncParam.getStockSyncFlag().equals(1);
            boolean canSyncCostPrice = Objects.equals(goodsInfoStockAndCostPriceSyncParam.getCostPriceSyncFlag(),1);
            Long tmpActualStockQty = goodsInfoStockAndCostPriceSyncParam.getStockQTY();
            BigDecimal tmpCostPrice = goodsInfoStockAndCostPriceSyncParam.getCostPrice();
            if (canSyncStock || canSyncCostPrice) {
                ErpGoodsInfoRequest erpGoodsInfoRequest = erpSkuCode2ErpGoodsInfoMap.get(goodsInfoStockAndCostPriceSyncParam.getErpGoodsInfoNo());
                Long erpStock = 0L;
                BigDecimal erpCostPrice = BigDecimal.ZERO;
                if (erpGoodsInfoRequest != null) {
                    erpStock = erpGoodsInfoRequest.getErpStock() == null ? 0L : erpGoodsInfoRequest.getErpStock();
                    erpCostPrice = erpGoodsInfoRequest.getErpCostPrice() == null ? BigDecimal.ZERO : erpGoodsInfoRequest.getErpCostPrice();
                }
                //两者都要同步
                if (canSyncStock && canSyncCostPrice) {
                    tmpActualStockQty = this.getActualStock(erpStock, goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId());
                    tmpCostPrice = erpCostPrice;
                    //更新库存和成本价
                    this.resetStockCostPriceByGoodsInfoId(goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId(), erpStock, tmpActualStockQty, erpCostPrice);
                }
                if (canSyncStock && !canSyncCostPrice) {
                    tmpActualStockQty = this.getActualStock(erpStock, goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId());
                    this.resetStockByGoodsInfoId(erpStock, goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId(), tmpActualStockQty);
                }
                if (!canSyncStock && canSyncCostPrice) {
                    if (erpCostPrice.compareTo(goodsInfoStockAndCostPriceSyncParam.getCostPrice()) != 0) {
                        //成本不一致 更新成本价
                        tmpCostPrice = erpCostPrice;
                        goodsInfoRepository.updateCostPriceById(goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId(), erpCostPrice);
                    }
                }
            }

            GoodsInfoStockSyncProviderResponse goodsInfoStockSyncResponse = new GoodsInfoStockSyncProviderResponse();
            goodsInfoStockSyncResponse.setSpuId(goodsInfoStockAndCostPriceSyncParam.getGoodsId());
            goodsInfoStockSyncResponse.setSkuId(goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId());
            goodsInfoStockSyncResponse.setSkuNo(goodsInfoStockAndCostPriceSyncParam.getGoodsInfoNo());
            goodsInfoStockSyncResponse.setSkuName(goodsInfoStockAndCostPriceSyncParam.getGoodsInfoName());
            goodsInfoStockSyncResponse.setActualStockQty(tmpActualStockQty.intValue());
            goodsInfoStockSyncResponse.setCurrentMarketPrice(goodsInfoStockAndCostPriceSyncParam.getMarketPrice());
            goodsInfoStockSyncResponse.setActualCostPrice(tmpCostPrice);

            //存入redis
            String lastSyncStockKey = goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId();
            String lastSyncStockValue = redisService.getHashValue(RedisKeyConstant.GOODS_INFO_SYNC_STOCK_KEY, lastSyncStockKey);
            if (!StringUtils.isBlank(lastSyncStockValue)) {
                goodsInfoStockSyncResponse.setLastStockQty(Integer.parseInt(lastSyncStockValue));
            } else {
                goodsInfoStockSyncResponse.setLastStockQty(0);
            }

            //存入redis
            String lastMarketPriceKey = goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId();
            String lastMarketPriceValue = redisService.getHashValue(RedisKeyConstant.GOODS_INFO_SYNC_MARKET_PRICE_KEY, lastMarketPriceKey);
            if (!StringUtils.isBlank(lastMarketPriceValue)) {
                goodsInfoStockSyncResponse.setLastMarketPrice(new BigDecimal(lastMarketPriceValue));
            } else {
                goodsInfoStockSyncResponse.setLastMarketPrice(goodsInfoStockSyncResponse.getCurrentMarketPrice());
            }


            //存入redis
            String lastSyncCostPriceKey =goodsInfoStockAndCostPriceSyncParam.getGoodsInfoId();
            String lastSyncCostPriceValue = redisService.getHashValue(RedisKeyConstant.GOODS_INFO_SYNC_COST_PRICE_KEY, lastSyncCostPriceKey);
            if (!StringUtils.isBlank(lastSyncCostPriceValue)) {
                goodsInfoStockSyncResponse.setLastCostPrice(new BigDecimal(lastSyncCostPriceValue));
            } else {
                goodsInfoStockSyncResponse.setLastCostPrice(BigDecimal.ZERO);
            }

            if (goodsInfoStockAndCostPriceSyncParam.isHasSaveRedis()) {
                redisService.putHash(RedisKeyConstant.GOODS_INFO_SYNC_STOCK_KEY, lastSyncStockKey, tmpActualStockQty.toString(),6 * 30 * 24 * 60 * 60);
                redisService.putHash(RedisKeyConstant.GOODS_INFO_SYNC_MARKET_PRICE_KEY, lastMarketPriceKey, goodsInfoStockSyncResponse.getCurrentMarketPrice().toString(),6 * 30 * 24 * 60 * 60);
                redisService.putHash(RedisKeyConstant.GOODS_INFO_SYNC_COST_PRICE_KEY, lastSyncCostPriceKey, tmpCostPrice.toString(),6 * 30 * 24 * 60 * 60);
            }
            result.add(goodsInfoStockSyncResponse);
        }
        return result;
    }


    /**
     * 获取当前冻结
     * @param goodsInfoId
     * @return
     */
    public Long getCurrentFreezeStock(String goodsInfoId) {

        Object freezeStockObj = redisTemplate.opsForValue().get(RedisKeyConstant.GOODS_INFO_STOCK_FREEZE_PREFIX + goodsInfoId);
        if (freezeStockObj != null) {
            return Long.parseLong(freezeStockObj.toString());
        }
        return 0L;
    }

    /**
     * 获取真实库存
     * @param currentErpStock
     * @param goodsInfoId
     * @return
     */
    public Long getActualStock(Long currentErpStock,String goodsInfoId){

        Long actualStockQty = null;
        //计算库存
        try {
            Long currentFreezeStock = this.getCurrentFreezeStock(goodsInfoId);
            if (currentFreezeStock <= 0L) {
                actualStockQty = currentErpStock;
            } else {
                actualStockQty = currentErpStock - currentFreezeStock;
            }
        }catch (Exception e){
            log.warn("更新库存失败，stock:{},goodsInfoId:{}",currentErpStock, goodsInfoId, e);
        }
        return actualStockQty;
    }


    /**
     * 根据SKU编号加库存
     *
     * @param stock       库存数
     * @param goodsInfoId SKU编号
     */
    public void addStockById(Long stock, String goodsInfoId) {
        RLock rLock = redissonClient.getFairLock(RedisKeyConstant.GOODS_INFO_STOCK_LOCK_PREFIX + goodsInfoId);
        try {
            // 检查数据准确
            checkStockCache(goodsInfoId);

            if (rLock.tryLock(1, 1, TimeUnit.SECONDS)) {

                Long count = redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock);
                log.info("增量增加库存结束：skuId={},增加的库存增量:{},增加后的库存:{}", goodsInfoId, stock, count);
                //发送mq，增加数据库库存
                log.info("扣减redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, stock);
                GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
                goodsInfoStockSink.addOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
                log.info("扣减redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, stock);
            }
        } catch (Exception ex) {
            log.error("GoodsInfoStockService addStockById goodsInfoId:{} 添加库存异常", goodsInfoId, ex);
        } finally {
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }
    }


    /**
     * 根据SKU编号减库存
     *
     * @param stock       库存数
     * @param goodsInfoId SKU编号
     */
    public void subStockById(Long stock, String goodsInfoId) {

        RLock rLock = redissonClient.getFairLock(RedisKeyConstant.GOODS_INFO_STOCK_LOCK_PREFIX + goodsInfoId);
        try {
            // 检查数据准确
            checkStockCache(goodsInfoId);
            if (rLock.tryLock(1, 1, TimeUnit.SECONDS)) {

                Long count = redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, -stock);
                log.info("增量扣减库存结束：skuId={},扣减的库存增量:{},扣减后的库存:{}", goodsInfoId, stock, count);
                if (count == null || count < 0) {
                    log.info("redis中库存不足，返还redis库存开始，skuId={},stock={}...", goodsInfoId, stock);
                    //扣到负数时，返还库存。
                    redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock); //增加冻结
                    log.info("redis中库存不足，返还redis库存结束，skuId={},stock={}...", goodsInfoId, stock);
                    throw new SbcRuntimeException("k-030301");
                } else {
                    redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_FREEZE_PREFIX + goodsInfoId, stock); //此处要考虑原子性问题
                    log.info("扣减redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, stock);
                    //发送mq，扣减数据库库存
                    GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
                    goodsInfoStockSink.subOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
                    log.info("扣减redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, stock);
                }
            } else {
                throw new SbcRuntimeException("k-030301");
            }
        } catch (Exception e) {
            log.error("GoodsInfoStockService subStockById goodsInfoId:{} 扣减库存异常", goodsInfoId, e);
        } finally {
            if (rLock.isLocked()) {
                rLock.unlock();
            }
        }

    }

    /**
     * 检查缓存中数据准确性并更新
     *
     * @param goodsInfoId skuId
     */
    public Long checkStockCache(String goodsInfoId) {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        // 检查缓存中数据是否准确。
        boolean updateFlag = false;
        Object stockInRedis = redisTemplate.opsForValue().get(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId);
        Long stockNum = 0L;
        if (Objects.isNull(stockInRedis)) {
            log.info("redis中无{}的库存，准备重载redis中库存...", goodsInfoId);
            // 当缓存中无数据时 更新
            updateFlag = true;
        } else {
            stockNum = Long.valueOf((String) stockInRedis);
        }

        if (updateFlag) {
            List<GoodsInfo> byGoodsInfoIds = goodsInfoRepository.findByGoodsInfoIds(Arrays.asList(goodsInfoId));
            if (CollectionUtils.isNotEmpty(byGoodsInfoIds)) {
                stockNum = byGoodsInfoIds.get(0).getStock();
                stockNum = stockNum == null ? 0 : stockNum;
                redisTemplate.opsForValue().set(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stockNum.toString());
                log.info("redis中{}的库存重载完毕，重载后的库存为{}...", goodsInfoId, stockNum);
            } else {
                throw new SbcRuntimeException("商品不存在");
            }
        }
        return stockNum;
    }


    /**
     * 批量加库存
     *
     * @param dtoList 增量库存参数
     */
    public void batchAddStock(List<GoodsInfoPlusStockDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        //缓存是扣库存性缓存，加库存则扣除
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsInfoId(), -g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU, beans);
    }

    /**
     * 批量减库存
     *
     * @param dtoList 减量库存参数
     */
    public void batchSubStock(List<GoodsInfoMinusStockDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return;
        }
        List<RedisHIncrBean> beans = dtoList.stream().map(g -> new RedisHIncrBean(g.getGoodsInfoId(), g.getStock()))
                .collect(Collectors.toList());
        redisService.hincrPipeline(CacheKeyConstant.GOODS_STOCK_SUB_CACHE_SKU, beans);
    }
}
