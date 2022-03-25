package com.soybean.mall.order.prize.service;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.prize.model.root.OrderCouponRecord;
import com.soybean.mall.order.prize.repository.OrderCouponRecordRepository;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.order.bean.dto.GoodsCouponDTO;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 优惠券记录表
 */
@Service
@Slf4j
public class OrderCouponService {

    @Value("${goods.coupon}")
    private String goodsCouponStr;

    @Value("${mini.program.goods.coupon}")
    private String wxGoodsCouponStr;

    @Autowired
    private OrderCouponRecordRepository orderCouponRecordRepository;
    /**
     * 订单支付成功发放优惠券记录表
     */
    public void sendCoupon(Trade trade){
        if(!Objects.equals(trade.getTradeState().getPayState(), PayState.PAID)){
            return;
        }
        List<String> goodsIds = trade.getTradeItems().stream().map(TradeItem::getSpuId).distinct().collect(Collectors.toList());
        String couponStr = Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)?wxGoodsCouponStr:goodsCouponStr;
        if(StringUtils.isEmpty(couponStr)){
            return;
        }
        List<GoodsCouponDTO> couponList = JSON.parseArray(couponStr, GoodsCouponDTO.class);
        List<GoodsCouponDTO> newCouponList = couponList.stream().filter(p->goodsIds.contains(p.getGoodsId())).collect(Collectors.toList());
        if(CollectionUtils.isEmpty(newCouponList)){
            return;
        }
        List<String> couponCodes = newCouponList.stream().flatMap(coupon -> coupon.getCouponCodes().stream()).distinct().collect(Collectors.toList());
        List<OrderCouponRecord> records = new ArrayList<>(couponCodes.size());
        couponCodes.forEach(coupon->{
            records.add(OrderCouponRecord.builder().couponId(coupon).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now())
                    .customerId(trade.getBuyer().getId()).status(0).deleted(0).orderId(trade.getId()).build());
        });
        orderCouponRecordRepository.saveAll(records);
    }
}
