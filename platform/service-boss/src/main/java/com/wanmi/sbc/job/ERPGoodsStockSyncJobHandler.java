package com.wanmi.sbc.job;

import com.alibaba.fastjson.JSON;
import com.wanmi.sbc.common.base.BaseResponse;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.CommonErrorCode;
import com.wanmi.sbc.elastic.api.provider.goods.EsGoodsStockProvider;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSkuStockSubRequest;
import com.wanmi.sbc.elastic.api.request.goods.EsGoodsSpuStockSubRequest;
import com.wanmi.sbc.goods.api.provider.goods.GoodsProvider;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ReturnT<String> execute(String param) {
        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_LOCKS);
        if (lock.isLocked()) {
            log.info("ERPGoodsStockSyncJobHandler running break");
            return ReturnT.FAIL;
        }
        lock.lock();
        long startTime = System.currentTimeMillis();
        log.info("ERPGoodsStockSyncJobHandler running begin param:{}", param);
        try {
//            boolean updateLastSyncTime = false;
//            Date currentDate = new Date();
//            String startSyncTime;
//            String erpGoodsInfoNo = "";
//            if (StringUtils.isEmpty(param)) {
//                startSyncTime = stringRedisTemplate.opsForValue().get(RedisKeyConstant.STOCK_SYNC_TIME_PREFIX);
//                //默认获取一周前的数据
//                if(StringUtils.isEmpty(startSyncTime)) {
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(new Date());
//                    c.add(Calendar.DATE, - 7);
//                    Date d = c.getTime();
//                    startSyncTime = sdf.format(d);
//                }
//            } else if ("initial".equals(param)) {
//                startSyncTime = "2020-01-01 20:00:00";
//            } else {
//                startSyncTime = "";
//                erpGoodsInfoNo = param;
//            }
            List<String> goodsIdList = new ArrayList<>();
            Integer pageSize = 1000;
            try {
                if (!StringUtils.isEmpty(param)) {
                    String[] split = param.split(",");
                    pageSize = Integer.valueOf(split[0]);
                    if (split.length > 1) {
                        Collections.addAll(goodsIdList, split[1].split("\\|"));
                    }
                }
            } catch (Exception ex) {
                log.error("ERPGoodsStockSyncJobHandler param error ", ex);
            }
            log.info("ERPGoodsStockSyncJobHandler param goodsIdList {} pageSize:{}", JSON.toJSONString(goodsIdList), pageSize);
            goodsProvider.guanYiSyncGoodsStock(goodsIdList, "", pageSize);

            //更新ES库存 TODO

//            Map<String, Map<String, Integer>> context = baseResponse.getContext();
//            if(!baseResponse.getContext().isEmpty()){
//                Integer total = context.get("total").get("total");
//                if(total <= 0) log.info("同步ERP商品库存查询结果为空1");
//                int pageNum = 1;
//                for(int i = 0; i < total; i+=20){
//                    log.info("同步ERP商品库存,共{}条数据,当前第{}页", total, pageNum);
//                    if(pageNum > 1) baseResponse = goodsProvider.partialUpdateStock(erpGoodsInfoNo, lastSyncTime, pageNum + "", "20");
//                    //更新ES中的SPU和SKU库存数据
//                    if(baseResponse.getContext() != null){
//                        Map<String, Integer> skusMap = baseResponse.getContext().get("skus");
//                        if(!skusMap.isEmpty()){
//                            updateLastSyncTime = true;
//                            EsGoodsSkuStockSubRequest esGoodsSkuStockSubRequest = EsGoodsSkuStockSubRequest.builder().skusMap(skusMap).build();
//                            esGoodsStockProvider.batchResetStockBySkuId(esGoodsSkuStockSubRequest);
//                        }
//                        Map<String, Integer> spusMap = baseResponse.getContext().get("spus");
//                        if(!spusMap.isEmpty()){
//                            updateLastSyncTime = true;
//                            EsGoodsSpuStockSubRequest esGoodsSpuStockSubRequest = EsGoodsSpuStockSubRequest.builder().spusMap(spusMap).build();
//                            esGoodsStockProvider.batchResetStockBySpuId(esGoodsSpuStockSubRequest);
//                        }
//                    }
//                    pageNum++;
//                }
//            }else {
//                log.info("同步ERP商品库存查询结果为空2");
//            }

//            //记录更新库存时间
//            if(updateLastSyncTime && (StringUtils.isEmpty(param) || "initial".equals(param))) {
//                stringRedisTemplate.opsForValue().set(RedisKeyConstant.STOCK_SYNC_TIME_PREFIX, sdf.format(currentDate));
//            }
            log.info("ERPGoodsStockSyncJobHandler running end cost: {} ms", (System.currentTimeMillis() - startTime));
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
