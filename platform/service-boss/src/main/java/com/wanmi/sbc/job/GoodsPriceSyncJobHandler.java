package com.wanmi.sbc.job;

        import com.wanmi.sbc.common.base.BaseResponse;
        import com.wanmi.sbc.common.constant.RedisKeyConstant;
        import com.wanmi.sbc.common.exception.SbcRuntimeException;
        import com.wanmi.sbc.common.util.CommonErrorCode;
        import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsInfoElasticProvider;
        import com.wanmi.sbc.elastic.api.request.goods.EsGoodsInfoAdjustPriceRequest;
        import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
        import com.wanmi.sbc.goods.api.provider.goods.GoodsQueryProvider;
        import com.wanmi.sbc.goods.api.request.info.GoodsInfoListByIdRequest;
        import com.wanmi.sbc.goods.bean.enums.PriceAdjustmentType;
        import com.wanmi.sbc.redis.RedisService;
        import com.xxl.job.core.biz.model.ReturnT;
        import com.xxl.job.core.handler.IJobHandler;
        import com.xxl.job.core.handler.annotation.JobHandler;
        import lombok.extern.slf4j.Slf4j;
        import org.apache.commons.collections4.CollectionUtils;
        import org.apache.commons.lang3.StringUtils;
        import org.redisson.api.RLock;
        import org.redisson.api.RedissonClient;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Map;


/**
 * 同步成本价
 **/
@JobHandler(value = "goodsPriceSyncJobHandler")
@Component
@Slf4j
public class GoodsPriceSyncJobHandler extends IJobHandler {

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsQueryProvider goodsQueryProvider;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private EsGoodsInfoElasticProvider esGoodsInfoElasticProvider;

    @Autowired
    private RedisService redisService;

    //分布式锁名称
    private static final String BATCH_SYNC_GOODS_INFO_COST_PRICE_LOCKS = "BATCH_SYNC_GOODS_INFO_COST_PRICE_LOCKS";

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        RLock lock = redissonClient.getLock(BATCH_SYNC_GOODS_INFO_COST_PRICE_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("同步商品成本价格任务执行开始");
        int pageSize = 20;
        try {
            GoodsInfoListByIdRequest goodsInfoListByIdRequest = GoodsInfoListByIdRequest.builder()
                    .pageNum(0)
                    .pageSize(pageSize)
                    .build();

            BaseResponse<Map<String,String>> baseResponse = goodsProvider.syncGoodsPrice(goodsInfoListByIdRequest);
            Map<String,String> result = baseResponse.getContext();
            //更新ES中的SPU和SKU库存价格
            if (result !=null && result.size() > 0) {
                log.info("============Es更新的价格:{}==================", result);
                EsGoodsInfoAdjustPriceRequest esGoodsInfoAdjustPriceRequest = EsGoodsInfoAdjustPriceRequest.builder().goodsInfoIds(new ArrayList<>(result.values())).type(PriceAdjustmentType.MARKET).build();
                esGoodsInfoElasticProvider.adjustPrice(esGoodsInfoAdjustPriceRequest);
                //更新redis商品基本数据
                for(String key:result.keySet()){
                    String goodsDetailInfo = redisService.getString(RedisKeyConstant.GOODS_DETAIL_CACHE + key);
                    if (StringUtils.isNotBlank(goodsDetailInfo)) {
                        redisService.delete(RedisKeyConstant.GOODS_DETAIL_CACHE + key);
                    }
                }

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
