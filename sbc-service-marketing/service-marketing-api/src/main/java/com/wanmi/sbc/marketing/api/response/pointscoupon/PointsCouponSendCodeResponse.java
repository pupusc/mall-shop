package com.wanmi.sbc.marketing.api.response.pointscoupon;

import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p>积分兑换券发放券码结果</p>
 *
 * @author minchen
 */
@ApiModel
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointsCouponSendCodeResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 积分优惠券id
     */
    @ApiModelProperty(value = "积分优惠券id")
    private Long pointsCouponId;

    /**
     * 优惠券信息
     */
    @ApiModelProperty(value = "优惠券信息")
    private CouponInfoVO couponInfoVO;

    /**
     * 优惠券码id
     */
    @ApiModelProperty(value = "优惠券码id")
    private String couponCodeId;

    /**
     * 发放的优惠券码
     */
    @ApiModelProperty(value = "发放的优惠券码")
    private String couponCode;

    /**
     * 兑换积分
     */
    @ApiModelProperty(value = "兑换积分")
    private Long points;

    /**
     * 会员信息
     */
    @ApiModelProperty(value = "会员信息")
    private CustomerVO customer;
}
