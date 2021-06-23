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
public class ERPGoodsStockSyncJobHandler extends IJobHandler{

    @Autowired
    private GoodsProvider goodsProvider;

    @Autowired
    private GoodsInfoQueryProvider goodsInfoQueryProvider;

    @Autowired
    private EsGoodsStockProvider esGoodsStockProvider;

    @Autowired
    private RedissonClient redissonClient;

    //分布式锁名称
    private static final String BATCH_GET_GOODS_STOCK_LOCKS = "BATCH_GET_GOODS_STOCK_LOCKS";

    @Override
    public ReturnT<String> execute(String params) throws Exception {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_LOCKS);
        if (lock.isLocked()) {
            log.error("定时任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        log.info("全量同步ERP商品库存任务执行开始");
        String[] paramterArray = params.split(",");
        int pageSize = 10;
        String skuNo = StringUtils.EMPTY;
        try {
            pageSize = Integer.parseInt(paramterArray[0]);
            if (paramterArray.length>1){
                skuNo = paramterArray[1];
            }
            GoodsInfoCountByConditionRequest goodsInfoCountByConditionRequest = GoodsInfoCountByConditionRequest.builder()
                    .goodsType(GoodsType.VIRTUAL_COUPON.toValue())
                    .delFlag(DeleteFlag.NO.toValue())
                    .auditStatus(CheckStatus.CHECKED).build();
            if(StringUtils.isNotEmpty(skuNo)){
                goodsInfoCountByConditionRequest.setLikeGoodsInfoNo(skuNo);
            }
            BaseResponse<GoodsInfoCountByConditionResponse> goodsInfoCountResponse = goodsInfoQueryProvider.countByCondition(goodsInfoCountByConditionRequest);
            Long count = goodsInfoCountResponse.getContext().getCount();
            log.info("============count==============:{}",count);
            if (count > 0){
                long pageCount = count % pageSize == 0 ? count / pageSize : count / pageSize + 1;
                log.info("============pageCount==============:{}",pageCount);
                for (int pageNum = 0; pageNum < pageCount; pageNum++) {
                    try {
                        GoodsInfoListByIdRequest goodsInfoListByIdRequest = GoodsInfoListByIdRequest.builder()
                                .pageNum(pageNum)
                                .pageSize(pageSize)
                                .build();
                        // 判断skuNo不为空 添加到实体
                        if(!StringUtils.isEmpty(skuNo)){
                            goodsInfoListByIdRequest.setGoodsInfoNo(skuNo);
                        }

                        BaseResponse<Map<String, Map<String, Integer>>> baseResponse = goodsProvider.syncERPStock(goodsInfoListByIdRequest);
                        Map<String, Map<String, Integer>> resultMap = baseResponse.getContext();


                        //更新ES中的SPU和SKU库存数据
                        if (!resultMap.isEmpty()){
                            Map<String, Integer> skusMap = resultMap.get("skus");
                            log.info("============Es更新sku的库存:{}==================",skusMap);
/*                            skusMap.entrySet().stream().forEach(entity->{
                                EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skuId(entity.getKey()).stock(Long.valueOf(entity.getValue())).build();
                                esGoodsStockProvider.resetStockBySkuId(esGoodsSkuStockSubRequest);
                            });*/
                            EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skusMap).build();
                            esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
                            Map<String, Integer> spusMap = resultMap.get("spus");
                            log.info("============Es更新spu的库存:{}==================",spusMap);
                 /*           spusMap.entrySet().stream().forEach(entity->{
                                EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest= EsGoodsSpuStockSubRequest.builder()
                                        .spuId(entity.getKey())
                                        .stock(Long.valueOf(entity.getValue())).build();
                                esGoodsStockProvider.resetStockBySpuId(esGoodsSpuStockSubRequest);
                            });*/
                            EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest= EsGoodsSpuStockSubRequest.builder().spusMap(spusMap).build();
                            esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
                        }
                    }catch (Exception e){
                        //打印异常堆栈信息
                        e.printStackTrace();
                    }
                }
            }
            log.info("全量同步ERP商品库存任务执行结束");
            return SUCCESS;
        } catch (RuntimeException e) {
            log.error("同步ERP库存定时任务,参数错误", e);
            throw new SbcRuntimeException(CommonErrorCode.FAILED);
        }finally {
            //释放锁
            lock.unlock();
        }
    }
}
