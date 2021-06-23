package com.wanmi.sbc.order.common;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.goods.api.request.goods.GoodsModifySalesNumRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * @ClassName GoodsSalesNumMq
 * @Description 统计商品销量mq
 * @Author lvzhenwei
 * @Date 2019/4/12 15:09
 **/
@Service
@EnableBinding
public class GoodsSalesNumMq {

    @Autowired
    private BinderAwareChannelResolver resolver;

    public void updateGoodsSalesNumMq(GoodsModifySalesNumRequest request){
        resolver.resolveDestination(MQConstant.GOODS_SALES_NUM).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }
}
