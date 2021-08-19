package com.wanmi.sbc.goods.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyEvaluateNumRequest;
import com.wanmi.sbc.goods.goodsevaluate.model.root.GoodsEvaluate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * @ClassName GoodsEvaluateNumMqService
 * @Description TODO
 * @Author lvzhenwei
 * @Date 2019/4/12 14:53
 **/
@Service
public class GoodsEvaluateNumMqService {

    @Autowired
    private BinderAwareChannelResolver resolver;

    /**
     * @Author lvzhenwei
     * @Description 统计商品评价数量mq
     * @Date 14:58 2019/4/12
     * @Param [goodsEvaluate]
     * @return void
     **/
    public void updateGoodsEvaluateNum(GoodsEvaluate goodsEvaluate){
        GoodsModifyEvaluateNumRequest request = new GoodsModifyEvaluateNumRequest();
        request.setGoodsId(goodsEvaluate.getGoodsId());
        request.setEvaluateScore(goodsEvaluate.getEvaluateScore());
        resolver.resolveDestination(MQConstant.GOODS_EVALUATE_NUM).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }
}
