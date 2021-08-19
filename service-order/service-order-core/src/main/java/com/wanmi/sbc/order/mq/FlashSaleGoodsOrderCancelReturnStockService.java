package com.wanmi.sbc.order.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.order.api.constant.JmsDestinationConstants;
import com.wanmi.sbc.order.api.request.flashsale.FlashSaleGoodsOrderCancelReturnStockRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * @ClassName FlashSaleGoodsOrderCancelReturnStockService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/8/5 15:01
 **/
@Service
public class FlashSaleGoodsOrderCancelReturnStockService {
    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * @Author lvzhenwei
     * @Description 统计商品评价数量mq
     * @Date 14:58 2019/4/12
     * @Param [goodsEvaluate]
     * @return void
     **/
    public void flashSaleGoodsOrderCancelReturnStock(FlashSaleGoodsOrderCancelReturnStockRequest request){
        resolver.resolveDestination(JmsDestinationConstants.Q_FLASH_SALE_GOODS_ORDER_CANCEL_RETURN_STOCK).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }
}
