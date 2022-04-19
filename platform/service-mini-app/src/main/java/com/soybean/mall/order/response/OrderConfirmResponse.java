package com.soybean.mall.order.response;

import com.wanmi.sbc.common.enums.DefaultFlag;
import com.wanmi.sbc.marketing.bean.vo.CouponCodeVO;
import com.wanmi.sbc.marketing.bean.vo.MarkupLevelDetailVO;
import com.wanmi.sbc.order.bean.vo.TradeConfirmItemVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderConfirmResponse implements Serializable {
    private static final long serialVersionUID = -2803790268288257008L;

    /**
     * 按商家拆分后的订单项
     */
    @ApiModelProperty(value = "按商家拆分后的订单项")
    private List<TradeConfirmItemVO> tradeConfirmItems;

    /**
     * 优惠券信息
     */
    private List<CouponCodeVO> couponCodes;

    /**
     * 订单总额
     */
    @ApiModelProperty(value = "订单总额")
    private BigDecimal totalPrice = BigDecimal.ZERO;

    /**
     * 商品总额
     */
    @ApiModelProperty(value = "商品总额")
    private BigDecimal goodsTotalPrice = BigDecimal.ZERO;

    /**
     * 优惠总额
     */
    @ApiModelProperty(value = "优惠总额")
    private BigDecimal discountsTotalPrice = BigDecimal.ZERO;
    

}
