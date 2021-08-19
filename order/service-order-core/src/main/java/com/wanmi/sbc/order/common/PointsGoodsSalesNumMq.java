package com.wanmi.sbc.order.common;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsSalesModifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Service;

/**
 * @ClassName PointsGoodsSalesNumMq
 * @Description 积分商品增加销量
 * @Author lvzhenwei
 * @Date 2019/5/29 10:27
 **/
@Service
@EnableBinding
public class PointsGoodsSalesNumMq {

    @Autowired
    private BinderAwareChannelResolver resolver;

    public void updatePointsGoodsSalesNumMq(PointsGoodsSalesModifyRequest request) {
        resolver.resolveDestination(MQConstant.POINTS_GOODS_SALES_NUM).send(new GenericMessage<>(JSONObject.toJSONString(request)));
    }
}
