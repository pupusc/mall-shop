package com.soybean.mall.order.prize.service;

import com.alibaba.fastjson.JSON;
import com.soybean.mall.order.prize.model.root.OrderCouponRecord;
import com.soybean.mall.order.prize.repository.OrderCouponRecordRepository;
import com.wanmi.sbc.common.enums.ChannelType;
import com.wanmi.sbc.common.util.StringUtil;
import com.wanmi.sbc.common.util.XssUtils;
import com.wanmi.sbc.goods.bean.enums.GoodsAdAuditStatus;
import com.wanmi.sbc.marketing.api.provider.coupon.CouponCodeProvider;
import com.wanmi.sbc.marketing.api.request.coupon.CouponCodeByCouponIdsRequest;
import com.wanmi.sbc.order.bean.dto.GoodsCouponDTO;
import com.wanmi.sbc.order.bean.enums.PayState;
import com.wanmi.sbc.order.trade.model.entity.TradeItem;
import com.wanmi.sbc.order.trade.model.root.Trade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    @Autowired
    private CouponCodeProvider couponCodeProvider;
    /**
     * 订单支付成功发放优惠券记录表
     */
    public void addCouponRecord(Trade trade){
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

    public void sendCoupon(){
        List<OrderCouponRecord> records = orderCouponRecordRepository.findAll(getWhereCriteria());
        if(CollectionUtils.isEmpty(records)){
            return;
        }
        Map<String,List<OrderCouponRecord>> map = records.stream().collect(Collectors.groupingBy(OrderCouponRecord::getCustomerId));
        map.forEach((customer,list)->{
            CouponCodeByCouponIdsRequest request = new CouponCodeByCouponIdsRequest();
            request.setCustomerId(customer);
            request.setCouponIds(list.stream().map(OrderCouponRecord::getCouponId).collect(Collectors.toList()));
            couponCodeProvider.sendCouponCodeByCouponIds(request);

        });
    }

    public Specification<OrderCouponRecord> getWhereCriteria() {
        return (root, cquery, cbuild) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cbuild.equal(root.get("status"), 0));
            predicates.add(cbuild.equal(root.get("deleted"), 0));
            Predicate[] p = predicates.toArray(new Predicate[predicates.size()]);
            return p.length == 0 ? null : p.length == 1 ? p[0] : cbuild.and(p);
        };
    }

    /**
     * 根据订单商品判断是否下发优惠券
     * @param trade
     * @return
     */
    public Boolean checkSendCoupon(Trade trade){
        List<String> goodsIds = trade.getTradeItems().stream().map(TradeItem::getSpuId).distinct().collect(Collectors.toList());
        String couponStr = Objects.equals(trade.getChannelType(), ChannelType.MINIAPP)?wxGoodsCouponStr:goodsCouponStr;
        if(StringUtils.isEmpty(couponStr)){
            return false;
        }
        List<GoodsCouponDTO> couponList = JSON.parseArray(couponStr, GoodsCouponDTO.class);
        return couponList.stream().anyMatch(p->goodsIds.contains(p.getGoodsId()));
    }
}
