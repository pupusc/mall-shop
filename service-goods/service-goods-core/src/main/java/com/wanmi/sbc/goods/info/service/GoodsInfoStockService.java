package com.wanmi.sbc.goods.info.service;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.redis.CacheKeyConstant;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMinusStockByIdRequest;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoMinusStockDTO;
import com.wanmi.sbc.goods.bean.dto.GoodsInfoPlusStockDTO;
import com.wanmi.sbc.goods.info.model.root.GoodsInfo;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import com.wanmi.sbc.goods.mq.GoodsInfoStockSink;
import com.wanmi.sbc.goods.redis.RedisHIncrBean;
import com.wanmi.sbc.goods.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
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
    private RedisService redisService;

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

    /**
     * 更新商品库存
     * @param stock
     * @param goodsInfoId
     */
    public void resetGoodsById(Long stock,String goodsInfoId){
        log.info("初始化/覆盖redis库存开始：skuId={},stock={}", goodsInfoId, stock);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.opsForValue().set(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock.toString());
        log.info("初始化/覆盖redis库存结束：skuId={},stock={}", goodsInfoId, stock);

        //发送mq，更新数据库库存
        log.info("更新redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, stock);
       // GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
      //  goodsInfoStockSink.resetOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
        int updateCount = goodsInfoRepository.resetStockById(stock, goodsInfoId);
        log.info("更新redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, stock);

    }


    /**
     * 根据SKU编号加库存
     *
     * @param stock       库存数
     * @param goodsInfoId SKU编号
     */
    public void addStockById(Long stock, String goodsInfoId) {

        // 检查数据准确
        checkStockCache(goodsInfoId);

        Long count = redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock);
        log.info("增量增加库存结束：skuId={},增加的库存增量:{},增加后的库存:{}", goodsInfoId, stock, count);

        //发送mq，增加数据库库存
        log.info("扣减redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, stock);
        GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
        goodsInfoStockSink.addOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
        log.info("扣减redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, stock);
    }


    /**
     * 根据SKU编号减库存
     *
     * @param stock       库存数
     * @param goodsInfoId SKU编号
     */
    public void subStockById(Long stock, String goodsInfoId) {
        // 检查数据准确
        checkStockCache(goodsInfoId);

        Long count = redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, -stock);
        log.info("增量扣减库存结束：skuId={},扣减的库存增量:{},扣减后的库存:{}", goodsInfoId, stock, count);
        if (count == null || count < 0) {
            log.info("redis中库存不足，返还redis库存开始，skuId={},stock={}...", goodsInfoId, stock);
            //扣到负数时，返还库存。
            redisTemplate.opsForValue().increment(RedisKeyConstant.GOODS_INFO_STOCK_PREFIX + goodsInfoId, stock);
            log.info("redis中库存不足，返还redis库存结束，skuId={},stock={}...", goodsInfoId, stock);
            throw new SbcRuntimeException("k-030301");
        } else {
            log.info("扣减redis库存后，发送mq同步至数据库开始skuId={},stock={}...", goodsInfoId, stock);
            //发送mq，扣减数据库库存
            GoodsInfoMinusStockByIdRequest request = GoodsInfoMinusStockByIdRequest.builder().goodsInfoId(goodsInfoId).stock(stock).build();
            goodsInfoStockSink.subOutput().send(new GenericMessage<>(JSONObject.toJSONString(request)));
            log.info("扣减redis库存后，发送mq同步至数据库结束skuId={},stock={}...", goodsInfoId, stock);
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