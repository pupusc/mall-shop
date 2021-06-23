package com.wanmi.sbc.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.common.constant.MQConstant;
import com.wanmi.sbc.goods.api.provider.pointsgoods.PointsGoodsSaveProvider;
import com.wanmi.sbc.goods.api.request.pointsgoods.PointsGoodsSalesModifyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

/**
 * @ClassName PointsGoodsSalesNumMqConsumerService
 * @Description 积分商品增加销量
 * @Author lvzhenwei
 * @Date 2019/5/29 10:30
 **/
@Service
@EnableBinding(GoodsAboutNumSink.class)
public class PointsGoodsSalesNumMqConsumerService {

    @Autowired
    private PointsGoodsSaveProvider pointsGoodsSaveProvider;

    @StreamListener(MQConstant.POINTS_GOODS_SALES_NUM)
    public void goodsSalesNumMqConsumer(String msg) {
        PointsGoodsSalesModifyRequest request = JSONObject.parseObject(msg, PointsGoodsSalesModifyRequest.class);
        pointsGoodsSaveProvider.updatePointsGoodsSalesNum(request);
    }
}
