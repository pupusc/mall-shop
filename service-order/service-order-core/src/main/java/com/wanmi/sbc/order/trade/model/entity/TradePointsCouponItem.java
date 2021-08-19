package com.wanmi.sbc.order.trade.model.entity;

import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 积分订单优惠券
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TradePointsCouponItem implements Serializable, Cloneable {

    private static final long serialVersionUID = -6856167222779392329L;

    private String oid;
    
    /**
     * 积分兑换的优惠券信息
     */
    private CouponInfoVO couponInfoVO;

}
