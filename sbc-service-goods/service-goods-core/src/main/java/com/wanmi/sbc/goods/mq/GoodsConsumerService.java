package com.wanmi.sbc.goods.mq;

import com.alibaba.fastjson.JSONObject;
import com.wanmi.sbc.goods.api.constant.GoodsJmsDestinationConstants;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoModifyAlreadyGrouponNumRequest;
import com.wanmi.sbc.goods.api.request.groupongoodsinfo.GrouponGoodsInfoModifyStatisticsNumRequest;
import com.wanmi.sbc.goods.groupongoodsinfo.service.GrouponGoodsInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * MQ消费者
 * @author: Geek Wang
 * @createDate: 2019/2/19 9:28
 * @version: 1.0
 */
@Slf4j
@Component
@EnableBinding(GoodsSink.class)
public class GoodsConsumerService {

    @Autowired
    private GrouponGoodsInfoService grouponGoodsInfoService;

    /**
     * 更新已成团人数
     * @param json
     */
    @StreamListener(GoodsJmsDestinationConstants.Q_GROUPON_GOODS_INFO_MODIFY_ALREADY_GROUPON_NUM)
    public void updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(String json) {
        try {
            GrouponGoodsInfoModifyAlreadyGrouponNumRequest request = JSONObject.parseObject(json, GrouponGoodsInfoModifyAlreadyGrouponNumRequest.class);
            int result = grouponGoodsInfoService.updateAlreadyGrouponNumByGrouponActivityIdAndGoodsInfoId(request.getGrouponActivityId(),request.getGoodsInfoIds(),request.getAlreadyGrouponNum());
            log.info("更新已成团人数，参数详细信息：{}，是否成功 ? {}",json,result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新已成团人数，发生异常! param={}", json, e);
        }
    }

    /**
     * 根据活动ID、SKU编号更新商品销售量、订单量、交易额
     * @param json
     */
    @StreamListener(GoodsJmsDestinationConstants.Q_GROUPON_GOODS_INFO_MODIFY_ORDER_PAY_STATISTICS)
    public void updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(String json) {
        try {
            GrouponGoodsInfoModifyStatisticsNumRequest request = JSONObject.parseObject(json, GrouponGoodsInfoModifyStatisticsNumRequest.class);
            int result = grouponGoodsInfoService.updateOrderPayStatisticNumByGrouponActivityIdAndGoodsInfoId(request.getGrouponActivityId(),
                    request.getGoodsInfoId(),request.getGoodsSalesNum(),request.getOrderSalesNum(),request.getTradeAmount());
            log.info("更新已成团人数，参数详细信息：{},是否成功 ? {}",json,result == 0 ? "失败" : "成功");
        } catch (Exception e) {
            log.error("更新已成团人数，发生异常! param={}", json, e);
        }
    }

}
