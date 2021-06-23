package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.ErrorCodeConstant;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.common.constant.RedisKeyConstant;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.common.util.Constants;
import com.wanmi.sbc.flashsale.request.RushToBuyFlashSaleGoodsRequest;
import com.wanmi.sbc.flashsale.request.RushToBuyFlashSaleOrderNumRequest;
import com.wanmi.sbc.flashsale.service.FlashSaleService;
import com.wanmi.sbc.goods.bean.vo.FlashSaleGoodsVO;
import com.wanmi.sbc.redis.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * @ClassName FlashSaleOrderCancleReturnStockListener
 * @Description 秒杀商品抢购资格失效还库存MQ
 * @Author lvzhenwei
 * @Date 2019/6/12 10:00
 **/
@Slf4j
@Component
public class FlashSaleOrderCancelReturnStockListener {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private FlashSaleService flashSaleService;

    @Autowired
    private RedisService redisService;

    /**
     * @return void
     * @Author lvzhenwei
     * @Description 秒杀商品抢购资格失效还库存MQ消费方法
     * @Date 18:58 2019/6/17
     * @Param [message]
     **/
    @StreamListener(MQConstant.FLASH_SALE_ORDER_CANCEL_RETURN_STOCK_TOPIC_INPUT)
    public void flashSaleOrderCancelReturnStockReceive(String message) {
        Boolean orderType = true;
        RushToBuyFlashSaleGoodsRequest request = JSONObject.parseObject(message, RushToBuyFlashSaleGoodsRequest.class);
        String flashSaleOrderInfo = redisService.getString(RedisKeyConstant.FLASH_SALE_GOODS_QUALIFICATIONS +
                request.getCustomerId() + request.getFlashSaleGoodsId() + "flashSaleOrder");
        if(StringUtils.isNotBlank(flashSaleOrderInfo)){
            RushToBuyFlashSaleOrderNumRequest rushToBuyFlashSaleOrderNumRequest = JSONObject.parseObject(flashSaleOrderInfo, RushToBuyFlashSaleOrderNumRequest.class);
            List<Integer> flashSaleNumList = rushToBuyFlashSaleOrderNumRequest.getFlashSaleNumList();
            if (flashSaleNumList != null && flashSaleNumList.size() > 0) {
                for(Integer flashSaleNum:flashSaleNumList){
                    if(flashSaleNum.equals(request.getFlashSaleNum())){
                        orderType = false;
                    }
                };
            }
        }
        //如果该次抢购商品对应会员的已下单，则不执行还库存操作，否则执行
        if (orderType) {
            //增加库存（加锁，保证高并发下库存是正确的）
            RLock rLock = redissonClient.getFairLock(RedisKeyConstant.FLASH_SALE_GOODS_INFO_STOCK + request.getFlashSaleGoodsId());
            rLock.lock();
            try {
                //查询现在库存
                FlashSaleGoodsVO flashSaleGoodsVO = flashSaleService.getFlashSaleGoodsInfoForRedis(request);
                if (Objects.nonNull(flashSaleGoodsVO)) {
                    //增加库存
                    flashSaleGoodsVO.setStock(flashSaleGoodsVO.getStock() + request.getFlashSaleGoodsNum());
                }
                //更新原来库存
                String flashSaleGoodsInfoKey = RedisKeyConstant.FLASH_SALE_GOODS_INFO + request.getFlashSaleGoodsId();
                redisService.setObj(flashSaleGoodsInfoKey, flashSaleGoodsVO, Constants.FLASH_SALE_LAST_HOUR * 60 * 60);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rLock.unlock();
            }
        }
    }
}
