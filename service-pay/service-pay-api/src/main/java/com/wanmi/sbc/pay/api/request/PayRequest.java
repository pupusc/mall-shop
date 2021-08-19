package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <p>支付请求参数</p>
 * Created by of628-wenzhi on 2017-08-04-下午5:59.
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class PayRequest extends PayBaseRequest {

    private static final long serialVersionUID = -8043632869428436067L;
    /**
     * 业务订(退)单号
     */
    @ApiModelProperty(value = "业务订(退)单号")
    @NotBlank
    private String businessId;

    /**
     * 支付项(具体支付渠道项)id
     */
    @ApiModelProperty(value = "支付项(具体支付渠道项)id")
    @NotNull
    private Long channelItemId;

    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    @NotNull
    private TerminalType terminal;

    /**
     * 订单交易总金额，单位元
     */
    @ApiModelProperty(value = "订单交易总金额，单位元")
    @NotNull
//    @Min(1L)
    private BigDecimal amount;

    /**
     * 发起支付请求的客户端ip
     */
    @ApiModelProperty(value = "发起支付请求的客户端ip")
    @NotBlank
    private String clientIp;

    /**
     * 商品标题
     */
    @ApiModelProperty(value = "商品标题")
    @NotBlank
    private String subject;

    /**
     * 商品描述信息
     */
    @ApiModelProperty(value = "商品描述信息")
    @NotBlank
    private String body;

    /**
     * 微信支付时必传，付款用户在商户 appid 下的唯一标识
     */
    @ApiModelProperty(value = "微信支付时必传，付款用户在商户 appid 下的唯一标识")
    private String openId;


    /**
     * 商户id-boss端取默认值
     */
    @ApiModelProperty(value = "商户id-boss端取默认值")
    @NotNull
    private Long storeId;
}
