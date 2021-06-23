package com.wanmi.sbc.goods.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.exception.SbcRuntimeException;
import com.wanmi.sbc.goods.api.constant.GoodsInfoJmsDestinationConstants;
import com.wanmi.sbc.goods.api.constant.GoodsStockErrorCode;
import com.wanmi.sbc.goods.api.request.info.GoodsInfoMinusStockByIdRequest;
import com.wanmi.sbc.goods.info.repository.GoodsInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * sku库存MQ消费者
 * @author: hehu
 * @createDate: 2020-03-16 14:35:19
 * @version: 1.0
 */
@Slf4j
@Component
@EnableBinding(GoodsInfoStockSink.class)
public class GoodsInfoStockConsumerService {

    @Autowired
    private GoodsInfoRepository goodsInfoRepository;


    /**
     * 扣减商品库存
     * @param json
     */
    @StreamListener(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_SUB_INPUT)
    @Transactional(rollbackOn = Exception.class)
    public void subGoodsInfoStock(String json) {
        try {
            GoodsInfoMinusStockByIdRequest request = JSONObject.parseObject(json, GoodsInfoMinusStockByIdRequest.class);
            int updateCount = goodsInfoRepository.subStockById(request.getStock(), request.getGoodsInfoId());

            if(updateCount<=0){
                throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
            }
            log.info("扣减数据库库存，是否成功 ? {},param={}",updateCount == 0 ? "失败" : "成功",json);
        } catch (Exception e) {
            log.error("扣减库存，发生异常! param={}", json, e);
        }
    }

    /**
     * 增加商品库存
     * @param json
     */
    @StreamListener(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_ADD_INPUT)
    @Transactional(rollbackOn = Exception.class)
    public void addGoodsInfoStock(String json) {
        try {
            GoodsInfoMinusStockByIdRequest request = JSONObject.parseObject(json, GoodsInfoMinusStockByIdRequest.class);
            int updateCount = goodsInfoRepository.addStockById(request.getStock(), request.getGoodsInfoId());

            if(updateCount<=0){
                throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
            }
            log.info("增加库存，是否成功 ? {},param={}",updateCount == 0 ? "失败" : "成功",json);
        } catch (Exception e) {
            log.error("增加库存，发生异常! param={}", json, e);
        }
    }

    /**
     * 覆盖商品库存
     * @param json
     */
    @StreamListener(GoodsInfoJmsDestinationConstants.GOODS_INFO_STOCK_RESET_INPUT)
    @Transactional(rollbackOn = Exception.class)
    public void resetGoodsInfoStock(String json){
        try {
            GoodsInfoMinusStockByIdRequest request = JSONObject.parseObject(json, GoodsInfoMinusStockByIdRequest.class);
            int updateCount = goodsInfoRepository.resetStockById(request.getStock(), request.getGoodsInfoId());

            if(updateCount<=0){
                throw new SbcRuntimeException(GoodsStockErrorCode.LACK_ERROR);
            }
            log.info("重置库存，是否成功 ? {},param={}",updateCount == 0 ? "失败" : "成功",json);
        } catch (Exception e) {
            log.error("重置库存，发生异常! param={}", json, e);
        }
    }
}
