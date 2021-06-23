package com.wanmi.sbc.marketing.common.response;

import com.wanmi.sbc.marketing.common.model.entity.TradeMarketing;
import com.wanmi.sbc.marketing.coupon.model.entity.TradeCoupon;
import lombok.Data;

/**
 * @Author: gaomuwei
 * @Date: Created In 上午10:39 2018/10/8
 * @Description:订单营销插件响应类
 */
@Data
public class TradeMarketingResponse {

    /**
     * 满系营销实体
     */
    private TradeMarketing tradeMarketing;

    /**
     * 优惠券实体
     */
    private TradeCoupon tradeCoupon;

}
