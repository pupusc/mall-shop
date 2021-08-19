package com.wanmi.sbc.order.bean.vo;

import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class TradePointsCouponItemVO implements Serializable, Cloneable {

    private static final long serialVersionUID = 5114409655338848109L;

    @ApiModelProperty(value = "oid")
    private String oid;

    /**
     * 积分兑换的优惠券信息
     */
    private CouponInfoVO couponInfoVO;

}
