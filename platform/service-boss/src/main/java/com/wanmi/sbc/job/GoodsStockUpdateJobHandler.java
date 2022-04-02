package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.base.MicroServicePage;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.feishu.constant.FeiShuMessageConstant;
import com.wanmi.sbc.feishu.service.FeiShuSendMessageService;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.api.response.goods.GoodsInfoStockSyncProviderResponse;
import com.wanmi.sbc.redis.RedisService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 查询goodsStockSync表中的库存更新商品库存和es
 **/
@JobHandler(value = "goodsStockUpdateJobHandler")
@Component
@Slf4j
public class GoodsStockUpdateJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private EsGoodsStockProvider esGoodsStockProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private RedisService redisService;

    @Autowired
    private FeiShuSendMessageService feiShuSendMessageService;

    //分布式锁名称
    private static final String BATCH_GET_GOODS_STOCK_AND_SYNC_LOCKS = "BATCH_GET_GOODS_STOCK_AND_SYNC_LOCKS";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    
    @Override
    public ReturnT<String> execute(String params) throws Exception {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_AND_SYNC_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("全量同步商品库存任务执行开始");
        int pageSize = 100;
        try {
            //查询数量
            BaseResponse<Integer> countResponse = goodsQueryProvider.countGoodsStockSync();
            if (countResponse == null || countResponse.getContext() == null || countResponse.getContext() < 1) {
                log.info("全量同步商品库存数量为0");
                return SUCCESS;
            }
            int pageNum = 0;
            for (int i = 0; i < countResponse.getContext(); i += 20) {
                log.info("同步商品库存,共{}条数据,当前第{}页", countResponse.getContext(), pageNum);
                GoodsInfoListByIdRequest goodsInfoListByIdRequest = GoodsInfoListByIdRequest.builder().pageNum(pageNum).pageSize(pageSize).build();
                BaseResponse<List<GoodsInfoStockSyncProviderResponse>> baseResponse = goodsProvider.syncGoodsStock(goodsInfoListByIdRequest);
                List<GoodsInfoStockSyncProviderResponse> result = baseResponse.getContext();
                ++pageNum;
                //更新ES中的SPU和SKU库存数据
                if (!result.isEmpty()) {
                    //博库一个sku只针对一个spu，所以用原来的方式不进行计算
                    Map<String, Integer> skuId2StockQtyMap = new HashMap<>();
                    Map<String, Integer> spuId2StockQtyMap = new HashMap<>();

                    List<GoodsInfoStockSyncProviderResponse> stockSendMessageList = new ArrayList<>();
                    for (GoodsInfoStockSyncProviderResponse goodsInfoStockSyncParam : result) {
                        skuId2StockQtyMap.put(goodsInfoStockSyncParam.getSkuId(), goodsInfoStockSyncParam.getActualStockQty());
                        spuId2StockQtyMap.put(goodsInfoStockSyncParam.getSpuId(), goodsInfoStockSyncParam.getActualStockQty());
                        //发送消息
                        if (goodsInfoStockSyncParam.getIsCalculateStock() && goodsInfoStockSyncParam.getActualStockQty() != null && goodsInfoStockSyncParam.getActualStockQty() <= 30) {
                            stockSendMessageList.add(goodsInfoStockSyncParam);
                        }
                    }

                    log.info("============Es更新sku的库存:{}==================", skuId2StockQtyMap);
                    EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skuId2StockQtyMap).build();
                    esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);

                    log.info("============Es更新spu的库存:{}==================", spuId2StockQtyMap);
                    EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spuId2StockQtyMap).build();
                    esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
                    log.info("============Es更新spu中的goodsInfo的库存:{}==================", spuId2StockQtyMap);
                    esGoodsStockProvider.batchResetGoodsInfoStockBySpuId(esGoodsSpuStockSubRequest);
                    //更新redis商品基本数据
                    if (!skuId2StockQtyMap.isEmpty()) {
                        for (String key : skuId2StockQtyMap.keySet()) {
                            String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + key);
                            if (StringUtils.isNotBlank(goodsDetailInfo)) {
                                redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + key);
                            }
                        }
                    }

                    //发送库存消息
                    for (GoodsInfoStockSyncProviderResponse p : stockSendMessageList) {
                        String content = MessageFormat.format(FeiShuMessageConstant.FEI_SHU_STOCK_NOTIFY, p.getSkuNo(), p.getSkuName(), sdf.format(new Date()) , p.getActualStockQty());
                        feiShuSendMessageService.sendMessage(content);
                    }
                }
            }
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("同步库存定时任务失败", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        } finally {
            //释放锁
            lock.unlock();
        }
    }
}
