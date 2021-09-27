package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 查询goodsPriceSync表 更新商品价格和es
 **/
@JobHandler(value = "goodsPriceUpdateJobHandler")
@Component
@Slf4j
public class GoodsPriceUpdateJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    //分布式锁名称
    private static final String BATCH_GET_GOODS_PRICE_AND_SYNC_LOCKS = "BATCH_GET_GOODS_PRICE_AND_SYNC_LOCKS";

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_PRICE_AND_SYNC_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("同步商品价格任务执行开始");
        int pageSize = 20;
        try {
            GoodsInfoListByIdRequest goodsInfoListByIdRequest = GoodsInfoListByIdRequest.builder()
                    .pageNum(0)
                    .pageSize(pageSize)
                    .build();

            BaseResponse<List<String>> baseResponse = goodsProvider.syncGoodsPrice(goodsInfoListByIdRequest);
            List<String> result = baseResponse.getContext();
            //更新ES中的SPU和SKU库存价格
            if (CollectionUtils.isNotEmpty(result)) {
                log.info("============Es更新的价格:{}==================", result);
                EsGoodsInfoAdjustPriceRequest esGoodsInfoAdjustPriceRequest = EsGoodsInfoAdjustPriceRequest.builder().goodsInfoIds(result).type(PriceAdjustmentType.MARKET).build();
                esGoodsInfoElasticProvider.adjustPrice(esGoodsInfoAdjustPriceRequest);
            }
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("同步库存定时任务,参数错误", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        } finally {
            //释放锁
            lock.unlock();
        }
    }
}
