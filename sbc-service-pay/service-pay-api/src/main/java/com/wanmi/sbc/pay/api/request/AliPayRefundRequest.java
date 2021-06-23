package com.wanmi.sbc.pay.api.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @program: service-pay
 * @description: 支付宝退款接口请求参数
 * @create: 2019-02-15 11:10
 **/
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AliPayRefundRequest extends PayBaseRequest {

    /**
     * 关联的订单业务id
     */
    @ApiModelProperty(value = "关联的订单业务id")
    @NotNull
    private String businessId;

    @ApiModelProperty(value = "退单业务id")
    private String refundBusinessId;

    /**
     * 退款金额，单位：元
     */
    @ApiModelProperty(value = "退款金额，单位：元")
    @NotNull
    private BigDecimal amount;

    /**
     * 退款描述
     */
    @ApiModelProperty(value = "退款描述")
    private String description;

    @ApiModelProperty(value = "支付宝APPID")
    private String appid;

    @ApiModelProperty(value = "支付宝商户私钥")
    private String appPrivateKey;

    @ApiModelProperty(value = "支付宝公钥")
    private String aliPayPublicKey;

}
