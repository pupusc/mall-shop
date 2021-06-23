package com.wanmi.sbc.order.api.request.trade;

import com.wanmi.sbc.common.base.BaseRequest;
import com.wanmi.sbc.common.base.Operator;
import com.wanmi.sbc.customer.bean.vo.CustomerSimplifyOrderCommitVO;
import com.wanmi.sbc.customer.bean.vo.CustomerVO;
import com.wanmi.sbc.order.bean.vo.PointsTradeItemGroupVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

/**
 * <p>客户端提交积分订单参数结构，包含除商品信息外的其他必要参数</p>
 * Created by yinxianzhi on 2019-05-20-下午3:40.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel
public class PointsTradeCommitRequest extends BaseRequest {

    private static final long serialVersionUID = 4478928439116796662L;

    /**
     * 订单收货地址id，必传
     */
    @ApiModelProperty(value = "订单收货地址id")
    @NotBlank
    private String consigneeId;

    /**
     * 收货地址详细信息(包含省市区)，必传
     */
    @ApiModelProperty(value = "收货地址详细信息(包含省市区)")
    @NotBlank
    private String consigneeAddress;

    /**
     * 收货地址修改时间，可空
     */
    @ApiModelProperty(value = "收货地址修改时间")
    private String consigneeUpdateTime;

    /**
     * 积分商品id
     */
    @ApiModelProperty("积分商品id")
    @NotNull
    private String pointsGoodsId;

    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    @Range(min = 1)
    private long num;

    /**
     * 订单备注
     */
    @ApiModelProperty(value = "订单备注")
    private String buyerRemark;

    /**
     * 操作人
     */
    @ApiModelProperty(value = "操作人")
    private Operator operator;

    /**
     * 下单用户
     */
    @ApiModelProperty(value = "下单用户")
    private CustomerSimplifyOrderCommitVO customer;

    /**
     * 积分订单项数据
     */
    @ApiModelProperty(value = "积分订单项数据")
    private PointsTradeItemGroupVO pointsTradeItemGroup;
}
