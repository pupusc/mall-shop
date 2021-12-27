package com.wanmi.sbc.job;

import com.sun.corba.se.impl.oa.toa.TOA;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    //分布式锁名称
    private static final String BATCH_GET_GOODS_STOCK_LOCKS = "BATCH_GET_GOODS_STOCK_LOCKS";

    @Override
    public ReturnT<String> execute(String param) {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_LOCKS);
        if (lock.isLocked()) {
            log.error("同步ERP商品库存任务在执行中,下次执行.");
            return null;
        }
        lock.lock();
        long startTime = System.currentTimeMillis();
        log.info("同步ERP商品库存任务执行开始,参数:{}", param);
        try {
            boolean updateLastSyncTime = false;
            Date currentDate = new Date();
            String lastSyncTime;
            String erpGoodsInfoNo = "";
            if("initial".equals(param)){
                lastSyncTime = "2020-01-01 20:00:00";
            }else if(StringUtils.isEmpty(param)) {
                lastSyncTime = stringRedisTemplate.opsForValue().get(RedisKeyConstant.STOCK_SYNC_TIME_PREFIX);
                if(lastSyncTime == null) lastSyncTime = "2021-12-27 20:00:00";
            }else {
                lastSyncTime = "";
                erpGoodsInfoNo = param;
            }
            BaseResponse<Map<String, Map<String, Integer>>> baseResponse = goodsProvider.partialUpdateStock(erpGoodsInfoNo, lastSyncTime, "1", "20");
            Map<String, Map<String, Integer>> context = baseResponse.getContext();
            if(!baseResponse.getContext().isEmpty()){
                Integer total = context.get("total").get("total");
                if(total <= 0) log.info("同步ERP商品库存查询结果为空1");
                int pageNum = 1;
                for(int i = 0; i < total; i+=20){
                    log.info("同步ERP商品库存,共{}条数据,当前第{}页", total, pageNum);
                    if(pageNum > 1) baseResponse = goodsProvider.partialUpdateStock(erpGoodsInfoNo, lastSyncTime, pageNum + "", "20");
                    //更新ES中的SPU和SKU库存数据
                    if(baseResponse.getContext() != null){
                        Map<String, Integer> skusMap = context.get("skus");
                        if(!skusMap.isEmpty()){
                            updateLastSyncTime = true;
                            EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skusMap).build();
                            esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
                        }
                        Map<String, Integer> spusMap = context.get("spus");
                        if(!spusMap.isEmpty()){
                            updateLastSyncTime = true;
                            EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spusMap).build();
                            esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
                        }
                    }
                    pageNum++;
                }
            }else {
                log.info("同步ERP商品库存查询结果为空2");
            }
            if(updateLastSyncTime && (StringUtils.isEmpty(param) || "initial".equals(param))) stringRedisTemplate.opsForValue().set(RedisKeyConstant.STOCK_SYNC_TIME_PREFIX, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(currentDate));
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
