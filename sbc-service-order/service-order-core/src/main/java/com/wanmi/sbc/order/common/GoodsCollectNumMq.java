package com.wanmi.sbc.order.common;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifyCollectNumRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * @ClassName GoodsCollectNumMq
 * @Description 商品收藏量mq
 * @Author lvzhenwei
 * @Date 2019/4/12 10:00
 **/
@Service
@EnableBinding
public class GoodsCollectNumMq {

    @Autowired
    private BinderAwareChannelResolver resolver;

    public void updateGoodsCollectNum(GoodsModifyCollectNumRequest goodsModifyCollectNumRequest){
        resolver.resolveDestination(MQConstant.GOODS_COLLECT_NUM).send(new GenericMessage<>(JSONObject.toJSONString(goodsModifyCollectNumRequest)));
    }
}
