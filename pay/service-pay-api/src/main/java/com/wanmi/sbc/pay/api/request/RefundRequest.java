package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>退款请求参数</p>
 * Created by of628-wenzhi on 2017-08-04-下午6:02.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefundRequest extends PayBaseRequest {

    private static final long serialVersionUID = 7485615721263948072L;
    /**
     * 退单业务id
     */
    @ApiModelProperty(value = "退单业务id")
    @NotNull
    private String refundBusinessId;

    /**
     * 关联的订单业务id
     */
    @ApiModelProperty(value = "关联的订单业务id")
    @NotNull
    private String businessId;

    /**
     * 退款金额，单位：元
     */
    @ApiModelProperty(value = "退款金额，单位：元")
    @NotNull
    private BigDecimal amount;

    /**
     * 订单金额，单位：元
     */
    @ApiModelProperty(value = "订单金额，单位：元")
    private BigDecimal totalPrice;

    /**
     * 发起退款请求的客户端ip
     */
    @ApiModelProperty(value = "发起退款请求的客户端ip")
    @NotBlank
    private String clientIp;

    /**
     * 退款描述
     */
    @ApiModelProperty(value = "退款描述")
    private String description;

    /**
     * 支付对象id，数据处理使用，无需传入
     */
    @ApiModelProperty(value = "支付对象id，数据处理使用，无需传入")
    private String payObjectId;

    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "商户id-boss端取默认值")
    @NotNull
    private Long storeId;
}
