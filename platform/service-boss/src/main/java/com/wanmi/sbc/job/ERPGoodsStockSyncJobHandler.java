//package com.wanmi.sbc.job;
//import com.google.common.collect.Lists;
//
//import com.alibaba.fastjson.JSON;
//import com.wanmi.sbc.common.base.BaseResponse;
//import com.wanmi.sbc.common.constant.RedisKeyConstant;
//import com.wanmi.sbc.common.exception.SbcRuntimeException;
//import com.wanmi.sbc.common.util.CommonErrorCode;
//import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
//import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
//import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
//import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
//import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
//import com.wanmi.sbc.feishu.FeiShuNoticeEnum;
//import com.wanmi.sbc.feishu.constant.FeiShuMessageConstant;
//import com.wanmi.sbc.feishu.service.FeiShuSendMessageService;
//import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
//import com.wanmi.sbc.goods.api.request.goodsstock.GuanYiSyncGoodsStockRequest;
//import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncMaxIdProviderResponse;
//import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
//import com.wanmi.sbc.goods.bean.dto.GoodsInfoPriceChangeDTO;
//import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
//import com.wanmi.sbc.redis.RedisService;
//import com.xxl.job.core.biz.model.ReturnT;
//import com.xxl.job.core.handler.IJobHandler;
//import com.xxl.job.core.handler.annotation.JobHandler;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.redisson.api.RLock;
//import org.redisson.api.RedissonClient;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.math.BigDecimal;
//import java.math.RoundingMode;
//import java.text.MessageFormat;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * @program: sbc-background
// * @description: ERP商品库存定时同步
// * @author: 0F3685-wugongjiang
// * @create: 2021-01-28 17:24
// **/
//@JobHandler(value = "erpGoodsStockSyncJobHandler")
//@Component
//@Slf4j
//public class ERPGoodsStockSyncJobHandler extends IJobHandler {
//
//    @Autowired
//    private GoodsProvider goodsProvider;
//
//    @Autowired
//    private EsGoodsStockProvider esGoodsStockProvider;
//
//    @Autowired
//    private RedissonClient redissonClient;
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//
//    @Autowired
//    private RedisService redisService;
//
//    @Autowired
//    private FeiShuSendMessageService feiShuSendMessageService;
//
//    @Autowired
//    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;
//
//    //分布式锁名称
//    private static final String BATCH_GET_GOODS_STOCK_LOCKS = "BATCH_GET_GOODS_STOCK_LOCKS";
//
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    @Override
//    public ReturnT<String> execute(String param) {
//        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_LOCKS);
//        if (lock.isLocked()) {
//            log.info("ERPGoodsStockSyncJobHandler running break");
//            return ReturnT.FAIL;
//        }
//        lock.lock();
//        long startTime = System.currentTimeMillis();
//        log.info("ERPGoodsStockSyncJobHandler running begin param:{}", param);
//        try {
//            List<String> goodsIdList = new ArrayList<>();
//            Integer pageSize = 80;
//            Long maxTmpId = -1L;
//            try {
//                if (!StringUtils.isEmpty(param)) {
//                    String[] split = param.split(",");
//                    maxTmpId = Long.parseLong(split[0]);
//                    pageSize = Integer.valueOf(split[1]);
//                    if (split.length > 2) {
//                        Collections.addAll(goodsIdList, split[2].split("\\|"));
//                    }
//                }
//            } catch (Exception ex) {
//                log.error("ERPGoodsStockSyncJobHandler param error ", ex);
//            }
//            if (maxTmpId == -1) {
//                String maxTmpIdRedis = stringRedisTemplate.opsForValue().get(RedisKeyConstant.GOODS_STOCK_SYNC_MAX_TMP_ID);
//                if (!StringUtils.isEmpty(maxTmpIdRedis)) {
//                    maxTmpId = Long.parseLong(maxTmpIdRedis);
//                }
//            }
//
//
//            log.info("ERPGoodsStockSyncJobHandler param goodsIdList {} pageSize:{} maxId: {}", JSON.toJSONString(goodsIdList), pageSize, maxTmpId);
//            GuanYiSyncGoodsStockRequest guanYiSyncGoodsStockRequest = new GuanYiSyncGoodsStockRequest();
//            guanYiSyncGoodsStockRequest.setGoodsIdList(goodsIdList);
//            guanYiSyncGoodsStockRequest.setHasSaveRedis(true);
//            guanYiSyncGoodsStockRequest.setMaxTmpId(maxTmpId);
//            guanYiSyncGoodsStockRequest.setPageSize(pageSize);
//
//            BaseResponse<GoodsInfoStockSyncMaxIdProviderResponse> result = goodsProvider.guanYiSyncGoodsStock(guanYiSyncGoodsStockRequest);
//            GoodsInfoStockSyncMaxIdProviderResponse context = result.getContext();
//            if (!CollectionUtils.isEmpty(context.getGoodsInfoStockSyncList())) {
//
//                Map<String, Integer> skuId2StockQtySumMap = new HashMap<>();
//                Map<String, Integer> spuId2StockQtySumMap = new HashMap<>();
//                List<GoodsInfoStockSyncProviderResponse> stockSendMessageList = new ArrayList<>();
//                List<GoodsInfoStockSyncProviderResponse> costPriceSendMessageList = new ArrayList<>();
//                for (GoodsInfoStockSyncProviderResponse goodsInfoStockSyncParam : context.getGoodsInfoStockSyncList()) {
//
//                    //删除缓存
//                    String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsInfoStockSyncParam.getSpuId());
//                    if (StringUtils.isNotBlank(goodsDetailInfo)) {
//                        redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + goodsInfoStockSyncParam.getSpuId());
//                    }
//
//                    //计算skuCode 数量
//                    Integer stockSumQtyTmp = skuId2StockQtySumMap.get(goodsInfoStockSyncParam.getSkuId());
//                    stockSumQtyTmp = stockSumQtyTmp == null ? goodsInfoStockSyncParam.getActualStockQty() : stockSumQtyTmp + goodsInfoStockSyncParam.getActualStockQty();
//                    skuId2StockQtySumMap.put(goodsInfoStockSyncParam.getSkuId(), stockSumQtyTmp);
//
//                    //计算spuCode 数量
//                    stockSumQtyTmp = spuId2StockQtySumMap.get(goodsInfoStockSyncParam.getSpuId());
//                    stockSumQtyTmp = stockSumQtyTmp == null ? goodsInfoStockSyncParam.getActualStockQty() : stockSumQtyTmp + goodsInfoStockSyncParam.getActualStockQty();
//                    spuId2StockQtySumMap.put(goodsInfoStockSyncParam.getSpuId(), stockSumQtyTmp);
//
//                    //发送消息
//                    if (goodsInfoStockSyncParam.getActualStockQty() != null && !Objects.equals(goodsInfoStockSyncParam.getActualStockQty(), goodsInfoStockSyncParam.getLastStockQty())) {
//                        stockSendMessageList.add(goodsInfoStockSyncParam);
//                    }
//
//                    //只是处理需要同步 成本价的商品
//                    if (goodsInfoStockSyncParam.getActualCostPrice().compareTo(goodsInfoStockSyncParam.getLastCostPrice()) != 0
//                            || goodsInfoStockSyncParam.getCurrentMarketPrice().compareTo(goodsInfoStockSyncParam.getLastMarketPrice()) != 0) {
//                        costPriceSendMessageList.add(goodsInfoStockSyncParam);
//                    }
//                }
//
//                if (!skuId2StockQtySumMap.isEmpty()) {
//                    //更新库存
//                    EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skuId2StockQtySumMap).build();
//                    esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
//
//                    EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spuId2StockQtySumMap).build();
//                    esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
//                }
//
//                if (CollectionUtils.isNotEmpty(costPriceSendMessageList)) {
//                    List<String> skuIdCostPriceList = new ArrayList<>();
//                    for (GoodsInfoStockSyncProviderResponse goodsInfoStockSyncParam : costPriceSendMessageList) {
//                        if (goodsInfoStockSyncParam.getActualCostPrice().compareTo(goodsInfoStockSyncParam.getLastCostPrice()) == 0){
//                            continue;
//                        }
//                        skuIdCostPriceList.add(goodsInfoStockSyncParam.getSkuId());
//                    }
//                    if (!CollectionUtils.isEmpty(skuIdCostPriceList)) {
//                        //更新成本价
//                        EsGoodsInfoAdjustPriceRequest costPrice = new EsGoodsInfoAdjustPriceRequest();
//                        costPrice.setGoodsInfoIds(skuIdCostPriceList);
//                        costPrice.setType(PriceAdjustmentType.MARKET);
//                        esGoodsInfoElasticProvider.adjustPrice(costPrice);
//                    }
//                }
//
//                //发送库存消息
//                for (GoodsInfoStockSyncProviderResponse p : stockSendMessageList) {
//
//                    if (p.getActualStockQty() > FeiShuMessageConstant.FEI_SHU_STOCK_LIMIT) {
//                        continue;
//                    }
//                    log.info("ERPGoodsStockSyncJobHandler stock send feishu message :{}", JSON.toJSONString(p));
//                    String content = MessageFormat.format(FeiShuMessageConstant.FEI_SHU_STOCK_NOTIFY, p.getSkuNo(), p.getSkuName(), sdf.format(new Date()) , p.getActualStockQty());
//                    feiShuSendMessageService.sendMessage(content, FeiShuNoticeEnum.STOCK);
//                }
//
//                //发送成本价消息
//                for (GoodsInfoStockSyncProviderResponse p : costPriceSendMessageList) {
//                    //计算毛利率
//
//                    BigDecimal oldRate = new BigDecimal(0);
//                    BigDecimal newRate = new BigDecimal(0);
//                    if(p.getCurrentMarketPrice() != null && p.getCurrentMarketPrice().compareTo(new BigDecimal(0)) != 0){
//                        oldRate = (p.getLastMarketPrice().subtract(p.getLastCostPrice())).multiply(new BigDecimal(100)).divide(p.getLastMarketPrice(),2, RoundingMode.HALF_UP);
//                        newRate = (p.getCurrentMarketPrice().subtract(p.getActualCostPrice())).multiply(new BigDecimal(100)).divide(p.getCurrentMarketPrice(),2,RoundingMode.HALF_UP);
//                    }
//                    log.info("ERPGoodsStockSyncJobHandler cost price send feishu message :{} oldRate:{} newRate:{}", JSON.toJSONString(p), oldRate, newRate);
//                    if (newRate.compareTo(new BigDecimal(FeiShuMessageConstant.FEI_SHU_COST_PRICE_LT_LIMIT)) <=0
//                            || newRate.compareTo(new BigDecimal(FeiShuMessageConstant.FEI_SHU_COST_PRICE_GT_LIMIT)) >=0) {
//                        log.info("ERPGoodsStockSyncJobHandler cost price send feishu message :{}", JSON.toJSONString(p));
//                        String content = MessageFormat.format(FeiShuMessageConstant.FEI_SHU_COST_PRICE_NOTIFY, p.getSkuNo(), p.getSkuName(),
//                                p.getCurrentMarketPrice(), sdf.format(new Date()) ,p.getLastCostPrice(), p.getActualCostPrice(), oldRate, newRate);
//                        feiShuSendMessageService.sendMessage(content, FeiShuNoticeEnum.COST_PRICE);
//                    }
//                }
//            }
//            log.info("ERPGoodsStockSyncJobHandler result maxTmpId: {}, content.MaxTmpId: {}", maxTmpId, context.getMaxTmpId());
//            if (maxTmpId == context.getMaxTmpId() && context.getMaxTmpId() > 0) {
//                stringRedisTemplate.opsForValue().set(RedisKeyConstant.GOODS_STOCK_SYNC_MAX_TMP_ID, "0");
//            } else {
//                stringRedisTemplate.opsForValue().set(RedisKeyConstant.GOODS_STOCK_SYNC_MAX_TMP_ID, String.valueOf(context.getMaxTmpId()));
//            }
//
//            log.info("ERPGoodsStockSyncJobHandler running end cost: {} ms", (System.currentTimeMillis() - startTime));
//            return SUCCESS;
//        } catch (RuntimeException e) {
//            log.error("同步ERP库存定时任务出错", e);
//            throw new SbcRuntimeException(CommonErrorCode.FAILED);
//        } finally {
//            //释放锁
//            lock.unlock();
//        }
//    }
//}
