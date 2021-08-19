package com.wanmi.sbc.pay.api.request;

import com.wanmi.sbc.pay.bean.enums.TerminalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @Auther: jiaojiao
 * @Date: 2018/10/16 10:18
 * @Description:
 */
@ApiModel
@EqualsAndHashCode(callSuper = true)
@Data
public class
UnionPayRequest extends PayBaseRequest {
    private static final long serialVersionUID = 6447066155217627362L;

    /**
     * 业务订(退)单号
     */
    @ApiModelProperty(value = "业务订(退)单号")
    @NotBlank
    private String businessId;
    /**
     * 终端
     */
    @ApiModelProperty(value = "终端")
    @NotNull
    private TerminalType terminal;

    /**
     * 支付项(具体支付渠道项)id
     */
    @ApiModelProperty(value = "支付项(具体支付渠道项)id")
    @NotNull
    private Long channelItemId;

    /**
     * 订单交易总金额，单位元
     */
    @ApiModelProperty(value = "订单交易总金额，单位元")
    @NotNull
    @Min(1L)
    private BigDecimal amount;

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
     * 前台页面跳转地址
     */
    @ApiModelProperty(value = "前台页面跳转地址")
    private String frontUrl;

    /**
     * 异步通知地址
     */
    @ApiModelProperty(value = "异步通知地址")
    private String notifyUrl;



    /**
     * 商户号
     */
    @ApiModelProperty(value = "商户号")
    private String apiKey;

    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private String encoding;
    /**
     * 订单支付发送时间戳
     */
    @ApiModelProperty(value = "订单支付发送时间戳")
    private String txnTime;

    /**
     * 发起支付请求的客户端ip
     */
    @ApiModelProperty(value = "发起支付请求的客户端ip")
    @NotBlank
    private String clientIp;


}
