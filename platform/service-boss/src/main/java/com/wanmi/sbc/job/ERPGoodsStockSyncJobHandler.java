package com.wanmi.sbc.job;

import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.enums.DeleteFlag;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.wanmi.sbc.goods.api.provider.info.GoodsInfoQueryProvider;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoCountByConditionRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdsRequest;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoPageRequest;
import com.wanmi.sbc.goods.api.response.info.GoodsInfoCountByConditionResponse;
import com.wanmi.sbc.goods.bean.enums.AuditStatus;
import com.wanmi.sbc.goods.bean.enums.CheckStatus;
import com.wanmi.sbc.goods.bean.enums.GoodsType;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import io.seata.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @program: sbc-background
 * @description: ERP商品库存定时同步
 * @author: 0F3685-wugongjiang
 * @create: 2021-01-28 17:24
 **/
@JobHandler(value = "erpGoodsStockSyncJobHandler")
@Component
@Slf4j
public class ERPGoodsStockSyncJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;
    @Autowired
    private EsGoodsStockProvider esGoodsStockProvider;
    @Autowired
    private RedissonClient redissonClient;

    //分布式锁名称
    private static final String BATCH_GET_GOODS_STOCK_LOCKS = "BATCH_GET_GOODS_STOCK_LOCKS";

    @Override
    public ReturnT<String> execute(String erpGoodsInfoNo) {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_LOCKS);
        if (lock.isLocked()) {
            log.error("全量同步ERP商品库存任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        long startTime = System.currentTimeMillis();
        log.info("全量同步ERP商品库存任务执行开始,参数:{}", erpGoodsInfoNo);
        try {
            BaseResponse<Map<String, Map<String, Integer>>> baseResponse = goodsProvider.partialUpdateStock(erpGoodsInfoNo);
            Map<String, Map<String, Integer>> resultMap = baseResponse.getContext();
            //更新ES中的SPU和SKU库存数据
            if (!resultMap.isEmpty()) {
                Map<String, Integer> skusMap = resultMap.get("skus");
                log.info("============Es更新sku的库存:{}==================", skusMap);
                EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skusMap).build();
                esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
                Map<String, Integer> spusMap = resultMap.get("spus");
                log.info("============Es更新spu的库存:{}==================", spusMap);
                EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spusMap).build();
                esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
            }
            log.info("库存同步ERP任务执行结束,耗时:{}", System.currentTimeMillis() - startTime);
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("同步ERP库存定时任务出错", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        } finally {
            //释放锁
            lock.unlock();
        }
    }
}
