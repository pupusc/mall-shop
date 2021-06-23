package com.wanmi.sbc.order.api.request.payorder;

import com.wanmi.sbc.account.bean.enums.PayType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class GeneratePayOrderByOrderCodeRequest implements Serializable {

    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
    private String orderCode;

    /**
     * 会员主键
     */
    @ApiModelProperty(value = "会员主键")
    private String customerId;

    /**
     * 收款金额
     */
    @ApiModelProperty(value = "收款金额")
    private BigDecimal payOrderPrice;

    /**
     * 支付类型
     */
    @ApiModelProperty(value = "支付类型")
    private PayType payType;

    /**
     * 商家id
     */
    @ApiModelProperty(value = "商家id")
    private Long companyInfoId;

    /**
     * 下单时间
     */
    @ApiModelProperty(value = "下单时间")
    private LocalDateTime orderTime;
}
