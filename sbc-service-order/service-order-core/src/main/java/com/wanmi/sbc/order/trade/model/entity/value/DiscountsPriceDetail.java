package com.wanmi.sbc.order.trade.model.entity.value;

import com.wanmi.sbc.marketing.bean.enums.MarketingType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>营销优惠金额明细</p>
 * Created by of628-wenzhi on 2018-04-19-下午9:52.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DiscountsPriceDetail implements Serializable{

    private static final long serialVersionUID = -5248352578031432110L;
    /**
     * 营销类型
     */
    private MarketingType marketingType;

    /**
     * 优惠金额
     */
    private BigDecimal discounts;
}
