package com.wanmi.sbc.order.trade.model.entity;

import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * <p>订单优惠金额</p>
 * Created by of628-wenzhi on 2018-02-26-下午6:19.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Discounts {
    /**
     * 营销类型
     */
    private MarketingType type;

    /**
     * 优惠金额
     */
    private BigDecimal amount;
}
