package com.wanmi.sbc.order.trade.model.root;

import com.wanmi.sbc.marketing.bean.vo.TradeCouponVO;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午9:14 2018/9/30
 * @Description: 订单组
 */
@Data
public class TradeGroup {

    /**
     * 订单组号
     */
    @Id
    private String id;

    /**
     * 订单组中使用的平台优惠券
     */
    private TradeCouponVO commonCoupon;

    /**
     * 使用平台优惠券的商品集合(已作废的商品)
     */
    private List<String> commonSkuIds = new ArrayList<>();

    /**
     * 平台券是否已退
     */
    private Boolean commonCouponIsReturn = Boolean.FALSE;

}
