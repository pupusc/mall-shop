package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.marketing.bean.vo.CouponInfoVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>客户端提交积分优惠券订单参数结构</p>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class PointsCouponTradeCommitRequest extends BaseRequest {

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
     * 下单用户
     */
    @ApiModelProperty(value = "下单用户")
    private CustomerSimplifyOrderCommitVO customer;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

}
