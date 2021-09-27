package com.fangdeng.server.job;

import com.fangdeng.server.service.GoodsService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



/**
 * 同步库存
 */
@JobHandler(value = "syncGoodsStockJobHandler")
@Component
@Slf4j
public class SyncGoodsStockJobHandler extends IJobHandler {

    @Autowired
    private GoodsService goodsService;

   // @Autowired
    //private RedissonClient redissonClient;

    //分布式锁名称
    private static final String BATCH_GET_GOODS_STOCK_LOCKS = "BOOK_BATCH_GET_GOODS_STOCK_LOCKS";
    @Override
    public ReturnT<String> execute(String params) throws Exception {
//        RLock lock = redissonClient.getLock(BATCH_GET_GOODS_STOCK_LOCKS);
//        if (lock.isLocked()) {
//            log.error("定时任务在执行中,下次执行.");
//            return null;
//        }
//        lock.lock();
        log.info("全量同步博库商品库存任务执行开始");
        String goodsIds = StringUtils.EMPTY;
        String startTime ="";
        String endTime ="";
        //try{
            if(StringUtils.isNotEmpty(params)){
                String[] paramterArray = params.split(",");
                goodsIds = paramterArray[0];
                //test
                startTime=paramterArray[1];
                endTime=paramterArray[2];
            }
            goodsService.syncGoodsStock(startTime,endTime);
            return SUCCESS;
//        } catch (RuntimeException e) {
//            log.error("同步ERP库存定时任务,参数错误", e);
//            return FAIL;
//        }finally {
//            //释放锁
//            lock.unlock();
//        }
    }
}